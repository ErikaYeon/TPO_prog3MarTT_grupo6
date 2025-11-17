package com.peliculas.recomendador.algorithm;

import com.peliculas.recomendador.model.Pelicula;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * DIJKSTRA - Algoritmo de Camino Más Corto (3 PUNTOS)
 * Encuentra el camino con menor distancia (peso) entre dos nodos
 * Implementación manual en Java con Cola de Prioridad
 */
@Component
public class AlgoritmoDijkstra {
    
    /**
     * Nodo interno para el algoritmo de Dijkstra
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
            return this.distancia.compareTo(otro.distancia);
        }
    }
    
    /**
     * Dijkstra: Encuentra el camino más corto entre dos películas
     * 
     * @param todasLasPeliculas Lista de todas las películas (nodos)
     * @param peliculaInicio ID de la película de inicio
     * @param peliculaFin ID de la película de fin
     * @return Lista de películas que forman el camino más corto
     */
    public List<Pelicula> caminoMasCorto(List<Pelicula> todasLasPeliculas, 
                                          Long peliculaInicio, Long peliculaFin) {
        if (todasLasPeliculas == null || todasLasPeliculas.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Construir mapa de películas por ID para acceso rápido
        Map<Long, Pelicula> mapaPeliculas = new HashMap<>();
        for (Pelicula pelicula : todasLasPeliculas) {
            mapaPeliculas.put(pelicula.getPeliculaId(), pelicula);
        }
        
        // Verificar que existan las películas
        if (!mapaPeliculas.containsKey(peliculaInicio) || !mapaPeliculas.containsKey(peliculaFin)) {
            return new ArrayList<>();
        }
        
        // PASO 1: Inicializar distancias
        Map<Long, Double> distancias = new HashMap<>();
        Map<Long, Long> padres = new HashMap<>();
        Set<Long> visitados = new HashSet<>();
        PriorityQueue<NodoGrafo> cola = new PriorityQueue<>();
        
        // Todas las distancias comienzan en infinito
        for (Long peliculaId : mapaPeliculas.keySet()) {
            distancias.put(peliculaId, Double.MAX_VALUE);
        }
        
        // Distancia a sí mismo es 0
        distancias.put(peliculaInicio, 0.0);
        cola.offer(new NodoGrafo(peliculaInicio, mapaPeliculas.get(peliculaInicio), 0.0));
        
        // PASO 2: Algoritmo de Dijkstra
        while (!cola.isEmpty()) {
            NodoGrafo nodoActual = cola.poll();
            Long idActual = nodoActual.peliculaId;
            
            // Si ya fue visitado, saltar
            if (visitados.contains(idActual)) {
                continue;
            }
            
            visitados.add(idActual);
            
            // Si llegamos al destino, podemos parar
            if (idActual.equals(peliculaFin)) {
                break;
            }
            
            // Obtener película actual
            Pelicula peliculaActual = mapaPeliculas.get(idActual);
            if (peliculaActual == null || peliculaActual.getPeliculasSimilares() == null) {
                continue;
            }
            
            // PASO 3: Relajación de aristas
            for (var relacion : peliculaActual.getPeliculasSimilares()) {
                Long idVecino = relacion.getPeliculaDestino().getPeliculaId();
                
                // Saltar si ya fue visitado
                if (visitados.contains(idVecino)) {
                    continue;
                }
                
                // El peso es la inversa de la similitud
                // (menor peso = más similar)
                Double peso = 1.0 / (relacion.getPeso() + 0.1); // +0.1 para evitar división por cero
                
                Double distanciaActual = distancias.get(idActual);
                Double distanciaVecino = distancias.get(idVecino);
                Double nuevaDistancia = distanciaActual + peso;
                
                // Si encontramos un camino más corto
                if (nuevaDistancia < distanciaVecino) {
                    distancias.put(idVecino, nuevaDistancia);
                    padres.put(idVecino, idActual);
                    
                    cola.offer(new NodoGrafo(
                        idVecino,
                        mapaPeliculas.get(idVecino),
                        nuevaDistancia
                    ));
                }
            }
        }
        
        // PASO 4: Reconstruir el camino
        return reconstruirCamino(padres, peliculaInicio, peliculaFin, mapaPeliculas, distancias);
    }
    
    /**
     * Reconstruye el camino desde el inicio hasta el fin
     */
    private List<Pelicula> reconstruirCamino(Map<Long, Long> padres,
                                             Long inicio,
                                             Long fin,
                                             Map<Long, Pelicula> mapaPeliculas,
                                             Map<Long, Double> distancias) {
        List<Pelicula> camino = new ArrayList<>();
        
        // Si no hay camino
        if (distancias.get(fin) == Double.MAX_VALUE) {
            return camino;
        }
        
        // Reconstruir desde fin hacia inicio
        Long actual = fin;
        while (actual != null) {
            camino.add(0, mapaPeliculas.get(actual));
            actual = padres.get(actual);
        }
        
        return camino;
    }
    
    /**
     * Dijkstra: Obtiene información de distancias desde un nodo origen
     * Útil para ver qué tan lejos está cada película
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
        
        // Dijkstra completo
        while (!cola.isEmpty()) {
            NodoGrafo nodoActual = cola.poll();
            Long idActual = nodoActual.peliculaId;
            
            if (visitados.contains(idActual)) {
                continue;
            }
            
            visitados.add(idActual);
            
            Pelicula peliculaActual = mapaPeliculas.get(idActual);
            if (peliculaActual == null || peliculaActual.getPeliculasSimilares() == null) {
                continue;
            }
            
            // Relajación
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
                    cola.offer(new NodoGrafo(idVecino, mapaPeliculas.get(idVecino), nuevaDistancia));
                }
            }
        }
        
        return distancias;
    }
    
    /**
     * Dijkstra: Top N películas más cercanas
     */
    public List<Pelicula> topNCercanas(List<Pelicula> todasLasPeliculas,
                                        Long peliculaOrigen,
                                        int n) {
        Map<Long, Double> distancias = obtenerDistancias(todasLasPeliculas, peliculaOrigen);
        
        // Construir mapa de películas
        Map<Long, Pelicula> mapaPeliculas = new HashMap<>();
        for (Pelicula pelicula : todasLasPeliculas) {
            mapaPeliculas.put(pelicula.getPeliculaId(), pelicula);
        }
        
        // Ordenar por distancia
        return distancias.entrySet().stream()
            .filter(e -> !e.getKey().equals(peliculaOrigen) && e.getValue() < Double.MAX_VALUE)
            .sorted(Map.Entry.comparingByValue())
            .limit(n)
            .map(e -> mapaPeliculas.get(e.getKey()))
            .toList();
    }
}
