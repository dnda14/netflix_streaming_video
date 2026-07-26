package com.netflix.contentservice.model;

import java.time.LocalDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private String director;

    @Column(length = 1000)
    private String cast;

    @Column(name = "release_year")
    private int releaseYear;

    private Float rating;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    private int duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "video_status")
    private VideoStatus videoStatus;
 
    @Column(unique = true)
    private String videoKey;

    @Column(name = "hls_url")
    private String hlsURL;

    
    @CreationTimestamp
    @Column(name="create_at", updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt;





    
}
