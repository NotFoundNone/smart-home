package dev.project.grpc.grpcservice;

import dev.project.grpc.grpcservice.service.DeviceService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class DeviceControlServiceImpl extends DeviceControlServiceGrpc.DeviceControlServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceControlServiceImpl.class);

    @Autowired
    private DeviceService deviceService;

    @Override
    public void turnOnDevice(DeviceRequest request, StreamObserver<DeviceResponse> responseObserver) {
        String action = request.getAction();
        Double averageTemperature = request.getTemperature();

        LOGGER.info("action = {}", action);

        if ("temperature".equals(action)) {
            LOGGER.info("Start processTemperatureAction");
            deviceService.processTemperatureAction(averageTemperature);
        }

        // Формируем и отправляем ответ
        DeviceResponse response = DeviceResponse.newBuilder()
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
