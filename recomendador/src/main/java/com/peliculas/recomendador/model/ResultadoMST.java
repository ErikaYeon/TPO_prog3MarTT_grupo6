package com.peliculas.recomendador.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Resultado de los algoritmos Prim/Kruskal
 * Contiene el Árbol de Expansión Mínimo (MST)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoMST {
    
    private List<Arista> aristas;
    private Double pesoTotal;
    private Integer numeroNodos;
    private Integer numeroAristas;
    private String algoritmo; // "Prim" o "Kruskal"
    
    public ResultadoMST(List<Arista> aristas, String algoritmo) {
        this.aristas = aristas;
        this.algoritmo = algoritmo;
        this.numeroAristas = aristas.size();
        this.pesoTotal = aristas.stream()
            .mapToDouble(Arista::getPeso)
            .sum();
    }
}
