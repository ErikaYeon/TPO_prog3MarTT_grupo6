package com.peliculas.recomendador.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Node("Actor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // ✅ SOLUCIÓN: Solo usar campos específicos
public class Actor {
    
    @Id
    @EqualsAndHashCode.Include // ✅ Solo incluir el nombre en hashCode/equals
    private String nombre;

    @JsonIgnore 
    @Relationship(type = "ACTUA_EN", direction = Relationship.Direction.OUTGOING)
    private Set<Pelicula> peliculas = new HashSet<>();
    
    public Actor(String nombre) {
        this.nombre = nombre;
    }
}