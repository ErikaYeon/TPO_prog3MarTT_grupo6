package com.peliculas.recomendador.algorithm;

import com.peliculas.recomendador.model.Pelicula;
import com.peliculas.recomendador.model.Genero;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * GREEDY - Algoritmo Voraz (1 PUNTO)
 * Estrategia: Recomendar películas del género más frecuente con mejor rating
 */
@Component
public class AlgoritmoGreedy {
    
    /**
     * Encuentra el género más frecuente en una lista de películas
     * y recomienda la película con mejor rating de ese género
     */
    public Pelicula recomendacionPorGeneroMasFrecuente(List<Pelicula> peliculas) {
        if (peliculas == null || peliculas.isEmpty()) {
            return null;
        }
        
        // PASO 1: Contar frecuencia de cada género (GREEDY: elegir el más frecuente)
        Map<String, Integer> frecuenciaGeneros = new HashMap<>();
        
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getGeneros() != null) {
                for (Genero genero : pelicula.getGeneros()) {
                    frecuenciaGeneros.put(
                        genero.getNombre(),
                        frecuenciaGeneros.getOrDefault(genero.getNombre(), 0) + 1
                    );
                }
            }
        }
        
        // PASO 2: Encontrar el género más frecuente
        String generoMasFrecuente = frecuenciaGeneros.entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
        
        if (generoMasFrecuente == null) {
            return peliculas.get(0);
        }
        
        // PASO 3: GREEDY - Elegir la película con MEJOR RATING de ese género
        return peliculas.stream()
            .filter(p -> p.getGeneros() != null && 
                        p.getGeneros().stream()
                            .anyMatch(g -> g.getNombre().equals(generoMasFrecuente)))
            .max(Comparator.comparing(Pelicula::getPromedioRating))
            .orElse(peliculas.get(0));
    }
    
    /**
     * Recomendación GREEDY: Top N películas con mejor rating
     */
    public List<Pelicula> topNMejoresRatings(List<Pelicula> peliculas, int n) {
        return peliculas.stream()
            .sorted(Comparator.comparing(Pelicula::getPromedioRating).reversed())
            .limit(n)
            .collect(Collectors.toList());
    }
    
    /**
     * Recomendación GREEDY por duración
     * Selecciona películas que maximicen rating dentro de un tiempo límite
     */
    public List<Pelicula> maratonGreedy(List<Pelicula> peliculas, int tiempoMaximoMinutos) {
        List<Pelicula> resultado = new ArrayList<>();
        int tiempoAcumulado = 0;
        
        // GREEDY: Ordenar por rating descendente
        List<Pelicula> ordenadas = peliculas.stream()
            .sorted(Comparator.comparing(Pelicula::getPromedioRating).reversed())
            .collect(Collectors.toList());
        
        // GREEDY: Tomar películas mientras quepan en el tiempo
        for (Pelicula pelicula : ordenadas) {
            if (tiempoAcumulado + pelicula.getDuracion() <= tiempoMaximoMinutos) {
                resultado.add(pelicula);
                tiempoAcumulado += pelicula.getDuracion();
            }
        }
        
        return resultado;
    }
}