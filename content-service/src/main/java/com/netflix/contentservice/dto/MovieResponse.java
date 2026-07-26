package com.netflix.contentservice.dto;

import java.time.LocalDateTime;
import com.netflix.contentservice.model.Genre;
import com.netflix.contentservice.model.VideoStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {
    
    private String id;
    private String title;
    private String description;
    private Genre genre;
    private String director;
    private String cast;
    private int releaseYear;
    private Float rating;
    private String thumbnailUrl;
    private int duration;
    private VideoStatus videoStatus;
    private String hlsURL;
    private LocalDateTime createAt;
    
}
