package ru.practicum.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.model.sensor.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Slf4j
public class SensorEventAvroMapper {
    public static SensorEventAvro mapToAvro(SensorEvent event) {
        log.info("Начали маппинг SensorEvent");
        switch (event.getType()) {
            case LIGHT_SENSOR_EVENT:
                log.info("Это LightSensorEvent !");
                LightSensorEvent lightEvent = (LightSensorEvent) event;
                return SensorEventAvro.newBuilder()
                        .setId(lightEvent.getId())
                        .setTimestamp(lightEvent.getTimestamp())
                        .setHubId(lightEvent.getHubId())
                        .setPayload(
                                LightSensorAvro.newBuilder()
                                        .setLinkQuality(lightEvent.getLinkQuality())
                                        .setLuminosity(lightEvent.getLuminosity())
                                        .build())
                        .build();

            case MOTION_SENSOR_EVENT:
                log.info("Это MOTION_SENSOR_EVENT !");
                MotionSensorEvent motionSensorEvent = (MotionSensorEvent) event;
                return SensorEventAvro.newBuilder()
                        .setId(motionSensorEvent.getId())
                        .setTimestamp(motionSensorEvent.getTimestamp())
                        .setHubId(motionSensorEvent.getHubId())
                        .setPayload(
                                MotionSensorAvro.newBuilder()
                                        .setLinkQuality(motionSensorEvent.getLinkQuality())
                                        .setMotion(motionSensorEvent.isMotion())
                                        .setVoltage(motionSensorEvent.getVoltage())
                                        .build())
                        .build();
            case CLIMATE_SENSOR_EVENT:
                log.info("Это CLIMATE_SENSOR_EVENT !");
                ClimateSensorEvent climateSensorEvent = (ClimateSensorEvent) event;
                return SensorEventAvro.newBuilder()
                        .setId(climateSensorEvent.getId())
                        .setTimestamp(climateSensorEvent.getTimestamp())
                        .setHubId(climateSensorEvent.getHubId())
                        .setPayload(
                                ClimateSensorAvro.newBuilder()
                                        .setHumidity(climateSensorEvent.getHumidity())
                                        .setCo2Level(climateSensorEvent.getCo2Level())
                                        .setTemperatureC(climateSensorEvent.getTemperatureC())
                                        .build())
                        .build();
            case SWITCH_SENSOR_EVENT:
                log.info("Это SWITCH_SENSOR_EVENT !");
                SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) event;
                return SensorEventAvro.newBuilder()
                        .setId(switchSensorEvent.getId())
                        .setTimestamp(switchSensorEvent.getTimestamp())
                        .setHubId(switchSensorEvent.getHubId())
                        .setPayload(
                                SwitchSensorAvro.newBuilder()
                                        .setState(switchSensorEvent.isState())
                                        .build())
                        .build();
            case TEMPERATURE_SENSOR_EVENT:
                log.info("Это TEMPERATURE_SENSOR_EVENT !");
                TemperatureSensorEvent temperatureSensorEvent = (TemperatureSensorEvent) event;
                return SensorEventAvro.newBuilder()
                        .setId(temperatureSensorEvent.getId())
                        .setTimestamp(temperatureSensorEvent.getTimestamp())
                        .setHubId(temperatureSensorEvent.getHubId())
                        .setPayload(
                                TemperatureSensorAvro.newBuilder()
                                        .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                                        .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                                        .build())
                        .build();
            default:
                throw new IllegalArgumentException("Неизвестный тип события");
        }
    }
}