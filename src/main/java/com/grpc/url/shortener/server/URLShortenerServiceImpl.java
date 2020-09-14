package com.grpc.url.shortener.server;

import com.google.common.hash.Hashing;
import com.proto.shortenurl.*;
import io.grpc.stub.StreamObserver;

import java.nio.charset.StandardCharsets;
import java.sql.*;

public class URLShortenerServiceImpl extends ShortenURLServiceGrpc.ShortenURLServiceImplBase {

    @Override
    public void shortenURL(ShortenURLRequest request, StreamObserver<ShortenURLResponse> responseObserver) {
        String full_url = request.getFullUrl();

        String id = Hashing.murmur3_32().hashString(full_url, StandardCharsets.UTF_8).toString();

        try (Connection conn = HikariCPDataSource.getConnection()) {
            String sql = "select id from urls where id = '" + id + "'";

            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery(sql);

            if (!res.next()) {
                String insert_sql = "insert into urls (id, full_url) VALUES (?, ?)";

                PreparedStatement preparedStatement = conn.prepareStatement(insert_sql);
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, full_url);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted == 1) {
                    System.out.println("New URL Data Persisted!");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        ShortenURLResponse response = ShortenURLResponse.newBuilder()
                .setShortUrl("http://localhost:8080/s/"+id)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getFullURL(GetFullURLRequest request, StreamObserver<GetFullURLResponse> responseObserver) {
        String id = request.getHashedId();

        try (Connection conn = HikariCPDataSource.getConnection()) {
            String sql = "select full_url from urls where id = '" + id + "'";
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery(sql);
            if (res.next()) {
                GetFullURLResponse response = GetFullURLResponse.newBuilder()
                        .setFullUrl(res.getString("full_url"))
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
