package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "actions")
@Data
@Builder
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