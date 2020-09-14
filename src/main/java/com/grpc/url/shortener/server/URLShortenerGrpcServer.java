package com.grpc.url.shortener.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class URLShortenerGrpcServer {

    private Server server;

    public URLShortenerGrpcServer() {
        server = ServerBuilder.forPort(5000)
                .addService(new URLShortenerServiceImpl())
                .build();
    }

    public void startgRPCServer() throws IOException, InterruptedException{
        server.start();
        System.out.println("gRPC server started. Listening on port 5000");
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            System.out.println("Received Shutdown");
            server.shutdown();
            System.out.println(("Successfully stopped the server"));
        }));
        server.awaitTermination();
        return;
    }

    public void stopgRPCServer(){
        server.shutdown();
    }
}
