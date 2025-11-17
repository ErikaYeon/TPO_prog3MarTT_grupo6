package com.peliculas.recomendador.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Resultado del algoritmo Branch & Bound
 * Contiene la mejor selección encontrada
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoBB {
    
    private List<Pelicula> peliculasOptimas;
    private Integer tiempoTotal;
    private Double puntuacionTotal;
    private Double ratioEficiencia;
    private String estrategia;
    private Integer nodosExplorados;
    private Integer nodosPodados;
    
    public ResultadoBB(List<Pelicula> peliculasOptimas, Integer tiempoTotal, Double puntuacionTotal, 
                       Integer nodosExplorados, Integer nodosPodados) {
        this.peliculasOptimas = peliculasOptimas;
        this.tiempoTotal = tiempoTotal;
        this.puntuacionTotal = puntuacionTotal;
        this.ratioEficiencia = tiempoTotal > 0 ? puntuacionTotal / tiempoTotal : 0.0;
        this.estrategia = "Branch & Bound - Optimización con Poda";
        this.nodosExplorados = nodosExplorados;
        this.nodosPodados = nodosPodados;
    }
}
