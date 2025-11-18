package com.peliculas.recomendador.controller;

import com.peliculas.recomendador.algorithm.AlgoritmoBacktracking;
import com.peliculas.recomendador.algorithm.AlgoritmoGreedy;
import com.peliculas.recomendador.algorithm.AlgoritmoQuickSort;
import com.peliculas.recomendador.algorithm.AlgoritmoMergeSort;
import com.peliculas.recomendador.algorithm.AlgoritmoDP;
import com.peliculas.recomendador.algorithm.AlgoritmoPrim;
import com.peliculas.recomendador.algorithm.AlgoritmoKruskal;
import com.peliculas.recomendador.algorithm.AlgoritmoBranchAndBound;
import com.peliculas.recomendador.algorithm.AlgoritmoDijkstra;
import com.peliculas.recomendador.model.Pelicula;
import com.peliculas.recomendador.model.ResultadoDP;
import com.peliculas.recomendador.model.ResultadoMST;
import com.peliculas.recomendador.model.ResultadoBB;
import com.peliculas.recomendador.repository.PeliculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller para los algoritmos completos
 * Total: 9 puntos (Greedy, QuickSort, MergeSort, Backtracking, DP, Prim, Kruskal, B&B, Dijkstra)
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
    private AlgoritmoMergeSort algoritmoMergeSort;
    
    @Autowired
    private AlgoritmoBacktracking algoritmoBacktracking;
    
    @Autowired
    private AlgoritmoDP algoritmoDP;
    
    @Autowired
    private AlgoritmoPrim algoritmoPrim;
    
    @Autowired
    private AlgoritmoKruskal algoritmoKruskal;
    
    @Autowired
    private AlgoritmoBranchAndBound algoritmoBB;
    
    @Autowired
    private AlgoritmoDijkstra algoritmoDijkstra;
    
    // ============================================
    // GREEDY
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
    // QUICKSORT
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
    // BACKTRACKING
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
            "mergesort", "Ordenamiento estable O(n log n) - Divide y conquista",
            "backtracking", "Búsqueda exhaustiva - Mix de géneros, maratones exactos",
            "dp", "Programación Dinámica - Maratón óptimo maximizando rating",
            "bb", "Branch & Bound - Optimización con poda inteligente",
            "prim", "Árbol de Expansión Mínimo - Red mínima de conexiones (desde un nodo)",
            "kruskal", "Árbol de Expansión Mínimo - Red mínima de conexiones (global)"
        );
    }
    
    // ============================================
    // MERGESORT - ORDENAMIENTO ESTABLE
    // ============================================
    
    /**
     * MERGESORT: Ordenar por rating (descendente)
     * GET /api/algoritmos/mergesort/rating
     */
    @GetMapping("/mergesort/rating")
    public List<Pelicula> mergeSortPorRating() {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoMergeSort.ordenarPorRating(todasLasPeliculas);
    }
    
    /**
     * MERGESORT: Ordenar por año (más recientes primero)
     * GET /api/algoritmos/mergesort/año
     */
    @GetMapping("/mergesort/año")
    public List<Pelicula> mergeSortPorAño() {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoMergeSort.ordenarPorAño(todasLasPeliculas);
    }
    
    /**
     * MERGESORT: Ordenar por duración (más cortas primero)
     * GET /api/algoritmos/mergesort/duracion
     */
    @GetMapping("/mergesort/duracion")
    public List<Pelicula> mergeSortPorDuracion() {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoMergeSort.ordenarPorDuracion(todasLasPeliculas);
    }
    
    /**
     * MERGESORT: Ordenar alfabéticamente por título
     * GET /api/algoritmos/mergesort/titulo
     */
    @GetMapping("/mergesort/titulo")
    public List<Pelicula> mergeSortPorTitulo() {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoMergeSort.ordenarPorTitulo(todasLasPeliculas);
    }
    
    // ============================================
    // DP - PROGRAMACIÓN DINÁMICA
    // ============================================
    
    /**
     * DP: Maratón óptimo maximizando rating dentro de un tiempo
     * GET /api/algoritmos/dp/maraton-optimo?tiempoMaximo=360
     */
    @GetMapping("/dp/maraton-optimo")
    public ResultadoDP maratonOptimo(
            @RequestParam(defaultValue = "360") int tiempoMaximo) {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoDP.maratonOptima(todasLasPeliculas, tiempoMaximo);
    }
    
    /**
     * DP: Maratón maximizando cantidad de películas
     * GET /api/algoritmos/dp/maraton-cantidad?tiempoMaximo=360
     */
    @GetMapping("/dp/maraton-cantidad")
    public ResultadoDP maratonMaximaCantidad(
            @RequestParam(defaultValue = "360") int tiempoMaximo) {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoDP.maratonMaximaCantidad(todasLasPeliculas, tiempoMaximo);
    }
    
    /**
     * DP: Maratón con mínimo de películas
     * GET /api/algoritmos/dp/maraton-minimo?tiempoMaximo=360&minimo=3
     */
    @GetMapping("/dp/maraton-minimo")
    public ResultadoDP maratonConMinimo(
            @RequestParam(defaultValue = "360") int tiempoMaximo,
            @RequestParam(defaultValue = "3") int minimo) {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoDP.maratonConMinimo(todasLasPeliculas, tiempoMaximo, minimo);
    }
    
    // ============================================
    // PRIM - ÁRBOL DE EXPANSIÓN MÍNIMO
    // ============================================
    
    /**
     * PRIM: Árbol de expansión mínimo desde todas las películas
     * GET /api/algoritmos/prim/mst
     */
    @GetMapping("/prim/mst")
    public ResultadoMST primMST() {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoPrim.arbolExpansionMinimoDesdeGrafo(todasLasPeliculas);
    }
    
    // ============================================
    // KRUSKAL - ÁRBOL DE EXPANSIÓN MÍNIMO
    // ============================================
    
    /**
     * KRUSKAL: Árbol de expansión mínimo desde todas las películas
     * GET /api/algoritmos/kruskal/mst
     */
    @GetMapping("/kruskal/mst")
    public ResultadoMST kruskalMST() {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoKruskal.arbolExpansionMinimoDesdeGrafo(todasLasPeliculas);
    }
    
    // ============================================
    // BRANCH & BOUND - OPTIMIZACIÓN CON PODA
    // ============================================
    
    /**
     * B&B: Maratón óptimo con poda inteligente
     * GET /api/algoritmos/bb/maraton-optimo?tiempoMaximo=360
     */
    @GetMapping("/bb/maraton-optimo")
    public ResultadoBB bbMaratonOptimo(
            @RequestParam(defaultValue = "360") int tiempoMaximo) {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoBB.maratonOptimo(todasLasPeliculas, tiempoMaximo);
    }
    
    /**
     * B&B: Maratón maximizando cantidad
     * GET /api/algoritmos/bb/maraton-cantidad?tiempoMaximo=360
     */
    @GetMapping("/bb/maraton-cantidad")
    public ResultadoBB bbMaratonCantidad(
            @RequestParam(defaultValue = "360") int tiempoMaximo) {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoBB.maratonMaximaCantidad(todasLasPeliculas, tiempoMaximo);
    }
    
    /**
     * B&B: Maratón con mínimo de películas
     * GET /api/algoritmos/bb/maraton-minimo?tiempoMaximo=360&minimo=3
     */
    @GetMapping("/bb/maraton-minimo")
    public ResultadoBB bbMaratonConMinimo(
            @RequestParam(defaultValue = "360") int tiempoMaximo,
            @RequestParam(defaultValue = "3") int minimo) {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoBB.maratonConMinimo(todasLasPeliculas, tiempoMaximo, minimo);
    }
    
    // ============================================
    // DIJKSTRA - CAMINO MÁS CORTO MANUAL
    // ============================================
    
    /**
     * DIJKSTRA MANUAL: Camino más corto entre dos películas
     * GET /api/algoritmos/dijkstra/camino/{idInicio}/{idFin}
     */
    @GetMapping("/dijkstra/camino/{idInicio}/{idFin}")
    public List<Pelicula> dijkstraCaminoMasCorto(
            @PathVariable Long idInicio,
            @PathVariable Long idFin) {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoDijkstra.caminoMasCorto(todasLasPeliculas, idInicio, idFin);
    }
    
    /**
     * DIJKSTRA MANUAL: Top N películas más cercanas
     * GET /api/algoritmos/dijkstra/cercanas/{id}?n=5
     */
    @GetMapping("/dijkstra/cercanas/{id}")
    public List<Pelicula> dijkstraPeliculasCercanas(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int n) {
        List<Pelicula> todasLasPeliculas = peliculaRepository.findAll();
        return algoritmoDijkstra.topNCercanas(todasLasPeliculas, id, n);
    }
}