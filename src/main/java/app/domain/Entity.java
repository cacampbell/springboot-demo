package app.domain;

import org.neo4j.ogm.annotation.*;

import java.util.HashSet;
import java.util.Set;

import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.consensus.ConsensusTopicId;

@NodeEntity
public class Entity {
    @Id
    @GeneratedValue
    private Long id = null;

    private String name = "";
    private String contents = "";

    // Annotate consensus information here?
    private ConsensusTopicId topic = null;
    private int sequenceNumber = -1;
    private TransactionId transactionId = null;

    // Updates to entities make children?
    @Relationship(type = "CHILDREN")
    private Set<Entity> children = new HashSet<Entity>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public TransactionId getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(TransactionId transactionId) {
        this.transactionId = transactionId;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public ConsensusTopicId getTopic() {
        return topic;
    }

    public void setTopic(ConsensusTopicId topic) {
        this.topic = topic;
    }

    public Set<Entity> getChildren() {
        return children;
    }

    public void setChildren(Set<Entity> children) {
        this.children = children;
    }

    public void addChild(Entity entity) {
        this.children.add(entity);
    }

    public void addParent(Entity entity) {
        entity.children.add(this);
    }

    public void removeChild(Entity entity) {
        this.children.remove(entity);
    }

    public void removeParent(Entity entity) {
        entity.children.remove(this);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Node ").append(id);

       if (!children.isEmpty()) {
           builder.append(": ").append(children.toString());
       }

        return builder.toString();
    }
}
