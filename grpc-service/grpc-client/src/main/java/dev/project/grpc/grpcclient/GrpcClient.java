package dev.project.grpc.grpcclient;

import dev.project.grpc.grpcservice.DeviceControlServiceGrpc;
import dev.project.grpc.grpcservice.DeviceRequest;
import dev.project.grpc.grpcservice.DeviceResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class GrpcClient implements AutoCloseable{

	private static final Logger LOGGER = LoggerFactory.getLogger(GrpcClient.class);

	private final ManagedChannel channel;
	private final DeviceControlServiceGrpc.DeviceControlServiceBlockingStub blockingStub;

	public GrpcClient() {
		this.channel = ManagedChannelBuilder
				.forAddress("localhost", 8080)
				.usePlaintext()
				.build();
		this.blockingStub = DeviceControlServiceGrpc.newBlockingStub(channel);
	}

	@Override
	public void close() throws Exception {
		try {
			channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			LOGGER.info("Thread interrupted while shutting down channel");
		}
	}

	public DeviceResponse turnOnDevice(Double averageTemperature){
		DeviceRequest request = DeviceRequest.newBuilder()
				.setAction("temperature")
				.setTemperature(averageTemperature)
				.build();

		DeviceResponse response = blockingStub.turnOnDevice(request);

		LOGGER.info("Ответ от сервера: " + response.getMessage());
		return response;
	}
}
