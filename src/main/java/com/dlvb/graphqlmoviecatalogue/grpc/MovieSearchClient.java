package com.dlvb.graphqlmoviecatalogue.grpc;

import com.dlvb.graphqlmoviecatalogue.MovieSearchServiceGrpc;
import com.dlvb.graphqlmoviecatalogue.Search;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service client for searching movies using gRPC.
 * @author Matushkin Anton
 */
@Service
@RequiredArgsConstructor
public class MovieSearchClient {

    @NonNull
    private final MovieSearchServiceGrpc.MovieSearchServiceBlockingStub blockingStub;

    /**
     * Searches for movies based on the provided query string, page number, and page size.
     *
     * @param query The search query (optional, defaults to empty string if null or empty)
     * @param pageNumber The page number for pagination
     * @param pageSize The number of items per page
     * @return The search response containing the list of movies
     */
    public Search.SearchResponse searchMovies(String query, Integer pageNumber, Integer pageSize) {
        Search.SearchRequest.Builder requestBuilder = Search.SearchRequest.newBuilder();

        requestBuilder.setQuery((query == null || query.isEmpty()) ? "" : query);

        requestBuilder.setPageNumber(pageNumber);
        requestBuilder.setPageSize(pageSize);

        Search.SearchRequest request = requestBuilder.build();

        return blockingStub.searchMovies(request);
    }

}
