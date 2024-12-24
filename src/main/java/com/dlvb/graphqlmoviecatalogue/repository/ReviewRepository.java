package com.dlvb.graphqlmoviecatalogue.repository;

import com.dlvb.graphqlmoviecatalogue.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing {@link Review} entities in the database.
 * @author Matushkin Anton
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
