package com.peliculas.recomendador.controller;

import com.peliculas.recomendador.algorithm.AlgoritmoBacktracking;
import com.peliculas.recomendador.algorithm.AlgoritmoGreedy;
import com.peliculas.recomendador.algorithm.AlgoritmoQuickSort;
import com.peliculas.recomendador.model.Pelicula;
import com.peliculas.recomendador.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller para los algoritmos: Greedy, QuickSort, Backtracking
 * Total: 3 puntos (1+1+1)
 */
@RestController
@RequestMapping("/api/algoritmos")
@CrossOrigin(origins = "*")
public class AlgoritmosController {
    
    @Autowired
    private PeliculaRepository peliculaRepository;
    
    @Autowired
    private AlgoritmoGreedy algoritmoGreedy;
    
    @Autowired
    private AlgoritmoQuickSort algoritmoQuickSort;
    
    @Autowired
    private AlgoritmoBacktracking algoritmoBacktracking;
    
    // ============================================
    // GREEDY (1 PUNTO) 🟢
    // ============================================
    
    /**
     * Recomendación GREEDY: Película del género más frecuente con mejor rating
     * GET /api/algoritmos/greedy/recomendacion
     */
    @GetMapping("/greedy/recomendacion")
    public Pelicula recomendacionGreedy() {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoGreedy.recomendacionPorGeneroMasFrecuente(todasLasPeliculas);
    }
    
    /**
     * GREEDY: Top N películas con mejor rating
     * GET /api/algoritmos/greedy/top?n=5
     */
    @GetMapping("/greedy/top")
    public List<Pelicula> topGreedy(@RequestParam(defaultValue = "5") int n) {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoGreedy.topNMejoresRatings(todasLasPeliculas, n);
    }
    
    /**
     * GREEDY: Maratón que maximiza rating dentro de un tiempo
     * GET /api/algoritmos/greedy/maraton?tiempoMaximo=300
     */
    @GetMapping("/greedy/maraton")
    public List<Pelicula> maratonGreedy(
            @RequestParam(defaultValue = "300") int tiempoMaximo) {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoGreedy.maratonGreedy(todasLasPeliculas, tiempoMaximo);
    }
    
    // ============================================
    // QUICKSORT (1 PUNTO) 🔵
    // ============================================
    
    /**
     * QUICKSORT: Ordenar por rating (descendente)
     * GET /api/algoritmos/quicksort/rating
     */
    @GetMapping("/quicksort/rating")
    public List<Pelicula> ordenarPorRating() {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoQuickSort.ordenarPorRating(todasLasPeliculas);
    }
    
    /**
     * QUICKSORT: Ordenar por año (más recientes primero)
     * GET /api/algoritmos/quicksort/año
     */
    @GetMapping("/quicksort/año")
    public List<Pelicula> ordenarPorAño() {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoQuickSort.ordenarPorAño(todasLasPeliculas);
    }
    
    /**
     * QUICKSORT: Ordenar por duración (más cortas primero)
     * GET /api/algoritmos/quicksort/duracion
     */
    @GetMapping("/quicksort/duracion")
    public List<Pelicula> ordenarPorDuracion() {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoQuickSort.ordenarPorDuracion(todasLasPeliculas);
    }
    
    // ============================================
    // BACKTRACKING (1 PUNTO) 🟣
    // ============================================
    
    /**
     * BACKTRACKING: Mix de géneros (una película de cada género)
     * POST /api/algoritmos/backtracking/mix-generos
     * Body: {"generos": ["Ciencia Ficción", "Drama", "Thriller"]}
     */
    @PostMapping("/backtracking/mix-generos")
    public List<List<Pelicula>> mixGeneros(@RequestBody Map<String, List<String>> request) {
        List<String> generos = request.get("generos");
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoBacktracking.mixGeneros(todasLasPeliculas, generos);
    }
    
    /**
     * BACKTRACKING: Maratón que sume exactamente N minutos
     * GET /api/algoritmos/backtracking/maraton-exacto?tiempo=240
     */
    @GetMapping("/backtracking/maraton-exacto")
    public List<List<Pelicula>> maratonTiempoExacto(
            @RequestParam(defaultValue = "240") int tiempo) {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoBacktracking.maratonTiempoExacto(todasLasPeliculas, tiempo);
    }
    
    /**
     * BACKTRACKING: Todas las combinaciones de N películas
     * GET /api/algoritmos/backtracking/combinaciones?cantidad=3
     */
    @GetMapping("/backtracking/combinaciones")
    public List<List<Pelicula>> todasLasCombinaciones(
            @RequestParam(defaultValue = "3") int cantidad) {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        
        // Limitar a máximo 10 películas para evitar explosión combinatoria
        if (todasLasPeliculas.size() > 10) {
            todasLasPeliculas = todasLasPeliculas.subList(0, 10);
        }
        
        return algoritmoBacktracking.todasLasCombinaciones(todasLasPeliculas, cantidad);
    }
    
    // ============================================
    // INFO DE ALGORITMOS
    // ============================================
    
    @GetMapping("/info")
    public Map<String, String> infoAlgoritmos() {
        return Map.of(
            "greedy", "Algoritmo voraz - Recomendación rápida por género más frecuente",
            "quicksort", "Ordenamiento eficiente O(n log n) - Por rating, año, duración",
            "backtracking", "Búsqueda exhaustiva - Mix de géneros, maratones exactos"
        );
    }
}