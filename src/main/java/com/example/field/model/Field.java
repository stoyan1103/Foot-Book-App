package com.example.field.model;

import com.example.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fields")
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private City city;

    @Column(nullable = false)
    private String locationUrl;

    @Column(nullable = false)
    private LocalTime openHour;

    @Column(nullable = false)
    private LocalTime closeHour;

    @Column(nullable = false)
    private String imageUrl;

    @Basic
    private String description;

    @Column(precision = 5, scale = 2)
    private BigDecimal pricePerHour;

    @Basic
    private boolean isAvailable;

    @Column
    private boolean isApproved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;
}