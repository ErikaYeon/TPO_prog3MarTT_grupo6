package com.peliculas.recomendador.controller;

import com.peliculas.recomendador.algorithm.AlgoritmoDijkstra;
import com.peliculas.recomendador.model.Pelicula;
import com.peliculas.recomendador.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/peliculas")
@CrossOrigin(origins = "*")
public class PeliculaController {
    
    @Autowired
    private PeliculaRepository peliculaRepository;
    
    @Autowired
    private AlgoritmoDijkstra algoritmoDijkstra;
    
    // ============================================
    // ENDPOINTS BÁSICOS
    // ============================================
    
    @GetMapping
    public List<Pelicula> obtenerTodasLasPeliculas() {
        return peliculaRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Pelicula obtenerPeliculaPorId(@PathVariable Long id) {
        return peliculaRepository.findById(id).orElse(null);
    }
    
    @GetMapping("/genero/{nombreGenero}")
    public List<Pelicula> obtenerPeliculasPorGenero(@PathVariable String nombreGenero) {
        return peliculaRepository.findByGenero(nombreGenero);
    }
    
    @GetMapping("/top")
    public List<Pelicula> obtenerTopPeliculas() {
        return peliculaRepository.findAllOrdenadasPorRating();
    }
    
    @GetMapping("/{id}/relacionadas")
    public List<Pelicula> obtenerPeliculasRelacionadas(
            @PathVariable Long id,
            @RequestParam(defaultValue = "2") int profundidad,
            @RequestParam(defaultValue = "10") int limite) {
        return peliculaRepository.findPeliculasRelacionadas(id, profundidad, limite);
    }
    
    // ============================================
    // BFS - BÚSQUEDA EN ANCHURA (2 PUNTOS) 🔵
    // ============================================
    @GetMapping("/{id}/bfs")
    public List<Pelicula> busquedaBFS(
            @PathVariable Long id,
            @RequestParam(defaultValue = "15") int limite) {
        return peliculaRepository.busquedaBFS(id, limite);
    }
    
    // ============================================
    // DFS - BÚSQUEDA EN PROFUNDIDAD (2 PUNTOS) 🟣
    // ============================================
    @GetMapping("/{id}/dfs")
    public List<Pelicula> busquedaDFS(
            @PathVariable Long id,
            @RequestParam(defaultValue = "3") int profundidad,
            @RequestParam(defaultValue = "15") int limite) {
        return peliculaRepository.busquedaDFS(id, profundidad, limite);
    }
    
    // ============================================
    // DIJKSTRA - CAMINO MÁS CORTO (3 PUNTOS) 🟢
    // ============================================
    @GetMapping("/camino/{idInicio}/{idFin}")
    public List<Pelicula> obtenerCaminoMasCorto(
            @PathVariable Long idInicio,
            @PathVariable Long idFin) {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoDijkstra.caminoMasCorto(todasLasPeliculas, idInicio, idFin);
    }
    
    /**
     * DIJKSTRA: Top N películas más cercanas a una película origen
     * GET /api/peliculas/{id}/dijkstra/cercanas?n=5
     */
    @GetMapping("/{id}/dijkstra/cercanas")
    public List<Pelicula> peliculasCercanas(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int n) {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoDijkstra.topNCercanas(todasLasPeliculas, id, n);
    }
    
    // ============================================
    // TEST DE CONEXIÓN
    // ============================================
    @GetMapping("/test")
    public String testConexion() {
        long count = peliculaRepository.count();
        return "✅ Conexión exitosa! Total de películas: " + count;
    }
}