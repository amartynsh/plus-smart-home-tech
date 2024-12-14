package ru.practicum.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.*;

@GrpcService
public class EventCollector extends CollectorControllerGrpc.CollectorControllerImplBase {


    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            SensorEventProto.PayloadCase payloadCase = request.getPayloadCase();

            switch (payloadCase) {
                case SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT:
                    System.out.println("Получено событие датчика освещённости");
                    // получаем данные датчика освещённости
                    LightSensorProto lightSensor = request.getLightSensorEvent();
                    System.out.println("Уровень освещённости: " + lightSensor.getLuminosity());
                    break;
                case SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT:
                    System.out.println("Получено событие климатического датчика");
                    // получаем данные климатического датчика
                    ClimateSensorProto climateSensor = request.getClimateSensorEvent();
                    System.out.println("Влажность воздуха: " + climateSensor.getHumidity());
                    break;

                case SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT:
                    System.out.println("Получено событие температурного датчика");
                    TemperatureSensorProto temperatureSensorEvent = request.getTemperatureSensorEvent();
                    System.out.println("Температура: " + temperatureSensorEvent.getTemperatureC());
                    break;
                case SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT:
                    System.out.println("Получено событие датчика движения");
                    MotionSensorProto motionSensorEvent = request.getMotionSensorEvent();
                    System.out.println("Движение: " + motionSensorEvent.getMotion());
                    break;
                case SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT:
                    System.out.println("Получено событие датчика переключения");
                    SwitchSensorProto switchSensorEvent = request.getSwitchSensorEvent();
                    break;

                default:
                    System.out.println("Получено событие неизвестного типа: " + payloadCase);
            }
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new RuntimeException("Ошибка при обработке запроса"));
            Status.INTERNAL
                    .withDescription(e.getLocalizedMessage())
                    .withCause(e);

        }
    }
}