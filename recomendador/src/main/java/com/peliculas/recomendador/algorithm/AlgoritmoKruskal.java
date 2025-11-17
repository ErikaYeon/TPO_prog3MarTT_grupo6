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
 */
@Component
public class AlgoritmoKruskal {
    
    /**
     * Implementación del algoritmo de Kruskal
     * Encuentra el MST usando Union-Find
     */
    public ResultadoMST arbolExpansionMinimo(List<Arista> todasLasAristas) {
        if (todasLasAristas == null || todasLasAristas.isEmpty()) {
            return new ResultadoMST(new ArrayList<>(), "Kruskal");
        }
        
        List<Arista> mst = new ArrayList<>();
        UnionFind uf = new UnionFind();
        
        // PASO 1: Inicializar Union-Find con todos los nodos
        Set<Long> nodos = new HashSet<>();
        for (Arista arista : todasLasAristas) {
            nodos.add(arista.getOrigen().getPeliculaId());
            nodos.add(arista.getDestino().getPeliculaId());
        }
        
        for (Long nodoId : nodos) {
            uf.makeSet(nodoId);
        }
        
        // PASO 2: Ordenar todas las aristas por peso (menor a mayor)
        List<Arista> aristasOrdenadas = new ArrayList<>(todasLasAristas);
        Collections.sort(aristasOrdenadas);
        
        // PASO 3: Algoritmo de Kruskal
        for (Arista arista : aristasOrdenadas) {
            Long origenId = arista.getOrigen().getPeliculaId();
            Long destinoId = arista.getDestino().getPeliculaId();
            
            // Si los nodos NO están conectados, agregar la arista
            if (!uf.estanConectados(origenId, destinoId)) {
                mst.add(arista);
                uf.union(origenId, destinoId);
                
                // Condición de parada: MST completo (n-1 aristas)
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
     * Versión que acepta películas y construye las aristas automáticamente
     */
    public ResultadoMST arbolExpansionMinimoDesdeGrafo(List<Pelicula> peliculas) {
        if (peliculas == null || peliculas.isEmpty()) {
            return new ResultadoMST(new ArrayList<>(), "Kruskal");
        }
        
        // Construir lista de aristas desde las relaciones de similitud
        List<Arista> aristas = new ArrayList<>();
        Set<String> aristasAgregadas = new HashSet<>();
        
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getPeliculasSimilares() != null) {
                for (var relacion : pelicula.getPeliculasSimilares()) {
                    Long origenId = pelicula.getPeliculaId();
                    Long destinoId = relacion.getPeliculaDestino().getPeliculaId();
                    
                    // Evitar duplicados (arista bidireccional)
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
        
        return arbolExpansionMinimo(aristas);
    }
}
