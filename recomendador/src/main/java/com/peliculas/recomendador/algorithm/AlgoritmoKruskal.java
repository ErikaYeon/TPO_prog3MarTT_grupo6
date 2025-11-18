package com.peliculas.recomendador.algorithm;

import com.peliculas.recomendador.model.Arista;
import com.peliculas.recomendador.model.Pelicula;
import com.peliculas.recomendador.model.ResultadoMST;
import com.peliculas.recomendador.model.UnionFind;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * KRUSKAL - Algoritmo de Árbol de Expansión Mínimo (MST)
 * Encuentra la red mínima de conexiones entre películas
 * 
 * COMPLEJIDAD TEMPORAL: O(E log E)
 * 
 * Usa Union-Find para detectar ciclos eficientemente
 */
@Component
public class AlgoritmoKruskal {
    
    /**
     * Algoritmo de Kruskal con Union-Find
     * COMPLEJIDAD: O(E log E)
     * - E log E: Ordenamiento de aristas (domina)
     */
    public ResultadoMST arbolExpansionMinimo(List<Arista> todasLasAristas) {
        if (todasLasAristas == null || todasLasAristas.isEmpty()) {
            return new ResultadoMST(new ArrayList<>(), "Kruskal");
        }
        
        List<Arista> mst = new ArrayList<>();
        UnionFind uf = new UnionFind();
        
        // ========================================
        // PASO 1: INICIALIZAR UNION-FIND - O(V)
        // ========================================
        Set<Long> nodos = new HashSet<>();
        // O(E): Extraer todos los nodos únicos
        for (Arista arista : todasLasAristas) {
            nodos.add(arista.getOrigen().getPeliculaId());
            nodos.add(arista.getDestino().getPeliculaId());
        }
        
        // O(V): Crear conjunto para cada nodo
        for (Long nodoId : nodos) {
            uf.makeSet(nodoId);  // O(1)
        }
        
        // ========================================
        // PASO 2: ORDENAR ARISTAS - O(E log E)
        // ========================================
        List<Arista> aristasOrdenadas = new ArrayList<>(todasLasAristas);
        Collections.sort(aristasOrdenadas);  // O(E log E) - ¡DOMINA LA COMPLEJIDAD!
        
        // ========================================
        // PASO 3: ALGORITMO DE KRUSKAL
        // ========================================
        for (Arista arista : aristasOrdenadas) {  // O(E) iteraciones
            Long origenId = arista.getOrigen().getPeliculaId();
            Long destinoId = arista.getDestino().getPeliculaId();
            
            // O(α(V)) ≈ O(1): Verificar si agregar la arista crearía un ciclo
            if (!uf.estanConectados(origenId, destinoId)) {
                mst.add(arista);  // O(1)
                uf.union(origenId, destinoId);  //  O(1)
                
                // Optimización: MST completo tiene exactamente V-1 aristas
                if (mst.size() == nodos.size() - 1) {
                    break;
                }
            }
        }
        
        ResultadoMST resultado = new ResultadoMST(mst, "Kruskal");
        resultado.setNumeroNodos(nodos.size());
        return resultado;
    }
    
    /**
     * Wrapper que construye aristas desde las películas
     * COMPLEJIDAD: O(E log E) dominado por el ordenamiento
     */
    public ResultadoMST arbolExpansionMinimoDesdeGrafo(List<Pelicula> peliculas) {
        if (peliculas == null || peliculas.isEmpty()) {
            return new ResultadoMST(new ArrayList<>(), "Kruskal");
        }
        
        // O(E): Construir lista de aristas evitando duplicados
        List<Arista> aristas = new ArrayList<>();
        Set<String> aristasAgregadas = new HashSet<>();
        
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getPeliculasSimilares() != null) {
                for (var relacion : pelicula.getPeliculasSimilares()) {
                    Long origenId = pelicula.getPeliculaId();
                    Long destinoId = relacion.getPeliculaDestino().getPeliculaId();
                    
                    // O(1): Evitar duplicados (grafo no dirigido)
                    String clave = Math.min(origenId, destinoId) + "-" + Math.max(origenId, destinoId);
                    
                    if (!aristasAgregadas.contains(clave)) {
                        Arista arista = new Arista(
                            pelicula,
                            relacion.getPeliculaDestino(),
                            relacion.getPeso(),
                            relacion.getGenerosComunes()
                        );
                        aristas.add(arista);
                        aristasAgregadas.add(clave);
                    }
                }
            }
        }
        
        // O(E log E): Ejecutar Kruskal
        return arbolExpansionMinimo(aristas);
    }
}
