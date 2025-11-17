package com.peliculas.recomendador.algorithm;

import com.peliculas.recomendador.model.Arista;
import com.peliculas.recomendador.model.Pelicula;
import com.peliculas.recomendador.model.ResultadoMST;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * PRIM - Algoritmo de Árbol de Expansión Mínimo (MST)
 * Encuentra la red mínima de conexiones entre películas
 */
@Component
public class AlgoritmoPrim {
    
    /**
     * Implementación del algoritmo de Prim
     * Encuentra el MST a partir de un nodo inicial
     */
    public ResultadoMST arbolExpansionMinimo(List<Arista> todasLasAristas, Long peliculaInicioId) {
        if (todasLasAristas == null || todasLasAristas.isEmpty()) {
            return new ResultadoMST(new ArrayList<>(), "Prim");
        }
        
        List<Arista> mst = new ArrayList<>();
        Set<Long> visitados = new HashSet<>();
        PriorityQueue<Arista> colaPrioridad = new PriorityQueue<>();
        
        // PASO 1: Agregar el nodo inicial
        visitados.add(peliculaInicioId);
        
        // PASO 2: Agregar todas las aristas del nodo inicial a la cola
        for (Arista arista : todasLasAristas) {
            if (arista.getOrigen().getPeliculaId().equals(peliculaInicioId)) {
                colaPrioridad.offer(arista);
            }
        }
        
        // PASO 3: Algoritmo de Prim
        while (!colaPrioridad.isEmpty() && mst.size() < visitados.size()) {
            Arista aristaActual = colaPrioridad.poll();
            Long destinoId = aristaActual.getDestino().getPeliculaId();
            
            // Si el destino ya fue visitado, ignorar (evitar ciclos)
            if (visitados.contains(destinoId)) {
                continue;
            }
            
            // Agregar la arista al MST
            mst.add(aristaActual);
            visitados.add(destinoId);
            
            // Agregar todas las aristas del nuevo nodo a la cola
            for (Arista arista : todasLasAristas) {
                Long origenId = arista.getOrigen().getPeliculaId();
                Long destId = arista.getDestino().getPeliculaId();
                
                // Agregar aristas que salgan del nodo recién agregado
                if (origenId.equals(destinoId) && !visitados.contains(destId)) {
                    colaPrioridad.offer(arista);
                }
            }
        }
        
        ResultadoMST resultado = new ResultadoMST(mst, "Prim");
        resultado.setNumeroNodos(visitados.size());
        return resultado;
    }
    
    /**
     * Versión que acepta películas y construye las aristas automáticamente
     */
    public ResultadoMST arbolExpansionMinimoDesdeGrafo(List<Pelicula> peliculas) {
        if (peliculas == null || peliculas.isEmpty()) {
            return new ResultadoMST(new ArrayList<>(), "Prim");
        }
        
        // Construir lista de aristas desde las relaciones de similitud
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
        
        // Ejecutar Prim desde la primera película
        return arbolExpansionMinimo(aristas, peliculas.get(0).getPeliculaId());
    }
}
