package com.peliculas.recomendador.algorithm;

import com.peliculas.recomendador.model.Pelicula;
import com.peliculas.recomendador.model.Genero;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * BACKTRACKING - Búsqueda con Retroceso (1 PUNTO)
 * Encuentra combinaciones de películas que cumplan restricciones
 */
@Component
public class AlgoritmoBacktracking {
    
    /**
     * Encuentra todas las combinaciones de N películas de diferentes géneros
     */
    public List<List<Pelicula>> mixGeneros(List<Pelicula> peliculas, List<String> generosDeseados) {
        List<List<Pelicula>> resultados = new ArrayList<>();
        List<Pelicula> combinacionActual = new ArrayList<>();
        Set<String> generosUsados = new HashSet<>();
        
        backtrackMixGeneros(
            peliculas, 
            generosDeseados, 
            0, 
            combinacionActual, 
            generosUsados, 
            resultados
        );
        
        return resultados;
    }
    
    private void backtrackMixGeneros(
            List<Pelicula> peliculas,
            List<String> generosDeseados,
            int indicePelicula,
            List<Pelicula> combinacionActual,
            Set<String> generosUsados,
            List<List<Pelicula>> resultados) {
        
        // CASO BASE: Ya tenemos una película de cada género deseado
        if (generosUsados.size() == generosDeseados.size()) {
            resultados.add(new ArrayList<>(combinacionActual));
            return;
        }
        
        // CASO BASE: No hay más películas para explorar
        if (indicePelicula >= peliculas.size()) {
            return;
        }
        
        Pelicula peliculaActual = peliculas.get(indicePelicula);
        
        // OPCIÓN 1: Incluir esta película si tiene un género que necesitamos
        if (peliculaActual.getGeneros() != null) {
            for (Genero genero : peliculaActual.getGeneros()) {
                // Si el género está en la lista deseada y aún no lo hemos usado
                if (generosDeseados.contains(genero.getNombre()) && 
                    !generosUsados.contains(genero.getNombre())) {
                    
                    // Agregar película y marcar género como usado
                    combinacionActual.add(peliculaActual);
                    generosUsados.add(genero.getNombre());
                    
                    // RECURSIÓN: Buscar siguiente película
                    backtrackMixGeneros(
                        peliculas, 
                        generosDeseados, 
                        indicePelicula + 1, 
                        combinacionActual, 
                        generosUsados, 
                        resultados
                    );
                    
                    // BACKTRACK: Deshacer la elección
                    combinacionActual.remove(combinacionActual.size() - 1);
                    generosUsados.remove(genero.getNombre());
                }
            }
        }
        
        // OPCIÓN 2: NO incluir esta película (probar con la siguiente)
        backtrackMixGeneros(
            peliculas, 
            generosDeseados, 
            indicePelicula + 1, 
            combinacionActual, 
            generosUsados, 
            resultados
        );
    }
    
    /**
     * Encuentra combinaciones de películas que sumen exactamente N minutos
     */
    public List<List<Pelicula>> maratonTiempoExacto(List<Pelicula> peliculas, int tiempoObjetivo) {
        List<List<Pelicula>> resultados = new ArrayList<>();
        List<Pelicula> combinacionActual = new ArrayList<>();
        
        backtrackTiempoExacto(
            peliculas,
            tiempoObjetivo,
            0,
            0,
            combinacionActual,
            resultados
        );
        
        return resultados;
    }
    
    private void backtrackTiempoExacto(
            List<Pelicula> peliculas,
            int tiempoObjetivo,
            int tiempoAcumulado,
            int indicePelicula,
            List<Pelicula> combinacionActual,
            List<List<Pelicula>> resultados) {
        
        // CASO BASE: Encontramos una combinación que suma exactamente el tiempo
        if (tiempoAcumulado == tiempoObjetivo) {
            resultados.add(new ArrayList<>(combinacionActual));
            return;
        }
        
        // PODA: Si ya nos pasamos del tiempo, no seguir
        if (tiempoAcumulado > tiempoObjetivo || indicePelicula >= peliculas.size()) {
            return;
        }
        
        Pelicula peliculaActual = peliculas.get(indicePelicula);
        
        // OPCIÓN 1: Incluir esta película
        combinacionActual.add(peliculaActual);
        backtrackTiempoExacto(
            peliculas,
            tiempoObjetivo,
            tiempoAcumulado + peliculaActual.getDuracion(),
            indicePelicula + 1,
            combinacionActual,
            resultados
        );
        
        // BACKTRACK: Quitar la película
        combinacionActual.remove(combinacionActual.size() - 1);
        
        // OPCIÓN 2: NO incluir esta película
        backtrackTiempoExacto(
            peliculas,
            tiempoObjetivo,
            tiempoAcumulado,
            indicePelicula + 1,
            combinacionActual,
            resultados
        );
    }
    
    /**
     * Encuentra todas las combinaciones posibles de N películas
     */
    public List<List<Pelicula>> todasLasCombinaciones(List<Pelicula> peliculas, int cantidadPeliculas) {
        List<List<Pelicula>> resultados = new ArrayList<>();
        List<Pelicula> combinacionActual = new ArrayList<>();
        
        backtrackCombinaciones(
            peliculas,
            cantidadPeliculas,
            0,
            combinacionActual,
            resultados
        );
        
        return resultados;
    }
    
    private void backtrackCombinaciones(
            List<Pelicula> peliculas,
            int cantidadPeliculas,
            int indicePelicula,
            List<Pelicula> combinacionActual,
            List<List<Pelicula>> resultados) {
        
        // CASO BASE: Ya tenemos la cantidad deseada de películas
        if (combinacionActual.size() == cantidadPeliculas) {
            resultados.add(new ArrayList<>(combinacionActual));
            return;
        }
        
        // Explorar las películas restantes
        for (int i = indicePelicula; i < peliculas.size(); i++) {
            // Incluir la película i
            combinacionActual.add(peliculas.get(i));
            
            // RECURSIÓN: Buscar más películas
            backtrackCombinaciones(
                peliculas,
                cantidadPeliculas,
                i + 1,
                combinacionActual,
                resultados
            );
            
            // BACKTRACK: Quitar la película
            combinacionActual.remove(combinacionActual.size() - 1);
        }
    }
}