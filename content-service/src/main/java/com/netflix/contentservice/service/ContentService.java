package com.netflix.contentservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.netflix.contentservice.dto.MovieRequest;
import com.netflix.contentservice.dto.MovieResponse;
import com.netflix.contentservice.model.Genre;
import com.netflix.contentservice.model.Movie;
import com.netflix.contentservice.model.VideoStatus;
import com.netflix.contentservice.repository.ContentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    public MovieResponse addMovie(MovieRequest movieRequest) {
        log.info("adding new Video{}",movieRequest.getTitle());

        Movie movie = new Movie();
        movie.setTitle(movieRequest.getTitle());
        movie.setDescription(movieRequest.getDescription());
        movie.setGenre(movieRequest.getGenre());
        movie.setDirector(movieRequest.getDirector());
        movie.setCast(movieRequest.getCast());
        movie.setReleaseYear(movieRequest.getReleaseYear());
        movie.setDuration(movieRequest.getDuration());
        movie.setThumbnailUrl(movieRequest.getThumbnailUrl());

        Movie savedMovie = contentRepository.save(movie);
        log.info("saving movie {}",movieRequest.getTitle());

        return mapToResponse(savedMovie);
    }

    public MovieResponse mapToResponse(Movie movie){
        MovieResponse movieRs = new MovieResponse();

        movieRs.setId(movie.getId());
        movieRs.setTitle(movie.getTitle());
        movieRs.setDescription(movie.getDescription());
        movieRs.setGenre(movie.getGenre());
        movieRs.setCast(movie.getCast());
        movieRs.setDirector(movie.getDirector());
        movieRs.setRating(movie.getRating());
        movieRs.setReleaseYear(movie.getReleaseYear());
        movieRs.setThumbnailUrl(movie.getThumbnailUrl());
        movieRs.setDuration(movie.getDuration());
        movieRs.setCreateAt(movie.getCreateAt());
        movieRs.setHlsURL(movie.getHlsURL());

        return movieRs;

    }

    public List<MovieResponse> getAllMovies() {
        List<MovieResponse> list_movie = new ArrayList<>();

        List<Movie> listDB = contentRepository.findAll();

        for (Movie movie : listDB) {
            list_movie.add(mapToResponse(movie));
        }
        return list_movie;
    }

    public List<MovieResponse> getMovieByGenre( Genre genre){
        List<MovieResponse> list_movie = new ArrayList<>();

        List<Movie> listDB = contentRepository.findByGenre(genre);

        for (Movie movie : listDB) {
            list_movie.add(mapToResponse(movie));
        }
        return list_movie;
    }

    public MovieResponse getMovieById( String id){


        Movie movie = contentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("movies not found")) ;

        MovieResponse movieRs = mapToResponse(movie);
        
        return movieRs;
    }
    
    public List<MovieResponse> searchMovie( String title){
        List<MovieResponse> listMovieRs = new ArrayList<>();

        List<Movie> listDB = contentRepository.findByTitleContainingIgnoreCase(title);

        for (Movie movie : listDB) {
            listMovieRs.add(mapToResponse(movie));
        }

        return listMovieRs;
    }

    public void updateVideoKey(String id,String videoKey){
        log.info("updating video key {}",videoKey);
        Movie movie = contentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("movies not found")) ;

        movie.setVideoKey(videoKey);
        movie.setVideoStatus(VideoStatus.UPLOADED);

        contentRepository.save(movie);
    }

    public void updateHls(String id, String hls){
        log.info("updating video hls {}",hls);
        Movie movie = contentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("movies not found")) ;

        movie.setHlsURL(hls);
        movie.setVideoStatus(VideoStatus.READY);

        contentRepository.save(movie);
    }

}
