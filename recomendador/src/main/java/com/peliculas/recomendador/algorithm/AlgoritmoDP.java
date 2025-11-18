package com.peliculas.recomendador.algorithm;

import com.peliculas.recomendador.model.Pelicula;
import com.peliculas.recomendador.model.ResultadoDP;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * PROGRAMACIÓN DINÁMICA - Problema de la Mochila (0/1 Knapsack)
 * Selecciona el mejor maratón de películas maximizando rating dentro de un tiempo límite
 * 
 * COMPLEJIDAD TEMPORAL: O(n × W)
 * - n = número de películas
 * - W = capacidad (tiempo máximo en minutos)
 * 
 * Es PSEUDO-POLINOMIAL: depende del VALOR de W, no de su tamaño en bits
 * 
 * PROPIEDADES DE DP:
 * 1. Subestructura óptima: solución óptima contiene soluciones óptimas
 * 2. Superposición de subproblemas: reutilizamos resultados guardados
 */
@Component
public class AlgoritmoDP {
    
    /**
     * Maratón óptima usando Programación Dinámica (Problema de la Mochila)
     * Maximiza la suma de ratings dentro de un tiempo máximo
     * 
     * COMPLEJIDAD: O(n × W)
     * - n = películas, W = tiempo máximo
     * - Espacio: O(n × W) para la tabla dp
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
        
        // ========================================
        // PASO 1: CREAR TABLA DE MEMOIZACIÓN - O(1) tiempo, O(n × W) espacio
        // ========================================
        // dp[i][t] = máximo rating usando películas 0..i-1 con tiempo t
        double[][] dp = new double[n + 1][tiempoMaximo + 1];
        
        // ========================================
        // PASO 2: LLENAR TABLA DP - O(n × W)
        // ========================================
        for (int i = 1; i <= n; i++) {  // O(n) iteraciones
            Pelicula pelicula = peliculas.get(i - 1);  // O(1)
            int duracion = pelicula.getDuracion() != null ? pelicula.getDuracion() : 0;
            double rating = pelicula.getPromedioRating() != null ? pelicula.getPromedioRating() : 0.0;
            
            for (int t = 0; t <= tiempoMaximo; t++) {  // O(W) iteraciones
                // RECURRENCIA DE DP (SUBESTRUCTURA ÓPTIMA)
                
                // Opción 1: NO incluir esta película
                dp[i][t] = dp[i - 1][t];  // O(1)
                
                // Opción 2: Incluir esta película (si cabe)
                if (duracion <= t) {  // O(1)
                    // Tomar el mejor resultado con tiempo restante + rating actual
                    double valorConPelicula = dp[i - 1][t - duracion] + rating;  // O(1)
                    dp[i][t] = Math.max(dp[i][t], valorConPelicula);  // O(1)
                }
            }
        }
        // Total Paso 2: n × W × O(1) = O(n × W)
        
        // ========================================
        // PASO 3: RECONSTRUIR SOLUCIÓN - O(n)
        // ========================================
        List<Pelicula> peliculasSeleccionadas = new ArrayList<>();
        int tiempoRestante = tiempoMaximo;
        double puntuacionTotal = dp[n][tiempoMaximo];
        
        for (int i = n; i > 0 && puntuacionTotal > 0; i--) {  // O(n) iteraciones
            // Si el valor cambió, significa que incluimos esta película
            if (dp[i][tiempoRestante] != dp[i - 1][tiempoRestante]) {  // O(1)
                Pelicula pelicula = peliculas.get(i - 1);  // O(1)
                peliculasSeleccionadas.add(pelicula);  // O(1)
                
                tiempoRestante -= pelicula.getDuracion();  // O(1)
                puntuacionTotal -= pelicula.getPromedioRating();  // O(1)
            }
        }
        
        // O(n): Invertir para obtener orden original
        Collections.reverse(peliculasSeleccionadas);
        
        int tiempoTotal = tiempoMaximo - tiempoRestante;
        double puntuacionFinal = dp[n][tiempoMaximo];
        
        return new ResultadoDP(peliculasSeleccionadas, tiempoTotal, puntuacionFinal);
    }
    // COMPLEJIDAD TOTAL: O(n × W) + O(n) = O(n × W)
    
    /**
     * Versión alternativa: Maximizar cantidad de películas
     * COMPLEJIDAD: O(n log n) por el ordenamiento
     */
    public ResultadoDP maratonMaximaCantidad(List<Pelicula> peliculas, int tiempoMaximo) {
        if (peliculas == null || peliculas.isEmpty() || tiempoMaximo <= 0) {
            return new ResultadoDP(new ArrayList<>(), 0, 0.0);
        }
        
        // O(n log n): Ordenar por duración (más cortas primero)
        List<Pelicula> peliculasOrdenadas = new ArrayList<>(peliculas);
        peliculasOrdenadas.sort(Comparator.comparing(Pelicula::getDuracion));
        
        List<Pelicula> seleccionadas = new ArrayList<>();
        int tiempoAcumulado = 0;
        double puntuacionTotal = 0.0;
        
        // O(n): Greedy - tomar películas cortas mientras quepan
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
     * COMPLEJIDAD: O(n × W) (dominado por maratonOptima)
     */
    public ResultadoDP maratonConMinimo(List<Pelicula> peliculas, int tiempoMaximo, int minimoePeliculas) {
        ResultadoDP resultado = maratonOptima(peliculas, tiempoMaximo);  // O(n × W)
        
        if (resultado.getPeliculasOptimas().size() < minimoePeliculas) {
            // Si no alcanza el mínimo, intentar con estrategia de cantidad
            resultado = maratonMaximaCantidad(peliculas, tiempoMaximo);  // O(n log n)
            resultado.setEstrategia("DP - Forzando mínimo de " + minimoePeliculas + " películas");
        }
        
        return resultado;
    }
}
