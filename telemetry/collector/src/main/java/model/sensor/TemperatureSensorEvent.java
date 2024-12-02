package model.sensor;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import model.sensor.constants.SensorEventType;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class TemperatureSensorEvent extends SensorEvent {
    private int temperatureC;
    private int temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
