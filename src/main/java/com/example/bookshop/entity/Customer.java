package com.example.bookshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String address;

    private State state;
    private String email;
    private String phone;

    public Customer(int id, String name, String address, State state, String email, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.state = state;
        this.email = email;
        this.phone = phone;
    }
}
