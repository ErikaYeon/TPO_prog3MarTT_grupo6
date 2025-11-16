package com.peliculas.recomendador.algorithm;

import com.peliculas.recomendador.model.Pelicula;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * QUICKSORT - Algoritmo de Ordenamiento (1 PUNTO)
 * Ordena películas por rating, año, duración, etc.
 */
@Component
public class AlgoritmoQuickSort {
    
    /**
     * QuickSort para ordenar películas por rating (descendente)
     */
    public List<Pelicula> ordenarPorRating(List<Pelicula> peliculas) {
        if (peliculas == null || peliculas.size() <= 1) {
            return peliculas;
        }
        
        List<Pelicula> copia = new ArrayList<>(peliculas);
        quickSortRating(copia, 0, copia.size() - 1);
        return copia;
    }
    
    private void quickSortRating(List<Pelicula> lista, int inicio, int fin) {
        if (inicio < fin) {
            int pivotIndex = particionRating(lista, inicio, fin);
            quickSortRating(lista, inicio, pivotIndex - 1);
            quickSortRating(lista, pivotIndex + 1, fin);
        }
    }
    
    private int particionRating(List<Pelicula> lista, int inicio, int fin) {
        double pivot = lista.get(fin).getPromedioRating();
        int i = inicio - 1;
        
        for (int j = inicio; j < fin; j++) {
            // Orden descendente (mayor rating primero)
            if (lista.get(j).getPromedioRating() >= pivot) {
                i++;
                swap(lista, i, j);
            }
        }
        
        swap(lista, i + 1, fin);
        return i + 1;
    }
    
    /**
     * QuickSort para ordenar películas por año (más recientes primero)
     */
    public List<Pelicula> ordenarPorAño(List<Pelicula> peliculas) {
        if (peliculas == null || peliculas.size() <= 1) {
            return peliculas;
        }
        
        List<Pelicula> copia = new ArrayList<>(peliculas);
        quickSortAño(copia, 0, copia.size() - 1);
        return copia;
    }
    
    private void quickSortAño(List<Pelicula> lista, int inicio, int fin) {
        if (inicio < fin) {
            int pivotIndex = particionAño(lista, inicio, fin);
            quickSortAño(lista, inicio, pivotIndex - 1);
            quickSortAño(lista, pivotIndex + 1, fin);
        }
    }
    
    private int particionAño(List<Pelicula> lista, int inicio, int fin) {
        int pivot = lista.get(fin).getAño();
        int i = inicio - 1;
        
        for (int j = inicio; j < fin; j++) {
            // Orden descendente (más recientes primero)
            if (lista.get(j).getAño() >= pivot) {
                i++;
                swap(lista, i, j);
            }
        }
        
        swap(lista, i + 1, fin);
        return i + 1;
    }
    
    /**
     * QuickSort para ordenar películas por duración (más cortas primero)
     */
    public List<Pelicula> ordenarPorDuracion(List<Pelicula> peliculas) {
        if (peliculas == null || peliculas.size() <= 1) {
            return peliculas;
        }
        
        List<Pelicula> copia = new ArrayList<>(peliculas);
        quickSortDuracion(copia, 0, copia.size() - 1);
        return copia;
    }
    
    private void quickSortDuracion(List<Pelicula> lista, int inicio, int fin) {
        if (inicio < fin) {
            int pivotIndex = particionDuracion(lista, inicio, fin);
            quickSortDuracion(lista, inicio, pivotIndex - 1);
            quickSortDuracion(lista, pivotIndex + 1, fin);
        }
    }
    
    private int particionDuracion(List<Pelicula> lista, int inicio, int fin) {
        int pivot = lista.get(fin).getDuracion();
        int i = inicio - 1;
        
        for (int j = inicio; j < fin; j++) {
            // Orden ascendente (más cortas primero)
            if (lista.get(j).getDuracion() <= pivot) {
                i++;
                swap(lista, i, j);
            }
        }
        
        swap(lista, i + 1, fin);
        return i + 1;
    }
    
    /**
     * Método auxiliar para intercambiar elementos
     */
    private void swap(List<Pelicula> lista, int i, int j) {
        Pelicula temp = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temp);
    }
    
    /**
     * QuickSort genérico con comparador personalizado
     */
    public List<Pelicula> ordenar(List<Pelicula> peliculas, Comparator<Pelicula> comparator) {
        if (peliculas == null || peliculas.size() <= 1) {
            return peliculas;
        }
        
        List<Pelicula> copia = new ArrayList<>(peliculas);
        copia.sort(comparator);
        return copia;
    }
}