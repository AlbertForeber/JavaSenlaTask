package com.senla.app.model.entity.auth;

import jakarta.persistence.*;

@Entity(name = "scopes")
public class Scope {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scopes_seq")
    @SequenceGenerator(name = "scopes_seq", sequenceName = "scopes_id_seq", allocationSize = 1)
    private Integer id;

    private String title;

    public Scope() { }

    public Scope(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
