package com.example.user.model;

import com.example.team_history.model.TeamHistory;
import com.example.statistics.model.Statistics;
import com.example.team.model.Team;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("PLAYER")
public class Player extends User {

    @Column(nullable = false)
    private List<String> positions;

    @Column(nullable = false)
    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team currentTeam;

    @OneToOne(fetch = FetchType.LAZY)
    private Statistics stats;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamHistory> teamHistory;
}