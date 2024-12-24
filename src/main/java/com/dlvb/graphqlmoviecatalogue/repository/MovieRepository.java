package com.dlvb.graphqlmoviecatalogue.repository;

import com.dlvb.graphqlmoviecatalogue.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing {@link Movie} entities in the database.
 * @author Matushkin Anton
 */
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
