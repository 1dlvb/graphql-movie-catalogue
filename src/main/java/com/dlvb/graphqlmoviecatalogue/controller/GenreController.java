
package com.dlvb.graphqlmoviecatalogue.controller;

import com.dlvb.graphqlmoviecatalogue.model.Genre;
import com.dlvb.graphqlmoviecatalogue.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @MutationMapping
    public Genre addGenre(@Argument String name) {
        return genreService.addGenre(name);
    }

    @MutationMapping
    public Genre updateGenre(@Argument Long id, @Argument String name) {
        return genreService.updateGenre(id, name);
    }

    @MutationMapping
    public Genre deleteGenre(@Argument Long id) {
        return genreService.deleteGenre(id);
    }

}
