package com.peliculas.recomendador.model;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@RelationshipProperties
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelacionSimilitud {
    
    @RelationshipId
    private Long id;
    
    private Double peso;
    private Integer generosComunes;
    
    @TargetNode
    private Pelicula peliculaDestino;
    
    public RelacionSimilitud(Double peso, Integer generosComunes, Pelicula peliculaDestino) {
        this.peso = peso;
        this.generosComunes = generosComunes;
        this.peliculaDestino = peliculaDestino;
    }
}
