package com.dlvb.graphqlmoviecatalogue.repository;

import com.dlvb.graphqlmoviecatalogue.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
