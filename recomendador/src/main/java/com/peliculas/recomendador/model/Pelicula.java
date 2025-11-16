package com.peliculas.recomendador.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Node("Pelicula")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // ✅ SOLUCIÓN: Solo usar campos específicos
public class Pelicula {
    
    @Id
    @EqualsAndHashCode.Include // ✅ Solo incluir el ID en hashCode/equals
    private Long peliculaId;
    
    private String titulo;
    private Integer año;
    private Double promedioRating;
    private Integer duracion;
    
    @Relationship(type = "TIENE_GENERO", direction = Relationship.Direction.OUTGOING)
    private Set<Genero> generos = new HashSet<>();

    @Relationship(type = "ACTUA_EN", direction = Relationship.Direction.INCOMING)
    private Set<Actor> actores = new HashSet<>();
    
    @Relationship(type = "SIMILAR_A", direction = Relationship.Direction.OUTGOING)
    private Set<RelacionSimilitud> peliculasSimilares = new HashSet<>();
    
    public Pelicula(Long peliculaId, String titulo, Integer año, Double promedioRating, Integer duracion) {
        this.peliculaId = peliculaId;
        this.titulo = titulo;
        this.año = año;
        this.promedioRating = promedioRating;
        this.duracion = duracion;
    }
}