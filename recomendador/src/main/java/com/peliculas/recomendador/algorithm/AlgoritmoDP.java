package com.peliculas.recomendador.algorithm;

import com.peliculas.recomendador.model.Pelicula;
import com.peliculas.recomendador.model.ResultadoDP;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * PROGRAMACIÓN DINÁMICA - Problema de la Mochila
 * Selecciona el mejor maratón de películas maximizando rating dentro de un tiempo límite
 */
@Component
public class AlgoritmoDP {
    
    /**
     * Maratón óptima usando Programación Dinámica (Problema de la Mochila)
     * Maximiza la suma de ratings dentro de un tiempo máximo
     * 
     * @param peliculas Lista de películas disponibles
     * @param tiempoMaximo Tiempo máximo disponible en minutos
     * @return Resultado con la selección óptima
     */
    public ResultadoDP maratonOptima(List<Pelicula> peliculas, int tiempoMaximo) {
        if (peliculas == null || peliculas.isEmpty() || tiempoMaximo <= 0) {
            return new ResultadoDP(new ArrayList<>(), 0, 0.0);
        }
        
        int n = peliculas.size();
        
        // dp[i][t] = máximo rating usando películas 0..i-1 con tiempo t
        double[][] dp = new double[n + 1][tiempoMaximo + 1];
        
        // PASO 1: Llenar la tabla DP
        for (int i = 1; i <= n; i++) {
            Pelicula pelicula = peliculas.get(i - 1);
            int duracion = pelicula.getDuracion() != null ? pelicula.getDuracion() : 0;
            double rating = pelicula.getPromedioRating() != null ? pelicula.getPromedioRating() : 0.0;
            
            for (int t = 0; t <= tiempoMaximo; t++) {
                // Opción 1: NO incluir esta película
                dp[i][t] = dp[i - 1][t];
                
                // Opción 2: Incluir esta película (si cabe)
                if (duracion <= t) {
                    double valorConPelicula = dp[i - 1][t - duracion] + rating;
                    dp[i][t] = Math.max(dp[i][t], valorConPelicula);
                }
            }
        }
        
        // PASO 2: Reconstruir la solución (backtracking)
        List<Pelicula> peliculasSeleccionadas = new ArrayList<>();
        int tiempoRestante = tiempoMaximo;
        double puntuacionTotal = dp[n][tiempoMaximo];
        
        for (int i = n; i > 0 && puntuacionTotal > 0; i--) {
            // Si el valor cambió, significa que incluimos esta película
            if (dp[i][tiempoRestante] != dp[i - 1][tiempoRestante]) {
                Pelicula pelicula = peliculas.get(i - 1);
                peliculasSeleccionadas.add(pelicula);
                
                tiempoRestante -= pelicula.getDuracion();
                puntuacionTotal -= pelicula.getPromedioRating();
            }
        }
        
        // Invertir para obtener orden original
        Collections.reverse(peliculasSeleccionadas);
        
        int tiempoTotal = tiempoMaximo - tiempoRestante;
        double puntuacionFinal = dp[n][tiempoMaximo];
        
        return new ResultadoDP(peliculasSeleccionadas, tiempoTotal, puntuacionFinal);
    }
    
    /**
     * Versión alternativa: Maximizar cantidad de películas
     */
    public ResultadoDP maratonMaximaCantidad(List<Pelicula> peliculas, int tiempoMaximo) {
        if (peliculas == null || peliculas.isEmpty() || tiempoMaximo <= 0) {
            return new ResultadoDP(new ArrayList<>(), 0, 0.0);
        }
        
        // Ordenar por duración (más cortas primero)
        List<Pelicula> peliculasOrdenadas = new ArrayList<>(peliculas);
        peliculasOrdenadas.sort(Comparator.comparing(Pelicula::getDuracion));
        
        List<Pelicula> seleccionadas = new ArrayList<>();
        int tiempoAcumulado = 0;
        double puntuacionTotal = 0.0;
        
        for (Pelicula pelicula : peliculasOrdenadas) {
            if (tiempoAcumulado + pelicula.getDuracion() <= tiempoMaximo) {
                seleccionadas.add(pelicula);
                tiempoAcumulado += pelicula.getDuracion();
                puntuacionTotal += pelicula.getPromedioRating();
            }
        }
        
        ResultadoDP resultado = new ResultadoDP(seleccionadas, tiempoAcumulado, puntuacionTotal);
        resultado.setEstrategia("DP - Maximizar Cantidad");
        return resultado;
    }
    
    /**
     * Versión con restricción: Al menos N películas
     */
    public ResultadoDP maratonConMinimo(List<Pelicula> peliculas, int tiempoMaximo, int minimoePeliculas) {
        ResultadoDP resultado = maratonOptima(peliculas, tiempoMaximo);
        
        if (resultado.getPeliculasOptimas().size() < minimoePeliculas) {
            // Si no alcanza el mínimo, intentar con estrategia de cantidad
            resultado = maratonMaximaCantidad(peliculas, tiempoMaximo);
            resultado.setEstrategia("DP - Forzando mínimo de " + minimoePeliculas + " películas");
        }
        
        return resultado;
    }
}
