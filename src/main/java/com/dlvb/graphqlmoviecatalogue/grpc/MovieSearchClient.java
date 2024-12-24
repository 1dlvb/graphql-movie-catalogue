package com.dlvb.graphqlmoviecatalogue.grpc;

import com.dlvb.graphqlmoviecatalogue.MovieSearchServiceGrpc;
import com.dlvb.graphqlmoviecatalogue.Search;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieSearchClient {

    @NonNull
    private final MovieSearchServiceGrpc.MovieSearchServiceBlockingStub blockingStub;

    public Search.SearchResponse searchMovies(String query, Integer pageNumber, Integer pageSize) {
        Search.SearchRequest.Builder requestBuilder = Search.SearchRequest.newBuilder();

        requestBuilder.setQuery((query == null || query.isEmpty()) ? "" : query);

        requestBuilder.setPageNumber(pageNumber);
        requestBuilder.setPageSize(pageSize);

        Search.SearchRequest request = requestBuilder.build();

        return blockingStub.searchMovies(request);
    }

}
