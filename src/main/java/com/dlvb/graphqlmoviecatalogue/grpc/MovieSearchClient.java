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

    public Search.SearchResponse searchMovies(String query) {
        Search.SearchRequest request = Search.SearchRequest.newBuilder().setQuery(query).build();

        return blockingStub.searchMovies(request);
    }

}
