package com.example.statistics.model;

import com.example.user.model.Player;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "statistics")
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player_id", unique = true)
    private Player player;

    private int totalGoals;

    private int totalAssists;

    private int yellowCards;

    private int redCards;

    private int minutesPlayed;
}