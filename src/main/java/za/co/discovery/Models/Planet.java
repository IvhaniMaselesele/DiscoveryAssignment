package za.co.discovery.Models;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "planet")
public class Planet {

    @Id
    @Column(nullable = false)
    private String node;

    @Column
    private String name;

    public Planet() {
    }

    public Planet(String node, String name) {
        this.name = name;
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
