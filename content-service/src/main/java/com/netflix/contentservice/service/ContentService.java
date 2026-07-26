package com.netflix.contentservice.service;

import org.springframework.stereotype.Service;

import com.netflix.contentservice.dto.MovieRequest;
import com.netflix.contentservice.dto.MovieResponse;
import com.netflix.contentservice.repository.ContentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    public MovieResponse addMovie(MovieRequest movieRequest) {
        // Aquí irá tu lógica para guardar en la base de datos
        return null; 
    }
}
