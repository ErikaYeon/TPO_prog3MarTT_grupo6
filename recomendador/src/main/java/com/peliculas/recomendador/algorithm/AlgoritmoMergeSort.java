package com.peliculas.recomendador.algorithm;

import com.peliculas.recomendador.model.Pelicula;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * MERGESORT - Algoritmo de Ordenamiento Estable (1 PUNTO)
 * Divide y conquista - Garantiza O(n log n) en todos los casos
 */
@Component
public class AlgoritmoMergeSort {
    
    /**
     * MergeSort para ordenar películas por rating (descendente)
     */
    public List<Pelicula> ordenarPorRating(List<Pelicula> peliculas) {
        if (peliculas == null || peliculas.size() <= 1) {
            return peliculas;
        }
        
        List<Pelicula> copia = new ArrayList<>(peliculas);
        mergeSortRating(copia, 0, copia.size() - 1);
        return copia;
    }
    
    private void mergeSortRating(List<Pelicula> lista, int inicio, int fin) {
        if (inicio < fin) {
            int medio = inicio + (fin - inicio) / 2;
            
            // Dividir
            mergeSortRating(lista, inicio, medio);
            mergeSortRating(lista, medio + 1, fin);
            
            // Conquistar (merge)
            mergeRating(lista, inicio, medio, fin);
        }
    }
    
    private void mergeRating(List<Pelicula> lista, int inicio, int medio, int fin) {
        // Crear copias de las sublistas
        List<Pelicula> izquierda = new ArrayList<>();
        List<Pelicula> derecha = new ArrayList<>();
        
        for (int i = inicio; i <= medio; i++) {
            izquierda.add(lista.get(i));
        }
        
        for (int i = medio + 1; i <= fin; i++) {
            derecha.add(lista.get(i));
        }
        
        // Merge (orden descendente - mayor rating primero)
        int i = 0, j = 0, k = inicio;
        
        while (i < izquierda.size() && j < derecha.size()) {
            if (izquierda.get(i).getPromedioRating() >= derecha.get(j).getPromedioRating()) {
                lista.set(k, izquierda.get(i));
                i++;
            } else {
                lista.set(k, derecha.get(j));
                j++;
            }
            k++;
        }
        
        // Copiar elementos restantes
        while (i < izquierda.size()) {
            lista.set(k, izquierda.get(i));
            i++;
            k++;
        }
        
        while (j < derecha.size()) {
            lista.set(k, derecha.get(j));
            j++;
            k++;
        }
    }
    
    /**
     * MergeSort para ordenar películas por año (más recientes primero)
     */
    public List<Pelicula> ordenarPorAño(List<Pelicula> peliculas) {
        if (peliculas == null || peliculas.size() <= 1) {
            return peliculas;
        }
        
        List<Pelicula> copia = new ArrayList<>(peliculas);
        mergeSortAño(copia, 0, copia.size() - 1);
        return copia;
    }
    
    private void mergeSortAño(List<Pelicula> lista, int inicio, int fin) {
        if (inicio < fin) {
            int medio = inicio + (fin - inicio) / 2;
            mergeSortAño(lista, inicio, medio);
            mergeSortAño(lista, medio + 1, fin);
            mergeAño(lista, inicio, medio, fin);
        }
    }
    
    private void mergeAño(List<Pelicula> lista, int inicio, int medio, int fin) {
        List<Pelicula> izquierda = new ArrayList<>();
        List<Pelicula> derecha = new ArrayList<>();
        
        for (int i = inicio; i <= medio; i++) {
            izquierda.add(lista.get(i));
        }
        
        for (int i = medio + 1; i <= fin; i++) {
            derecha.add(lista.get(i));
        }
        
        int i = 0, j = 0, k = inicio;
        
        while (i < izquierda.size() && j < derecha.size()) {
            // Orden descendente (más recientes primero)
            if (izquierda.get(i).getAño() >= derecha.get(j).getAño()) {
                lista.set(k, izquierda.get(i));
                i++;
            } else {
                lista.set(k, derecha.get(j));
                j++;
            }
            k++;
        }
        
        while (i < izquierda.size()) {
            lista.set(k, izquierda.get(i));
            i++;
            k++;
        }
        
        while (j < derecha.size()) {
            lista.set(k, derecha.get(j));
            j++;
            k++;
        }
    }
    
    /**
     * MergeSort para ordenar películas por duración (más cortas primero)
     */
    public List<Pelicula> ordenarPorDuracion(List<Pelicula> peliculas) {
        if (peliculas == null || peliculas.size() <= 1) {
            return peliculas;
        }
        
        List<Pelicula> copia = new ArrayList<>(peliculas);
        mergeSortDuracion(copia, 0, copia.size() - 1);
        return copia;
    }
    
    private void mergeSortDuracion(List<Pelicula> lista, int inicio, int fin) {
        if (inicio < fin) {
            int medio = inicio + (fin - inicio) / 2;
            mergeSortDuracion(lista, inicio, medio);
            mergeSortDuracion(lista, medio + 1, fin);
            mergeDuracion(lista, inicio, medio, fin);
        }
    }
    
    private void mergeDuracion(List<Pelicula> lista, int inicio, int medio, int fin) {
        List<Pelicula> izquierda = new ArrayList<>();
        List<Pelicula> derecha = new ArrayList<>();
        
        for (int i = inicio; i <= medio; i++) {
            izquierda.add(lista.get(i));
        }
        
        for (int i = medio + 1; i <= fin; i++) {
            derecha.add(lista.get(i));
        }
        
        int i = 0, j = 0, k = inicio;
        
        while (i < izquierda.size() && j < derecha.size()) {
            // Orden ascendente (más cortas primero)
            if (izquierda.get(i).getDuracion() <= derecha.get(j).getDuracion()) {
                lista.set(k, izquierda.get(i));
                i++;
            } else {
                lista.set(k, derecha.get(j));
                j++;
            }
            k++;
        }
        
        while (i < izquierda.size()) {
            lista.set(k, izquierda.get(i));
            i++;
            k++;
        }
        
        while (j < derecha.size()) {
            lista.set(k, derecha.get(j));
            j++;
            k++;
        }
    }
    
    /**
     * MergeSort genérico con comparador personalizado
     */
    public List<Pelicula> ordenarPorTitulo(List<Pelicula> peliculas) {
        if (peliculas == null || peliculas.size() <= 1) {
            return peliculas;
        }
        
        List<Pelicula> copia = new ArrayList<>(peliculas);
        mergeSortTitulo(copia, 0, copia.size() - 1);
        return copia;
    }
    
    private void mergeSortTitulo(List<Pelicula> lista, int inicio, int fin) {
        if (inicio < fin) {
            int medio = inicio + (fin - inicio) / 2;
            mergeSortTitulo(lista, inicio, medio);
            mergeSortTitulo(lista, medio + 1, fin);
            mergeTitulo(lista, inicio, medio, fin);
        }
    }
    
    private void mergeTitulo(List<Pelicula> lista, int inicio, int medio, int fin) {
        List<Pelicula> izquierda = new ArrayList<>();
        List<Pelicula> derecha = new ArrayList<>();
        
        for (int i = inicio; i <= medio; i++) {
            izquierda.add(lista.get(i));
        }
        
        for (int i = medio + 1; i <= fin; i++) {
            derecha.add(lista.get(i));
        }
        
        int i = 0, j = 0, k = inicio;
        
        while (i < izquierda.size() && j < derecha.size()) {
            // Orden alfabético
            if (izquierda.get(i).getTitulo().compareTo(derecha.get(j).getTitulo()) <= 0) {
                lista.set(k, izquierda.get(i));
                i++;
            } else {
                lista.set(k, derecha.get(j));
                j++;
            }
            k++;
        }
        
        while (i < izquierda.size()) {
            lista.set(k, izquierda.get(i));
            i++;
            k++;
        }
        
        while (j < derecha.size()) {
            lista.set(k, derecha.get(j));
            j++;
            k++;
        }
    }
}
