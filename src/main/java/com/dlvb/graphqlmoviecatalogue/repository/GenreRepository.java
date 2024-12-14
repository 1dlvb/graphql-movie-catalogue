package com.dlvb.graphqlmoviecatalogue.repository;

import com.dlvb.graphqlmoviecatalogue.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    Genre findGenreByName(String name);

}
