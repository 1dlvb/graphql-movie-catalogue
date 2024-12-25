package com.dlvb.graphqlmoviecatalogue.grpc;

import com.dlvb.graphqlmoviecatalogue.MovieSearchServiceGrpc;
import com.dlvb.graphqlmoviecatalogue.Search;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieSearchClientTests {

    @Mock
    private MovieSearchServiceGrpc.MovieSearchServiceBlockingStub blockingStub;

    @InjectMocks
    private MovieSearchClient movieSearchClient;

    private String query;
    private int pageNumber;
    private int pageSize;

    @BeforeEach
    void setUp() {
        query = "Action";
        pageNumber = 1;
        pageSize = 10;
    }

    @Test
    void testSearchMoviesWhenSuccess() {
        Search.SearchResponse mockResponse = Search.SearchResponse.newBuilder()
                .addMovies(Search.Movie.newBuilder()
                        .setId("1")
                        .setTitle("Action Movie")
                        .setDescription("A great action movie").build())
                .build();

        when(blockingStub.searchMovies(any(Search.SearchRequest.class))).thenReturn(mockResponse);

        Search.SearchResponse response = movieSearchClient.searchMovies(query, pageNumber, pageSize);

        assertNotNull(response);
        assertEquals(1, response.getMoviesList().size());
        assertEquals("Action Movie", response.getMoviesList().getFirst().getTitle());
        assertEquals("A great action movie", response.getMoviesList().getFirst().getDescription());

        verify(blockingStub, times(1)).searchMovies(any(Search.SearchRequest.class));
    }

    @Test
    void testSearchMoviesWhenEmptyQuery() {
        query = "";

        Search.SearchResponse mockResponse = Search.SearchResponse.newBuilder()
                .addMovies(Search.Movie.newBuilder().setId("1").setTitle("Default Movie")
                        .setDescription("A default movie").build())
                .build();

        when(blockingStub.searchMovies(any(Search.SearchRequest.class))).thenReturn(mockResponse);

        Search.SearchResponse response = movieSearchClient.searchMovies(query, pageNumber, pageSize);

        assertNotNull(response);
        assertEquals(1, response.getMoviesList().size());
        assertEquals("Default Movie", response.getMoviesList().getFirst().getTitle());
        assertEquals("A default movie", response.getMoviesList().getFirst().getDescription());

        verify(blockingStub, times(1)).searchMovies(any(Search.SearchRequest.class));
    }

    @Test
    void testSearchMoviesWhenNullQuery() {
        query = null;

        Search.SearchResponse mockResponse = Search.SearchResponse.newBuilder()
                .addMovies(Search.Movie.newBuilder()
                        .setId("1")
                        .setTitle("Default Movie")
                        .setDescription("A default movie")
                        .build())
                .build();

        when(blockingStub.searchMovies(any(Search.SearchRequest.class))).thenReturn(mockResponse);

        Search.SearchResponse response = movieSearchClient.searchMovies(query, pageNumber, pageSize);

        assertNotNull(response);
        assertEquals(1, response.getMoviesList().size());
        assertEquals("Default Movie", response.getMoviesList().getFirst().getTitle());
        assertEquals("A default movie", response.getMoviesList().getFirst().getDescription());

        verify(blockingStub, times(1)).searchMovies(any(Search.SearchRequest.class));
    }

    @Test
    void testSearchMoviesWhenEmptyResponse() {
        Search.SearchResponse mockResponse = Search.SearchResponse.newBuilder().build();

        when(blockingStub.searchMovies(any(Search.SearchRequest.class))).thenReturn(mockResponse);

        Search.SearchResponse response = movieSearchClient.searchMovies(query, pageNumber, pageSize);

        assertNotNull(response);
        assertEquals(0, response.getMoviesList().size());

        verify(blockingStub, times(1)).searchMovies(any(Search.SearchRequest.class));
    }

}
