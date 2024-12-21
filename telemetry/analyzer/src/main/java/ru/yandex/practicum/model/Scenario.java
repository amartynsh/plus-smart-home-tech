package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "scenarios")
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Scenario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "hub_id")
    private String hubId;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "scenario", fetch = FetchType.EAGER)
    @Builder.Default
    private List<Condition> conditions = new ArrayList<>();
    @OneToMany(mappedBy = "scenario", fetch = FetchType.EAGER)
    @Builder.Default
    private List<Action> actions = new ArrayList<>();

    @Override
    public String toString() {
        return "Scenario{" +
                "name='" + name + '\'' +
                ", hubId='" + hubId + '\'' +
                ", id=" + id +
                '}';
    }
}