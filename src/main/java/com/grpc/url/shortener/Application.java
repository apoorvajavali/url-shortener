package com.grpc.url.shortener;

import com.grpc.url.shortener.server.URLShortenerGrpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(Application.class, args);

		URLShortenerGrpcServer grpcServer = new URLShortenerGrpcServer();
		grpcServer.startgRPCServer();
	}

}
