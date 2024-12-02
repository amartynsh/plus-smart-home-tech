package model.sensor;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import model.sensor.constants.SensorEventType;
import org.springframework.stereotype.Component;

@Component
@EqualsAndHashCode(callSuper = true)

@Getter
@Setter
public class LightSensorEvent extends SensorEvent {
    private int linkQuality;
    private int luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
