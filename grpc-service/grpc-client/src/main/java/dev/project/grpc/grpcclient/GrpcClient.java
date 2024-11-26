package dev.project.grpc.grpcclient;

import dev.project.grpc.grpcservice.DeviceControlServiceGrpc;
import dev.project.grpc.grpcservice.DeviceRequest;
import dev.project.grpc.grpcservice.DeviceResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class GrpcClient implements AutoCloseable{

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
			System.err.println("Thread interrupted while shutting down channel");
		}
	}

	public DeviceResponse turnOnDevice(Double averageTemperature){
		DeviceRequest request = DeviceRequest.newBuilder()
				.setAction("temperature")
				.setTemperature(averageTemperature)
				.build();

		DeviceResponse response = blockingStub.turnOnDevice(request);

		System.out.println("Ответ от сервера: " + response.getMessage());
		return response;
	}
}
