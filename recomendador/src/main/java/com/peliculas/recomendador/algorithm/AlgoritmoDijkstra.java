package com.peliculas.recomendador.algorithm;

import com.peliculas.recomendador.model.Pelicula;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * DIJKSTRA - Algoritmo de Camino Más Corto
 * Encuentra el camino con menor distancia (peso) entre dos nodos
 * 
 * COMPLEJIDAD TEMPORAL: O((V + E) log V)
 * 
 * Implementación con PriorityQueue (heap binario)
 */
@Component
public class AlgoritmoDijkstra {
    
    /**
     * Nodo interno para la cola de prioridad
     * Comparable permite que PriorityQueue ordene por distancia
     */
    private static class NodoGrafo implements Comparable<NodoGrafo> {
        Long peliculaId;
        Double distancia;
        Pelicula pelicula;
        
        NodoGrafo(Long peliculaId, Pelicula pelicula, Double distancia) {
            this.peliculaId = peliculaId;
            this.pelicula = pelicula;
            this.distancia = distancia;
        }
        
        @Override
        public int compareTo(NodoGrafo otro) {
            return this.distancia.compareTo(otro.distancia); // Min-heap
        }
    }
    
    /**
     * Dijkstra: Encuentra el camino más corto entre dos películas
     * 
     * COMPLEJIDAD: O((V + E) log V)
     * - V = número de películas (vértices)
     * - E = número de relaciones (aristas)
     */
    public List<Pelicula> caminoMasCorto(List<Pelicula> todasLasPeliculas, 
                                          Long peliculaInicio, Long peliculaFin) {
        if (todasLasPeliculas == null || todasLasPeliculas.isEmpty()) {
            return new ArrayList<>();
        }
        
        // O(V): Construir HashMap para acceso O(1) por ID
        Map<Long, Pelicula> mapaPeliculas = new HashMap<>();
        for (Pelicula pelicula : todasLasPeliculas) {
            mapaPeliculas.put(pelicula.getPeliculaId(), pelicula);
        }
        
        if (!mapaPeliculas.containsKey(peliculaInicio) || !mapaPeliculas.containsKey(peliculaFin)) {
            return new ArrayList<>();
        }
        
        // ========================================
        // PASO 1: INICIALIZACIÓN - O(V)
        // ========================================
        Map<Long, Double> distancias = new HashMap<>();      // Distancia mínima a cada nodo
        Map<Long, Long> padres = new HashMap<>();            // Para reconstruir camino
        Set<Long> visitados = new HashSet<>();               // Nodos ya procesados
        PriorityQueue<NodoGrafo> cola = new PriorityQueue<>(); // Min-heap por distancia
        
        // O(V): Inicializar todas las distancias a infinito
        for (Long peliculaId : mapaPeliculas.keySet()) {
            distancias.put(peliculaId, Double.MAX_VALUE);
        }
        
        // O(1): Distancia del nodo inicial = 0
        distancias.put(peliculaInicio, 0.0);
        cola.offer(new NodoGrafo(peliculaInicio, mapaPeliculas.get(peliculaInicio), 0.0)); // O(log V)
        
        // ========================================
        // PASO 2: ALGORITMO PRINCIPAL - O((V + E) log V)
        // ========================================
        while (!cola.isEmpty()) {  // O(V) iteraciones
            NodoGrafo nodoActual = cola.poll();  // O(log V) - sacar mínimo del heap
            Long idActual = nodoActual.peliculaId;
            
            // O(1): Evitar procesar nodos duplicados en la cola
            if (visitados.contains(idActual)) {
                continue;
            }
            
            visitados.add(idActual);  // O(1)
            
            // Optimización: terminar si alcanzamos el destino
            if (idActual.equals(peliculaFin)) {
                break;
            }
            
            Pelicula peliculaActual = mapaPeliculas.get(idActual);
            if (peliculaActual == null || peliculaActual.getPeliculasSimilares() == null) {
                continue;
            }
            
            // ========================================
            // PASO 3: RELAJACIÓN - O(E log V) total
            // ========================================
            for (var relacion : peliculaActual.getPeliculasSimilares()) {  // O(grado del nodo)
                Long idVecino = relacion.getPeliculaDestino().getPeliculaId();
                
                if (visitados.contains(idVecino)) {  // O(1)
                    continue;
                }
                
                // Calcular peso: menor similitud = mayor distancia
                Double peso = 1.0 / (relacion.getPeso() + 0.1);
                
                Double distanciaActual = distancias.get(idActual);
                Double distanciaVecino = distancias.get(idVecino);
                Double nuevaDistancia = distanciaActual + peso;
                
                // RELAJACIÓN: Si encontramos un camino más corto
                if (nuevaDistancia < distanciaVecino) {  // O(1)
                    distancias.put(idVecino, nuevaDistancia);  // O(1)
                    padres.put(idVecino, idActual);  // O(1)
                    
                    cola.offer(new NodoGrafo(idVecino, mapaPeliculas.get(idVecino), nuevaDistancia));  // O(log V)
                }
            }
        }
        
        // ========================================
        // PASO 4: RECONSTRUIR CAMINO - O(V)
        // ========================================
        return reconstruirCamino(padres, peliculaInicio, peliculaFin, mapaPeliculas, distancias);
    }
    
    /**
     * Reconstruye el camino siguiendo los padres desde fin hacia inicio
     * COMPLEJIDAD: O(V) en el peor caso (camino atraviesa todos los nodos)
     */
    private List<Pelicula> reconstruirCamino(Map<Long, Long> padres,
                                             Long inicio,
                                             Long fin,
                                             Map<Long, Pelicula> mapaPeliculas,
                                             Map<Long, Double> distancias) {
        List<Pelicula> camino = new ArrayList<>();
        
        if (distancias.get(fin) == Double.MAX_VALUE) {
            return camino; // No existe camino
        }
        
        // O(longitud del camino): Reconstruir desde fin hacia inicio
        Long actual = fin;
        while (actual != null) {
            camino.add(0, mapaPeliculas.get(actual));  // O(1) agregar al inicio
            actual = padres.get(actual);  // O(1) obtener padre
        }
        
        return camino;
    }
    
    /**
     * Dijkstra completo: Calcula distancias a TODOS los nodos desde un origen
     * COMPLEJIDAD: O((V + E) log V)
     */
    public Map<Long, Double> obtenerDistancias(List<Pelicula> todasLasPeliculas,
                                               Long peliculaOrigen) {
        if (todasLasPeliculas == null || todasLasPeliculas.isEmpty()) {
            return new HashMap<>();
        }
        
        // Construir mapa de películas
        Map<Long, Pelicula> mapaPeliculas = new HashMap<>();
        for (Pelicula pelicula : todasLasPeliculas) {
            mapaPeliculas.put(pelicula.getPeliculaId(), pelicula);
        }
        
        if (!mapaPeliculas.containsKey(peliculaOrigen)) {
            return new HashMap<>();
        }
        
        // Inicializar distancias
        Map<Long, Double> distancias = new HashMap<>();
        Set<Long> visitados = new HashSet<>();
        PriorityQueue<NodoGrafo> cola = new PriorityQueue<>();
        
        for (Long peliculaId : mapaPeliculas.keySet()) {
            distancias.put(peliculaId, Double.MAX_VALUE);
        }
        
        distancias.put(peliculaOrigen, 0.0);
        cola.offer(new NodoGrafo(peliculaOrigen, mapaPeliculas.get(peliculaOrigen), 0.0));
        
        // O((V + E) log V): Mismo algoritmo, pero sin terminar early
        while (!cola.isEmpty()) {
            NodoGrafo nodoActual = cola.poll();  // O(log V)
            Long idActual = nodoActual.peliculaId;
            
            if (visitados.contains(idActual)) {
                continue;
            }
            
            visitados.add(idActual);
            
            Pelicula peliculaActual = mapaPeliculas.get(idActual);
            if (peliculaActual == null || peliculaActual.getPeliculasSimilares() == null) {
                continue;
            }
            
            for (var relacion : peliculaActual.getPeliculasSimilares()) {
                Long idVecino = relacion.getPeliculaDestino().getPeliculaId();
                
                if (visitados.contains(idVecino)) {
                    continue;
                }
                
                Double peso = 1.0 / (relacion.getPeso() + 0.1);
                Double distanciaActual = distancias.get(idActual);
                Double distanciaVecino = distancias.get(idVecino);
                Double nuevaDistancia = distanciaActual + peso;
                
                if (nuevaDistancia < distanciaVecino) {
                    distancias.put(idVecino, nuevaDistancia);
                    cola.offer(new NodoGrafo(idVecino, mapaPeliculas.get(idVecino), nuevaDistancia));  // O(log V)
                }
            }
        }
        
        return distancias;
    }
    
    /**
     * Top N películas más cercanas usando Dijkstra
     * COMPLEJIDAD: O((V + E) log V + V log V) = O((V + E) log V)
     */
    public List<Pelicula> topNCercanas(List<Pelicula> todasLasPeliculas,
                                        Long peliculaOrigen,
                                        int n) {
        // O((V + E) log V): Calcular todas las distancias
        Map<Long, Double> distancias = obtenerDistancias(todasLasPeliculas, peliculaOrigen);
        
        // O(V): Construir mapa de películas
        Map<Long, Pelicula> mapaPeliculas = new HashMap<>();
        for (Pelicula pelicula : todasLasPeliculas) {
            mapaPeliculas.put(pelicula.getPeliculaId(), pelicula);
        }
        
        // O(V log V): Ordenar y tomar top N
        return distancias.entrySet().stream()
            .filter(e -> !e.getKey().equals(peliculaOrigen) && e.getValue() < Double.MAX_VALUE)
            .sorted(Map.Entry.comparingByValue())  // O(V log V)
            .limit(n)
            .map(e -> mapaPeliculas.get(e.getKey()))
            .toList();
    }
}
