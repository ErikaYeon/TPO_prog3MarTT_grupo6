package com.peliculas.recomendador.algorithm;

import com.peliculas.recomendador.model.Arista;
import com.peliculas.recomendador.model.Pelicula;
import com.peliculas.recomendador.model.ResultadoMST;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * PRIM - Algoritmo de Árbol de Expansión Mínimo (MST)
 * Encuentra la red mínima de conexiones entre películas
 * 
 * COMPLEJIDAD TEMPORAL: O((V + E) log V)
 * 
 * Implementación con PriorityQueue (heap binario)
 */
@Component
public class AlgoritmoPrim {
    
    /**
     * Algoritmo de Prim desde un nodo inicial
     * COMPLEJIDAD: O((V + E) log V)
     */
    public ResultadoMST arbolExpansionMinimo(List<Arista> todasLasAristas, Long peliculaInicioId) {
        if (todasLasAristas == null || todasLasAristas.isEmpty()) {
            return new ResultadoMST(new ArrayList<>(), "Prim");
        }
        
        List<Arista> mst = new ArrayList<>();
        Set<Long> visitados = new HashSet<>();
        PriorityQueue<Arista> colaPrioridad = new PriorityQueue<>();  // Min-heap por peso
        
        // ========================================
        // PASO 1: INICIALIZACIÓN - O(E)
        // ========================================
        visitados.add(peliculaInicioId);  // O(1)
        
        // O(E): Buscar todas las aristas del nodo inicial
        for (Arista arista : todasLasAristas) {
            if (arista.getOrigen().getPeliculaId().equals(peliculaInicioId)) {
                colaPrioridad.offer(arista);  // O(log E)
            }
        }
        
        // ========================================
        // PASO 2: ALGORITMO DE PRIM - O((V + E) log V)
        // ========================================
        while (!colaPrioridad.isEmpty() && mst.size() < visitados.size()) {  // O(V) iteraciones
            Arista aristaActual = colaPrioridad.poll();  // O(log E)
            Long destinoId = aristaActual.getDestino().getPeliculaId();
            
            // Evitar ciclos
            if (visitados.contains(destinoId)) {  // O(1)
                continue;
            }
            
            // Agregar arista al MST
            mst.add(aristaActual);
            visitados.add(destinoId);
            
            // Agregar aristas del nodo recién agregado a la cola
            for (Arista arista : todasLasAristas) {
                Long origenId = arista.getOrigen().getPeliculaId();
                Long destId = arista.getDestino().getPeliculaId();
                
                // Agregar aristas que salgan del nodo recién agregado
                if (origenId.equals(destinoId) && !visitados.contains(destId)) {
                    colaPrioridad.offer(arista);  // O(log E)
                }
            }
        }
        
        ResultadoMST resultado = new ResultadoMST(mst, "Prim");
        resultado.setNumeroNodos(visitados.size());
        return resultado;
    }
    
    /**
     * Wrapper que construye las aristas desde las películas
     * COMPLEJIDAD: O((V + E) log V)
     */
    public ResultadoMST arbolExpansionMinimoDesdeGrafo(List<Pelicula> peliculas) {
        if (peliculas == null || peliculas.isEmpty()) {
            return new ResultadoMST(new ArrayList<>(), "Prim");
        }
        
        // O(V × grado promedio): Construir lista de aristas
        List<Arista> aristas = new ArrayList<>();
        
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getPeliculasSimilares() != null) {
                for (var relacion : pelicula.getPeliculasSimilares()) {
                    Arista arista = new Arista(
                        pelicula,
                        relacion.getPeliculaDestino(),
                        relacion.getPeso(),
                        relacion.getGenerosComunes()
                    );
                    aristas.add(arista);
                }
            }
        }
        
        if (aristas.isEmpty()) {
            return new ResultadoMST(new ArrayList<>(), "Prim");
        }
        
        // O((V + E) log V): Ejecutar Prim
        return arbolExpansionMinimo(aristas, peliculas.get(0).getPeliculaId());
    }
}
