package com.example.field.model;

import com.example.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    @Basic
    private String description;

    @Column(precision = 5, scale = 2)
    private BigDecimal pricePerHour;

    @Basic
    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}