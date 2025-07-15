package com.example.user.model;


import com.example.team.model.Team;
import com.example.team_history.model.TeamHistory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("COACH")
public class Coach extends User {

    @Basic
    private String licenceLevel;

    @Basic
    private int experienceYears;

    @OneToOne(mappedBy = "coach")
    private Team currentTeam;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamHistory> coachedTeams;
}
