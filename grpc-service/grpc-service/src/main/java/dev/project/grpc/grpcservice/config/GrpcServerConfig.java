package dev.project.grpc.grpcservice.config;

import dev.project.grpc.grpcservice.DeviceControlServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {

    @Autowired
    private DeviceControlServiceImpl deviceControlService;

    @Bean
    public Server grpcServer() throws Exception {
        return ServerBuilder.forPort(8080)
                .addService(deviceControlService)
                .build();
    }
}