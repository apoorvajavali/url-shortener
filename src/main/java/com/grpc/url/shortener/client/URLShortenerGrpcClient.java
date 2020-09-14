package com.grpc.url.shortener.client;

import com.proto.shortenurl.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class URLShortenerGrpcClient {

    private ManagedChannel channel;
    private ShortenURLServiceGrpc.ShortenURLServiceBlockingStub syncClient;

    public URLShortenerGrpcClient() {
        System.out.println("\nInitialising client");
        channel = ManagedChannelBuilder.forAddress("localhost", 5000)
                .usePlaintext()
                .build();
        syncClient = ShortenURLServiceGrpc.newBlockingStub(channel);
    }

    public String getShortURL(String full_url){
        ShortenURLRequest request = ShortenURLRequest.newBuilder()
                .setFullUrl(full_url)
                .build();

        ShortenURLResponse response = syncClient.shortenURL(request);
        System.out.println("Shortened URL output from gRPC server: "+response.getShortUrl());

        shutDownChannel();
        return response.getShortUrl();
    }

    public String getFullURL(String id) {
        GetFullURLRequest request = GetFullURLRequest.newBuilder()
                .setHashedId(id)
                .build();

        GetFullURLResponse response = syncClient.getFullURL(request);
        System.out.println("Full URL output from gRPC server: "+response.getFullUrl());

        shutDownChannel();
        return response.getFullUrl();
    }

    private void shutDownChannel(){
        System.out.println("Shutting down the channel");
        channel.shutdown();
    }
}
