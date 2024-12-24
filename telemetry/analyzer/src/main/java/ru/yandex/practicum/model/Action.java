package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "actions")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ActionType type;
    @Column(name = "value")
    private Integer value;
    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;
}