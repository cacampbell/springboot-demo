package app.service;

import java.io.IOException;
import java.io.PrintStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.consensus.ConsensusMessageSubmitTransaction;
import com.hedera.hashgraph.sdk.consensus.ConsensusTopicId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import com.hedera.hashgraph.sdk.mirror.MirrorClient;
import com.hedera.hashgraph.sdk.mirror.MirrorConsensusTopicQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import app.domain.Entity;

@Service
public class HederaConsensusService {
    @Autowired
    private ObjectMapper objectMapper;

    private final String topicIdStr;
    private final String operatorIdStr;
    private final String operatorKeyStr;
    private final String networkName;
    private final String mirrorNodeAddressStr;
    private final String submitKeyStr;
    private final Client client;
    private final MirrorClient mirrorClient;
    private final AccountId operatorId;
    private final Ed25519PrivateKey operatorKey;
    private final ConsensusTopicId topicId;
    private final Ed25519PrivateKey submitKey;

    public boolean isTestnet() {
        return this.networkName.contains("test");
    }

    private Client constructClient() {
        Client client = null;

        if (this.isTestnet()) {
            client = Client.forTestnet();
        } else if (!this.isTestnet()) {
            client = Client.forMainnet();
        }

        if (client != null) {
            client.setOperator(this.operatorId, this.operatorKey);
        }

        return client;
    }

    public Client getClient() {
        return this.client;
    }

    private MirrorClient constructMirrorClient() {
        return new MirrorClient(this.mirrorNodeAddressStr);
    }

    public MirrorClient getMirrorClient() {
        return this.mirrorClient;
    }

    private Transaction hcsMessage(final Entity entity) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(entity);
        byte[] msg = json.getBytes();
        Transaction consensusTransaction = new ConsensusMessageSubmitTransaction()
                    .setTopicId(this.topicId)
                    .setMessage(msg)
                    .build(this.client);

            if (this.submitKey != null) {
                consensusTransaction.sign(this.submitKey);
            }

        return consensusTransaction;
    }

    public void postAsync(final Entity entity) throws IOException, JsonProcessingException {
        Transaction consensusTransaction = this.hcsMessage(entity);
        consensusTransaction.executeAsync(this.client, System.out::print, System.err::print);
    }

    public TransactionReceipt postSync(final Entity entity) throws JsonProcessingException, HederaStatusException {
        Transaction consensusTransaction = this.hcsMessage(entity);
        return consensusTransaction.execute(this.client).getReceipt(this.client);
    }

	public void subscribe(PrintStream out) {
        new MirrorConsensusTopicQuery()
            .setTopicId(this.topicId)
            .subscribe(this.mirrorClient, resp -> {
                out.println("[" + resp.consensusTimestamp + "] Received HCS Message: " + resp.message);
            }, Throwable::printStackTrace);
    }
    
    // Use Autowired for the Value annotations in constructor
    // Cannot use @Value on instance members because they won't
    // be available at construction time of the spring context
    @Autowired
    HederaConsensusService(
        @Value("${spring.hedera.topic_id}") final String topicIdStr,
        @Value("${spring.hedera.operator_id}") final String operatorIdStr,
        @Value("${spring.hedera.operator_key}") final String operatorKeyStr,
        @Value("${spring.hedera.network_name}") final String networkName,
        @Value("${spring.hedera.mirror_node_address}") final String mirrorNodeAddressStr,
        @Value("${spring.hedera.submit_key}") final String submitKeyStr) 
    {
        this.topicIdStr = topicIdStr;
        this.operatorIdStr = operatorIdStr;
        this.operatorKeyStr = operatorKeyStr;
        this.networkName = networkName;
        this.mirrorNodeAddressStr = mirrorNodeAddressStr;
        this.submitKeyStr = submitKeyStr;
        this.operatorId = AccountId.fromString(this.operatorIdStr);
        this.operatorKey = Ed25519PrivateKey.fromString(this.operatorKeyStr);
        this.topicId = ConsensusTopicId.fromString(this.topicIdStr);

        if (this.submitKeyStr != null && !this.submitKeyStr.isEmpty()) {
            this.submitKey = Ed25519PrivateKey.fromString(this.submitKeyStr);
        } else {
            this.submitKey = null;
        }
        
        this.client = constructClient();
        this.mirrorClient = constructMirrorClient();
    }
}