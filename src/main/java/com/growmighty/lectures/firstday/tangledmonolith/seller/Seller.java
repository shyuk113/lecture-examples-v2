package com.growmighty.lectures.firstday.tangledmonolith.seller;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sellers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    public static Seller create(Long userId) {
        Seller seller = new Seller();
        seller.userId = userId;
        
        return seller;
    }
}
