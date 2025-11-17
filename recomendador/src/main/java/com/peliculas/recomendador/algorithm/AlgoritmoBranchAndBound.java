package com.peliculas.recomendador.algorithm;

import com.peliculas.recomendador.model.Pelicula;
import com.peliculas.recomendador.model.ResultadoBB;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * BRANCH & BOUND (Ramificación y Acotación)
 * Encuentra la mejor selección de películas con poda inteligente
 */
@Component
public class AlgoritmoBranchAndBound {
    
    private int nodosExplorados;
    private int nodosPodados;
    
    /**
     * Nodo del árbol de decisión
     */
    private static class Nodo {
        int nivel;                    // Índice de la película actual
        double puntuacionActual;       // Puntuación acumulada
        int tiempoActual;              // Tiempo acumulado
        List<Pelicula> peliculas;      // Películas seleccionadas
        double cotaSuperior;           // Estimación optimista del mejor caso
        
        Nodo(int nivel, double puntuacion, int tiempo, List<Pelicula> peliculas) {
            this.nivel = nivel;
            this.puntuacionActual = puntuacion;
            this.tiempoActual = tiempo;
            this.peliculas = new ArrayList<>(peliculas);
        }
    }
    
    /**
     * Branch & Bound: Maratón óptimo maximizando rating
     */
    public ResultadoBB maratonOptimo(List<Pelicula> peliculas, int tiempoMaximo) {
        if (peliculas == null || peliculas.isEmpty() || tiempoMaximo <= 0) {
            return new ResultadoBB(new ArrayList<>(), 0, 0.0, 0, 0);
        }
        
        // Reiniciar contadores
        nodosExplorados = 0;
        nodosPodados = 0;
        
        // Ordenar por ratio rating/duración (greedy heuristic)
        List<Pelicula> peliculasOrdenadas = new ArrayList<>(peliculas);
        peliculasOrdenadas.sort((p1, p2) -> {
            double ratio1 = p1.getPromedioRating() / Math.max(p1.getDuracion(), 1);
            double ratio2 = p2.getPromedioRating() / Math.max(p2.getDuracion(), 1);
            return Double.compare(ratio2, ratio1);
        });
        
        // Cola de prioridad (ordenada por cota superior)
        PriorityQueue<Nodo> cola = new PriorityQueue<>(
            (n1, n2) -> Double.compare(n2.cotaSuperior, n1.cotaSuperior)
        );
        
        // Nodo inicial
        Nodo raiz = new Nodo(0, 0.0, 0, new ArrayList<>());
        raiz.cotaSuperior = calcularCotaSuperior(peliculasOrdenadas, 0, 0.0, 0, tiempoMaximo);
        cola.offer(raiz);
        
        // Mejor solución encontrada
        double mejorPuntuacion = 0.0;
        List<Pelicula> mejorSeleccion = new ArrayList<>();
        int mejorTiempo = 0;
        
        // Branch & Bound
        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            nodosExplorados++;
            
            // Si la cota superior es peor que la mejor solución, podar
            if (actual.cotaSuperior <= mejorPuntuacion) {
                nodosPodados++;
                continue;
            }
            
            // Si llegamos al final
            if (actual.nivel >= peliculasOrdenadas.size()) {
                if (actual.puntuacionActual > mejorPuntuacion) {
                    mejorPuntuacion = actual.puntuacionActual;
                    mejorSeleccion = new ArrayList<>(actual.peliculas);
                    mejorTiempo = actual.tiempoActual;
                }
                continue;
            }
            
            Pelicula peliculaActual = peliculasOrdenadas.get(actual.nivel);
            
            // OPCIÓN 1: Incluir la película (si cabe)
            if (actual.tiempoActual + peliculaActual.getDuracion() <= tiempoMaximo) {
                List<Pelicula> nuevaSeleccion = new ArrayList<>(actual.peliculas);
                nuevaSeleccion.add(peliculaActual);
                
                Nodo nodoIncluir = new Nodo(
                    actual.nivel + 1,
                    actual.puntuacionActual + peliculaActual.getPromedioRating(),
                    actual.tiempoActual + peliculaActual.getDuracion(),
                    nuevaSeleccion
                );
                
                nodoIncluir.cotaSuperior = calcularCotaSuperior(
                    peliculasOrdenadas,
                    actual.nivel + 1,
                    nodoIncluir.puntuacionActual,
                    nodoIncluir.tiempoActual,
                    tiempoMaximo
                );
                
                // Solo agregar si la cota es prometedora
                if (nodoIncluir.cotaSuperior > mejorPuntuacion) {
                    cola.offer(nodoIncluir);
                } else {
                    nodosPodados++;
                }
                
                // Actualizar mejor solución
                if (nodoIncluir.puntuacionActual > mejorPuntuacion) {
                    mejorPuntuacion = nodoIncluir.puntuacionActual;
                    mejorSeleccion = nuevaSeleccion;
                    mejorTiempo = nodoIncluir.tiempoActual;
                }
            }
            
            // OPCIÓN 2: NO incluir la película
            Nodo nodoExcluir = new Nodo(
                actual.nivel + 1,
                actual.puntuacionActual,
                actual.tiempoActual,
                actual.peliculas
            );
            
            nodoExcluir.cotaSuperior = calcularCotaSuperior(
                peliculasOrdenadas,
                actual.nivel + 1,
                nodoExcluir.puntuacionActual,
                nodoExcluir.tiempoActual,
                tiempoMaximo
            );
            
            // Solo agregar si la cota es prometedora
            if (nodoExcluir.cotaSuperior > mejorPuntuacion) {
                cola.offer(nodoExcluir);
            } else {
                nodosPodados++;
            }
        }
        
        return new ResultadoBB(mejorSeleccion, mejorTiempo, mejorPuntuacion, 
                               nodosExplorados, nodosPodados);
    }
    
    /**
     * Calcula la cota superior (estimación optimista) usando relajación fraccional
     */
    private double calcularCotaSuperior(List<Pelicula> peliculas, int indiceActual, 
                                       double puntuacionActual, int tiempoActual, 
                                       int tiempoMaximo) {
        double cotaSuperior = puntuacionActual;
        int tiempoDisponible = tiempoMaximo - tiempoActual;
        
        // Intentar agregar películas completas mientras quepan
        for (int i = indiceActual; i < peliculas.size() && tiempoDisponible > 0; i++) {
            Pelicula pelicula = peliculas.get(i);
            
            if (pelicula.getDuracion() <= tiempoDisponible) {
                // Agregar película completa
                cotaSuperior += pelicula.getPromedioRating();
                tiempoDisponible -= pelicula.getDuracion();
            } else {
                // Relajación fraccional: agregar proporción de la película
                double fraccion = (double) tiempoDisponible / pelicula.getDuracion();
                cotaSuperior += pelicula.getPromedioRating() * fraccion;
                break;
            }
        }
        
        return cotaSuperior;
    }
    
    /**
     * B&B con restricción de cantidad mínima de películas
     */
    public ResultadoBB maratonConMinimo(List<Pelicula> peliculas, int tiempoMaximo, int minimoePeliculas) {
        ResultadoBB resultado = maratonOptimo(peliculas, tiempoMaximo);
        
        if (resultado.getPeliculasOptimas().size() < minimoePeliculas) {
            // Si no alcanza el mínimo, intentar con otro enfoque
            // Por simplicidad, devolver el resultado actual con nota
            resultado.setEstrategia("B&B - No se alcanzó el mínimo de " + minimoePeliculas + " películas");
        }
        
        return resultado;
    }
    
    /**
     * B&B maximizando cantidad (en lugar de rating)
     */
    public ResultadoBB maratonMaximaCantidad(List<Pelicula> peliculas, int tiempoMaximo) {
        if (peliculas == null || peliculas.isEmpty() || tiempoMaximo <= 0) {
            return new ResultadoBB(new ArrayList<>(), 0, 0.0, 0, 0);
        }
        
        // Ordenar por duración (más cortas primero)
        List<Pelicula> peliculasOrdenadas = new ArrayList<>(peliculas);
        peliculasOrdenadas.sort(Comparator.comparing(Pelicula::getDuracion));
        
        List<Pelicula> seleccion = new ArrayList<>();
        int tiempoAcumulado = 0;
        double puntuacionTotal = 0.0;
        
        for (Pelicula pelicula : peliculasOrdenadas) {
            if (tiempoAcumulado + pelicula.getDuracion() <= tiempoMaximo) {
                seleccion.add(pelicula);
                tiempoAcumulado += pelicula.getDuracion();
                puntuacionTotal += pelicula.getPromedioRating();
            }
        }
        
        ResultadoBB resultado = new ResultadoBB(seleccion, tiempoAcumulado, puntuacionTotal, 1, 0);
        resultado.setEstrategia("B&B - Maximizar Cantidad (Greedy + Ordenamiento)");
        return resultado;
    }
}
