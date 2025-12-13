package com.tonorg.model;

import jakarta.persistence.*;

@Entity
public class Laboratory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String city;

    private String contactEmail;

    // 👇 nouveau champ : l'utilisateur admin du labo
    @OneToOne
    @JoinColumn(name = "admin_user_id")
    private User adminUser;

    // getters / setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getCity() {
        return city;
    }

    public void setCity(String city) { this.city = city; }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public User getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(User adminUser) {
        this.adminUser = adminUser;
    }
}
