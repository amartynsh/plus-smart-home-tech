package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "sensors")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sensor {
    @Id
    private String id;
    @Column(name = "hub_id")
    private String hubId;

    @Override
    public String toString() {
        return "Sensor{" +
                "id='" + id + '\'' +
                ", hubId='" + hubId + '\'' +
                '}';
    }
}



