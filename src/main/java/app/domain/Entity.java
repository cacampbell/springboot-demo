package app.domain;

import org.neo4j.ogm.annotation.*;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Entity {
    @Id
    @GeneratedValue
    private Long id = null;

    @Relationship(type = "CHILDREN")
    private Set<Entity> children = new HashSet<Entity>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
