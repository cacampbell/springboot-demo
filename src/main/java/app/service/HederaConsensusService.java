package app.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.hedera.hashgraph.proto.mirror.ConsensusTopicQuery;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.consensus.ConsensusMessageSubmitTransaction;
import com.hedera.hashgraph.sdk.consensus.ConsensusTopicId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import com.hedera.hashgraph.sdk.mirror.MirrorClient;
import com.hedera.hashgraph.sdk.mirror.MirrorConsensusTopicQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import app.domain.Category;

@Service
public class HederaConsensusService {

    @Autowired
    private CategorySerializer serializer;

    @Value("${hedera.topic_id}")
    String topicIdStr;

    @Value("${hedera.operator_id}")
    String operatorIdStr;

    @Value("${hedera.operator_key}")
    String operatorKeyStr;

    @Value("${hedera.network_name}")
    String networkName;

    @Value("${hedera.mirror_node_address}")
    String mirrorNodeAddressStr;

    @Value("${hedera.submit_key}")
    String submitKeyStr;

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

    public void postAsync(Category category) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonFactory factory = new JsonFactory();
        JsonGenerator generator = factory.createGenerator(outputStream);
        serializer.serialize(category, generator, null);
        byte[] msg = outputStream.toByteArray();
        Transaction consensusTransaction = new ConsensusMessageSubmitTransaction()
                    .setTopicId(this.topicId)
                    .setMessage(msg)
                    .build(this.client);

            if (this.submitKey != null) {
                consensusTransaction.sign(this.submitKey);
            }

            consensusTransaction.executeAsync(this.client, System.out::print, System.err::print);
    }

	public void subscribe(PrintStream out) {
        new MirrorConsensusTopicQuery()
            .setTopicId(this.topicId)
            .subscribe(this.mirrorClient, resp -> {
                out.println("[" + resp.consensusTimestamp + "] Received HCS Message: " + resp.message);
            }, Throwable::printStackTrace);
	}

    HederaConsensusService() {
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