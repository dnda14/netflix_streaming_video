package com.netflix.contentservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.netflix.contentservice.model.Genre;
import com.netflix.contentservice.model.Movie;

@Repository
public interface ContentRepository extends JpaRepository<Movie, String> {

    List<Movie> findByGenre(Genre genre);
    List<Movie> findByTitleContainingIgnoreCase(String title);
}
