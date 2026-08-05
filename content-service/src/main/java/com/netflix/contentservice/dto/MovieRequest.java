package com.netflix.contentservice.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.netflix.contentservice.model.Genre;
import com.netflix.contentservice.model.VideoStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest {
    
    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotNull(message = "El género es obligatorio")
    private Genre genre;

    private String director;

    private String cast;

    @Positive(message = "El año de lanzamiento debe ser un número positivo")
    private int releaseYear;

    @Positive(message = "La duración debe ser un número positivo")
    private int duration;

    private String thumbnailUrl;


    
    
}
