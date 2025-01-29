package com.dlvb.graphqlmoviecatalogue.config;

import com.dlvb.graphqlmoviecatalogue.MovieSearchServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up gRPC client beans.
 * <p>
 * This class configures a gRPC client for the {@link MovieSearchServiceGrpc.MovieSearchServiceBlockingStub},
 * </p>
 * @author Matushkin Anton
 */
@Configuration
public class GrpcClientConfig {

    @GrpcClient("movieSearchService")
    private MovieSearchServiceGrpc.MovieSearchServiceBlockingStub blockingStub;

    @Bean
    public MovieSearchServiceGrpc.MovieSearchServiceBlockingStub movieSearchServiceBlockingStub() {
        return blockingStub;
    }

}
