package dev.project.grpc.grpcservice;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//public class GrpcServiceApplication {
//	public static void main(String[] args) throws Exception {
//		Server server = ServerBuilder.forPort(8080)
//				.addService(new DeviceControlServiceImpl())
//				.build();
//
//		System.out.println("Запуск gRPC сервера на порту 8080...");
//		server.start();
//		server.awaitTermination();
//	}
//}
@SpringBootApplication
public class GrpcServiceApplication implements CommandLineRunner {

	@Autowired
	private Server grpcServer;

	public static void main(String[] args) {
		SpringApplication.run(GrpcServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Запуск gRPC сервера на порту 8080...");
		grpcServer.start();
		grpcServer.awaitTermination();
	}
}