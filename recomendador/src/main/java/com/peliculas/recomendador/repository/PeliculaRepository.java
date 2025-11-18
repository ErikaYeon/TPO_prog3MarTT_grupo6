package com.peliculas.recomendador.repository;

import com.peliculas.recomendador.model.Pelicula;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeliculaRepository extends Neo4jRepository<Pelicula, Long> {
    
    // Buscar por título - SDN carga automáticamente las relaciones
    Pelicula findByTitulo(String titulo);
    
    // Buscar por género
    @Query("MATCH (p:Pelicula)-[:TIENE_GENERO]->(g:Genero {nombre: $nombreGenero}) " +
           "RETURN p")
    List<Pelicula> findByGenero(@Param("nombreGenero") String nombreGenero);
    
    // Películas con rating mayor a X - SDN carga automáticamente las relaciones
    List<Pelicula> findByPromedioRatingGreaterThan(Double rating);
    
    // Todas las películas ordenadas por rating
    @Query("MATCH (p:Pelicula) " +
           "RETURN p " +
           "ORDER BY p.promedioRating DESC")
    List<Pelicula> findAllOrdenadasPorRating();
    
    // Películas relacionadas (genérico)
    @Query("MATCH path = (inicio:Pelicula {peliculaId: $peliculaId})-[:TIENE_GENERO|SIMILAR_A*1..$profundidad]-(relacionada:Pelicula) " +
           "WHERE inicio <> relacionada " +
           "RETURN DISTINCT relacionada " +
           "LIMIT $limite")
    List<Pelicula> findPeliculasRelacionadas(@Param("peliculaId") Long peliculaId, 
                                             @Param("profundidad") int profundidad, 
                                             @Param("limite") int limite);
    
    // ============================================
    // BFS - BÚSQUEDA EN ANCHURA
    // ============================================
    /**
     * BFS explora el grafo NIVEL POR NIVEL (amplitud primero)
     * 
     * COMPLEJIDAD: O(V + E)
     * - V = número de vértices (películas)
     * - E = número de aristas (relaciones)
     * 
     * IMPLEMENTACIÓN:
     * - Usa ORDER BY distancia ASC para procesar nivel por nivel
     * - Explora distancias 1, 2, 3 en ese orden
     * - Encuentra películas MÁS CERCANAS primero
     * 
     * ESTRUCTURA DE DATOS: Cola FIFO (First In First Out)
     */
    @Query("MATCH (inicio:Pelicula {peliculaId: $peliculaId}) " +
           "CALL { " +
           "  WITH inicio " +
           "  MATCH path = (inicio)-[:SIMILAR_A|TIENE_GENERO*1..3]-(relacionada:Pelicula) " +
           "  WHERE inicio <> relacionada " +
           "  WITH relacionada, length(path) as distancia " +
           "  ORDER BY distancia ASC " +  // nivel por nivel
           "  RETURN DISTINCT relacionada " +
           "  LIMIT $limite " +
           "} " +
           "RETURN relacionada")
    List<Pelicula> busquedaBFS(@Param("peliculaId") Long peliculaId, 
                               @Param("limite") int limite);
    
    // ============================================
    // DFS - BÚSQUEDA EN PROFUNDIDAD
    // ============================================
    /**
     * DFS explora el grafo siguiendo RAMAS COMPLETAS (profundidad primero)
     * 
     * COMPLEJIDAD: O(V + E)
     * - Igual que BFS, pero diferente ORDEN de exploración
     * 
     * IMPLEMENTACIÓN:
     * - Usa ORDER BY length(path) DESC para ir profundo primero
     * - Explora distancias 5, 4, 3, 2, 1 en ese orden
     * - Permite hasta profundidad 5 (más que BFS)
     * 
     * ESTRUCTURA DE DATOS: Pila LIFO (Last In First Out)
     * Neo4j implementa esto internamente
     * 
     */
    @Query("MATCH (inicio:Pelicula {peliculaId: $peliculaId}) " +
           "CALL { " +
           "  WITH inicio " +
           "  MATCH path = (inicio)-[:SIMILAR_A|TIENE_GENERO*1..5]-(relacionada:Pelicula) " +
           "  WHERE inicio <> relacionada AND length(path) <= $profundidad " +
           "  WITH relacionada, path " +
           "  ORDER BY length(path) DESC " +  // profundidad primero
           "  RETURN DISTINCT relacionada " +
           "  LIMIT $limite " +
           "} " +
           "RETURN relacionada")
    List<Pelicula> busquedaDFS(@Param("peliculaId") Long peliculaId, 
                               @Param("profundidad") int profundidad, 
                               @Param("limite") int limite);
    
    // ============================================
    // DIJKSTRA - CAMINO MÁS CORTO
    // ============================================
    @Query("MATCH (inicio:Pelicula {peliculaId: $idInicio}), (fin:Pelicula {peliculaId: $idFin}), " +
           "path = shortestPath((inicio)-[r:SIMILAR_A*]-(fin)) " +
           "RETURN nodes(path)")
    List<Pelicula> findCaminoMasCorto(@Param("idInicio") Long idInicio, 
                                      @Param("idFin") Long idFin);
    
    // ============================================
    // DIJKSTRA con PESO (más preciso)
    // ============================================
    @Query("MATCH (inicio:Pelicula {peliculaId: $idInicio}), (fin:Pelicula {peliculaId: $idFin}) " +
           "CALL gds.shortestPath.dijkstra.stream('peliculasGraph', { " +
           "  sourceNode: inicio, " +
           "  targetNode: fin, " +
           "  relationshipWeightProperty: 'peso' " +
           "}) " +
           "YIELD index, sourceNode, targetNode, totalCost, nodeIds, costs, path " +
           "RETURN " +
           "  [nodeId IN nodeIds | gds.util.asNode(nodeId)] AS camino, " +
           "  totalCost " +
           "ORDER BY totalCost ASC " +
           "LIMIT 1")
    List<Object> findCaminoMasCortoConPeso(@Param("idInicio") Long idInicio, 
                                           @Param("idFin") Long idFin);
}
