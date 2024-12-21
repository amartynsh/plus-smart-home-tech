package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conditions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    @Override
    public String toString() {
        return "Condition{" +
                "id=" + id +
                ", sensor=" + sensor +
                ", type=" + type +
                ", operation=" + operation +
                ", value=" + value +
                '}';
    }
}