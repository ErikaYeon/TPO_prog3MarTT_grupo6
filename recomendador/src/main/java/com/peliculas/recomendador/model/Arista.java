package com.peliculas.recomendador.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una arista (conexión) entre dos películas con un peso
 * Usado para algoritmos Prim y Kruskal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Arista implements Comparable<Arista> {
    
    private Pelicula origen;
    private Pelicula destino;
    private Double peso;
    private Integer generosComunes;
    
    @Override
    public int compareTo(Arista otra) {
        // Ordenar por peso (menor peso primero para MST)
        return this.peso.compareTo(otra.peso);
    }
    
    @Override
    public String toString() {
        return String.format("%s -[%.2f]-> %s", 
            origen.getTitulo(), 
            peso, 
            destino.getTitulo()
        );
    }
}
