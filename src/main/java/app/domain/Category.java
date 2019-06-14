package app.domain;

import org.neo4j.ogm.annotation.*;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Category {
    @Id
    @GeneratedValue
    private Long id = null;

    @Relationship(type = "CHILDREN")
    private Set<Category> children = new HashSet<Category>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    public void addChild(Category entity) {
        this.children.add(entity);
    }

    public void addParent(Category entity) {
        entity.children.add(this);
    }

    public void removeChild(Category entity) {
        this.children.remove(entity);
    }

    public void removeParent(Category entity) {
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
