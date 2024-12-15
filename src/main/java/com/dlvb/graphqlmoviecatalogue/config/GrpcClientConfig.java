package com.dlvb.graphqlmoviecatalogue.config;

import com.dlvb.graphqlmoviecatalogue.MovieSearchServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @GrpcClient("movieSearchService")
    private MovieSearchServiceGrpc.MovieSearchServiceBlockingStub blockingStub;

    @Bean
    public MovieSearchServiceGrpc.MovieSearchServiceBlockingStub movieSearchServiceBlockingStub() {
        return blockingStub;
    }

}
