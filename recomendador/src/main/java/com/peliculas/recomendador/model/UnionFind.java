package com.peliculas.recomendador.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Estructura Union-Find (Disjoint Set Union)
 * Usado para detectar ciclos en el algoritmo de Kruskal
 */
public class UnionFind {
    
    private Map<Long, Long> padre;
    private Map<Long, Integer> rango;
    
    public UnionFind() {
        this.padre = new HashMap<>();
        this.rango = new HashMap<>();
    }
    
    /**
     * Inicializa un nodo en la estructura
     */
    public void makeSet(Long peliculaId) {
        if (!padre.containsKey(peliculaId)) {
            padre.put(peliculaId, peliculaId);
            rango.put(peliculaId, 0);
        }
    }
    
    /**
     * Encuentra el representante del conjunto (con compresión de ruta)
     */
    public Long find(Long peliculaId) {
        if (!padre.containsKey(peliculaId)) {
            makeSet(peliculaId);
        }
        
        if (!padre.get(peliculaId).equals(peliculaId)) {
            // Compresión de ruta
            padre.put(peliculaId, find(padre.get(peliculaId)));
        }
        
        return padre.get(peliculaId);
    }
    
    /**
     * Une dos conjuntos (con unión por rango)
     */
    public void union(Long peliculaId1, Long peliculaId2) {
        Long raiz1 = find(peliculaId1);
        Long raiz2 = find(peliculaId2);
        
        if (raiz1.equals(raiz2)) {
            return; // Ya están en el mismo conjunto
        }
        
        // Unión por rango
        int rango1 = rango.get(raiz1);
        int rango2 = rango.get(raiz2);
        
        if (rango1 < rango2) {
            padre.put(raiz1, raiz2);
        } else if (rango1 > rango2) {
            padre.put(raiz2, raiz1);
        } else {
            padre.put(raiz2, raiz1);
            rango.put(raiz1, rango1 + 1);
        }
    }
    
    /**
     * Verifica si dos nodos están en el mismo conjunto
     */
    public boolean estanConectados(Long peliculaId1, Long peliculaId2) {
        return find(peliculaId1).equals(find(peliculaId2));
    }
}
