package com.peliculas.recomendador.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Resultado del algoritmo de Programación Dinámica
 * Contiene la selección óptima de películas para un maratón
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoDP {
    
    private List<Pelicula> peliculasOptimas;
    private Integer tiempoTotal;
    private Double puntuacionTotal;
    private Double ratioEficiencia; // Puntuación / tiempo
    private String estrategia; // Descripción de la estrategia usada
    
    public ResultadoDP(List<Pelicula> peliculasOptimas, Integer tiempoTotal, Double puntuacionTotal) {
        this.peliculasOptimas = peliculasOptimas;
        this.tiempoTotal = tiempoTotal;
        this.puntuacionTotal = puntuacionTotal;
        this.ratioEficiencia = tiempoTotal > 0 ? puntuacionTotal / tiempoTotal : 0.0;
        this.estrategia = "Programación Dinámica - Mochila";
    }
}
