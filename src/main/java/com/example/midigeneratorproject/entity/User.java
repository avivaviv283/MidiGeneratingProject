package com.example.midigeneratorproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false, length = 64)
    private String password;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="role")
    private Role role;


    public List getRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        return roles;
    }



}
