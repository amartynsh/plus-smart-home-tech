package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "conditions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;
    @Enumerated(EnumType.STRING)
    ConditionType type;
    @Enumerated(EnumType.STRING)
    ConditionOperation operation;
    @Column(name = "value")
    Integer value;
    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;
}