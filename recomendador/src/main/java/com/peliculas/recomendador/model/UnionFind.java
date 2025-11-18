package com.peliculas.recomendador.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Estructura Union-Find (Disjoint Set Union)
 * Detecta ciclos en el algoritmo de Kruskal
 * 
 * OPTIMIZACIONES:
 * 1. Compresión de Ruta en find()
 * 2. Unión por Rango en union()
 * 
 * COMPLEJIDAD: O(α(V)) por operación
 * - α(V) = función inversa de Ackermann ≈ constante
 */
public class UnionFind {
    
    private Map<Long, Long> padre;    // padre[i] = representante del conjunto
    private Map<Long, Integer> rango; // rango[i] = altura del árbol
    
    public UnionFind() {
        this.padre = new HashMap<>();
        this.rango = new HashMap<>();
    }
    
    /**
     * Crea un nuevo conjunto con un solo elemento
     * COMPLEJIDAD: O(1)
     */
    public void makeSet(Long peliculaId) {
        if (!padre.containsKey(peliculaId)) {
            padre.put(peliculaId, peliculaId);  // Es su propio padre
            rango.put(peliculaId, 0);           // Árbol de altura 0
        }
    }
    
    /**
     * Encuentra el representante del conjunto
     * COMPLEJIDAD: O(α(V)) con compresión de ruta
     * 
     * COMPRESIÓN DE RUTA: Aplana el árbol haciendo que todos
     * los nodos apunten directamente a la raíz
     */
    public Long find(Long peliculaId) {
        if (!padre.containsKey(peliculaId)) {
            makeSet(peliculaId);
        }
        
        if (!padre.get(peliculaId).equals(peliculaId)) {
            // COMPRESIÓN DE RUTA: Actualizar padre directamente a la raíz
            padre.put(peliculaId, find(padre.get(peliculaId)));
        }
        
        return padre.get(peliculaId);
    }
    
    /**
     * Une dos conjuntos
     * COMPLEJIDAD: O(α(V)) con unión por rango
     * 
     * UNIÓN POR RANGO: Siempre cuelga el árbol más pequeño
     * del más grande para mantener árboles balanceados
     */
    public void union(Long peliculaId1, Long peliculaId2) {
        Long raiz1 = find(peliculaId1);  // O(α(V))
        Long raiz2 = find(peliculaId2);  // O(α(V))
        
        if (raiz1.equals(raiz2)) {
            return; // Ya están en el mismo conjunto
        }
        
        // UNIÓN POR RANGO: Colgar árbol pequeño del grande
        int rango1 = rango.get(raiz1);
        int rango2 = rango.get(raiz2);
        
        if (rango1 < rango2) {
            padre.put(raiz1, raiz2);  // raiz1 cuelga de raiz2
        } else if (rango1 > rango2) {
            padre.put(raiz2, raiz1);  // raiz2 cuelga de raiz1
        } else {
            // Rangos iguales: cualquiera cuelga del otro
            padre.put(raiz2, raiz1);
            rango.put(raiz1, rango1 + 1);  // Incrementar altura
        }
    }
    
    /**
     * Verifica si dos nodos están en el mismo conjunto
     * COMPLEJIDAD: O(α(V))
     */
    public boolean estanConectados(Long peliculaId1, Long peliculaId2) {
        return find(peliculaId1).equals(find(peliculaId2));  // 2 × O(α(V))
    }
}
