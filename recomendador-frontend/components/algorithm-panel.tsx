'use client';

import { useState } from 'react';
import { Movie, AlgorithmResult } from '@/app/page';
import AlgorithmButton from './algorithm-button';
import { ControlSection } from './control-section';

interface AlgorithmPanelProps {
  movies: Movie[];
  onResult: (result: AlgorithmResult) => void;
  onError: (message: string, type: 'error' | 'success') => void;
  onLoading: (loading: boolean) => void;
}

const API_ALG = process.env.NEXT_PUBLIC_API_ALG || 'http://localhost:8080/api/algoritmos';
const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/peliculas';

export default function AlgorithmPanel({
  movies,
  onResult,
  onError,
  onLoading,
}: AlgorithmPanelProps) {
  const [selectedMovie, setSelectedMovie] = useState('');
  const [startMovie, setStartMovie] = useState('');
  const [endMovie, setEndMovie] = useState('');
  const [marathonTime, setMarathonTime] = useState(300);
  const [exactTime, setExactTime] = useState(240);
  const [dpTime, setDpTime] = useState(360); // Para DP
  const [bbTime, setBbTime] = useState(360); // Para B&B

  const executeAlgorithm = async (
    url: string,
    title: string,
    algorithm: string,
    processResponse?: (data: any) => Movie[]
  ) => {
    onLoading(true);
    try {
      const response = await fetch(url);
      const data = await response.json();
      const moviesList = processResponse ? processResponse(data) : data;
      onResult({
        title,
        algorithm,
        movies: Array.isArray(moviesList) ? moviesList : [moviesList],
      });
    } catch (error) {
      onError('Error al ejecutar algoritmo', 'error');
    } finally {
      onLoading(false);
    }
  };

  const handleBFS = async () => {
    if (!selectedMovie) {
      onError('Por favor selecciona una película', 'error');
      return;
    }
    
    const movieId = Number(selectedMovie);
    const movieName = movies.find(m => m.peliculaId === movieId)?.titulo || '';
    
    await executeAlgorithm(
      `${API_URL}/${selectedMovie}/bfs?profundidad=3&limite=15`,
      `BFS desde "${movieName}"`,
      'BFS'
    );
  };

  const handleDFS = async () => {
    if (!selectedMovie) {
      onError('Por favor selecciona una película', 'error');
      return;
    }
    
    const movieId = Number(selectedMovie);
    const movieName = movies.find(m => m.peliculaId === movieId)?.titulo || '';
    
    await executeAlgorithm(
      `${API_URL}/${selectedMovie}/dfs?profundidad=3&limite=15`,
      `DFS desde "${movieName}"`,
      'DFS'
    );
  };

  const handleDijkstra = async () => {
    if (!startMovie || !endMovie) {
      onError('Por favor selecciona ambas películas', 'error');
      return;
    }
    await executeAlgorithm(
      `${API_ALG}/dijkstra/camino/${startMovie}/${endMovie}`,
      'Camino Más Corto Dijkstra Manual',
      'DIJKSTRA'
    );
  };

  const handleDijkstraManual = async () => {
    if (!startMovie || !endMovie) {
      onError('Por favor selecciona ambas películas', 'error');
      return;
    }
    
    // Convertir a números
    const startMovieId = Number(startMovie);
    const endMovieId = Number(endMovie);
    
    const startMovieName = movies.find(m => m.peliculaId === startMovieId)?.titulo || 'Película desconocida';
    const endMovieName = movies.find(m => m.peliculaId === endMovieId)?.titulo || 'Película desconocida';
    
    await executeAlgorithm(
      `${API_ALG}/dijkstra/camino/${startMovie}/${endMovie}`,
      `Dijkstra: "${startMovieName}" → "${endMovieName}"`,
      'DIJKSTRA'
    );
  };

  const handleGreedy = async () => {
    await executeAlgorithm(
      `${API_ALG}/greedy/recomendacion`,
      'Recomendación Greedy',
      'GREEDY'
    );
  };

  const handleMarathonGreedy = async () => {
    await executeAlgorithm(
      `${API_ALG}/greedy/maraton?tiempoMaximo=${marathonTime}`,
      `Maratón Greedy (${marathonTime} min)`,
      'GREEDY'
    );
  };

  const handleQuickSortRating = async () => {
    await executeAlgorithm(
      `${API_ALG}/quicksort/rating`,
      'Ordenado por Calificación',
      'QUICKSORT'
    );
  };

  const handleQuickSortYear = async () => {
    await executeAlgorithm(
      `${API_ALG}/quicksort/año`,
      'Ordenado por Año',
      'QUICKSORT'
    );
  };

  // ============================================
  // MERGESORT
  // ============================================
  const handleMergeSortRating = async () => {
    await executeAlgorithm(
      `${API_ALG}/mergesort/rating`,
      'MergeSort por Rating',
      'MERGESORT'
    );
  };

  const handleMergeSortYear = async () => {
    await executeAlgorithm(
      `${API_ALG}/mergesort/año`,
      'MergeSort por Año',
      'MERGESORT'
    );
  };

  const handleMergeSortDuration = async () => {
    await executeAlgorithm(
      `${API_ALG}/mergesort/duracion`,
      'MergeSort por Duración',
      'MERGESORT'
    );
  };

  const handleMergeSortTitle = async () => {
    await executeAlgorithm(
      `${API_ALG}/mergesort/titulo`,
      'MergeSort Alfabético',
      'MERGESORT'
    );
  };

  const handleBacktrackingMix = async () => {
    onLoading(true);
    try {
      const response = await fetch(`${API_ALG}/backtracking/mix-generos`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ generos: ['Ciencia Ficción', 'Drama', 'Thriller'] }),
      });
      const combinations = await response.json();
      onResult({
        title: 'Mezcla de Géneros',
        algorithm: 'BACKTRACKING',
        movies: combinations[0] || [],
      });
    } catch (error) {
      onError('Error al generar mezcla de géneros', 'error');
    } finally {
      onLoading(false);
    }
  };

  const handleBacktrackingExact = async () => {
    await executeAlgorithm(
      `${API_ALG}/backtracking/maraton-exacto?tiempo=${exactTime}`,
      `Maratón Exacto (${exactTime} min)`,
      'BACKTRACKING',
      (data) => data[0] || []
    );
  };

  // ============================================
  // DP - PROGRAMACIÓN DINÁMICA
  // ============================================
  const handleDPOptimo = async () => {
    onLoading(true);
    try {
      const response = await fetch(`${API_ALG}/dp/maraton-optimo?tiempoMaximo=${dpTime}`);
      const resultado = await response.json();
      onResult({
        title: `Maratón Óptimo DP (${dpTime} min) - Rating: ${resultado.puntuacionTotal?.toFixed(2)}`,
        algorithm: 'DP',
        movies: resultado.peliculasOptimas || [],
        metadata: {
          tiempo: resultado.tiempoTotal,
          puntuacion: resultado.puntuacionTotal,
          eficiencia: resultado.ratioEficiencia,
        }
      });
    } catch (error) {
      onError('Error al ejecutar DP óptimo', 'error');
    } finally {
      onLoading(false);
    }
  };

  const handleDPCantidad = async () => {
    onLoading(true);
    try {
      const response = await fetch(`${API_ALG}/dp/maraton-cantidad?tiempoMaximo=${dpTime}`);
      const resultado = await response.json();
      onResult({
        title: `Máxima Cantidad DP (${dpTime} min) - ${resultado.peliculasOptimas?.length || 0} películas`,
        algorithm: 'DP',
        movies: resultado.peliculasOptimas || [],
        metadata: {
          tiempo: resultado.tiempoTotal,
          puntuacion: resultado.puntuacionTotal,
          cantidad: resultado.peliculasOptimas?.length,
        }
      });
    } catch (error) {
      onError('Error al ejecutar DP cantidad', 'error');
    } finally {
      onLoading(false);
    }
  };

  // ============================================
  // PRIM / KRUSKAL - MST
  // ============================================
  const handlePrim = async () => {
    onLoading(true);
    try {
      const response = await fetch(`${API_ALG}/prim/mst`);
      const resultado = await response.json();
      
      // Convertir aristas a películas para mostrar
      const peliculasEnMST = new Map<string, any>();
      resultado.aristas?.forEach((arista: any) => {
        peliculasEnMST.set(arista.origen.peliculaId, arista.origen);
        peliculasEnMST.set(arista.destino.peliculaId, arista.destino);
      });

      onResult({
        title: `Prim MST - ${resultado.numeroNodos} nodos, Peso: ${resultado.pesoTotal?.toFixed(2)}`,
        algorithm: 'PRIM',
        movies: Array.from(peliculasEnMST.values()),
        metadata: {
          numeroAristas: resultado.numeroAristas,
          pesoTotal: resultado.pesoTotal,
          algoritmo: resultado.algoritmo,
        }
      });
    } catch (error) {
      onError('Error al ejecutar Prim', 'error');
    } finally {
      onLoading(false);
    }
  };

  const handleKruskal = async () => {
    onLoading(true);
    try {
      const response = await fetch(`${API_ALG}/kruskal/mst`);
      const resultado = await response.json();
      
      // Convertir aristas a películas para mostrar
      const peliculasEnMST = new Map<string, any>();
      resultado.aristas?.forEach((arista: any) => {
        peliculasEnMST.set(arista.origen.peliculaId, arista.origen);
        peliculasEnMST.set(arista.destino.peliculaId, arista.destino);
      });

      onResult({
        title: `Kruskal MST - ${resultado.numeroNodos} nodos, Peso: ${resultado.pesoTotal?.toFixed(2)}`,
        algorithm: 'KRUSKAL',
        movies: Array.from(peliculasEnMST.values()),
        metadata: {
          numeroAristas: resultado.numeroAristas,
          pesoTotal: resultado.pesoTotal,
          algoritmo: resultado.algoritmo,
        }
      });
    } catch (error) {
      onError('Error al ejecutar Kruskal', 'error');
    } finally {
      onLoading(false);
    }
  };

  // ============================================
  // BRANCH & BOUND
  // ============================================
  const handleBBOptimo = async () => {
    onLoading(true);
    try {
      const response = await fetch(`${API_ALG}/bb/maraton-optimo?tiempoMaximo=${bbTime}`);
      const resultado = await response.json();
      onResult({
        title: `B&B Óptimo (${bbTime} min) - Rating: ${resultado.puntuacionTotal?.toFixed(2)} | Nodos: ${resultado.nodosExplorados}, Podados: ${resultado.nodosPodados}`,
        algorithm: 'BB',
        movies: resultado.peliculasOptimas || [],
        metadata: {
          tiempo: resultado.tiempoTotal,
          puntuacion: resultado.puntuacionTotal,
          eficiencia: resultado.ratioEficiencia,
          nodosExplorados: resultado.nodosExplorados,
          nodosPodados: resultado.nodosPodados,
        }
      });
    } catch (error) {
      onError('Error al ejecutar B&B', 'error');
    } finally {
      onLoading(false);
    }
  };

  const handleBBCantidad = async () => {
    onLoading(true);
    try {
      const response = await fetch(`${API_ALG}/bb/maraton-cantidad?tiempoMaximo=${bbTime}`);
      const resultado = await response.json();
      onResult({
        title: `B&B Cantidad (${bbTime} min) - ${resultado.peliculasOptimas?.length || 0} películas, Rating: ${resultado.puntuacionTotal?.toFixed(2)}`,
        algorithm: 'BB',
        movies: resultado.peliculasOptimas || [],
        metadata: {
          tiempo: resultado.tiempoTotal,
          puntuacion: resultado.puntuacionTotal,
          cantidad: resultado.peliculasOptimas?.length,
          nodosExplorados: resultado.nodosExplorados,
        }
      });
    } catch (error) {
      onError('Error al ejecutar B&B Cantidad', 'error');
    } finally {
      onLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Graph Algorithms */}
      <div className="bg-card rounded-xl p-6 shadow-lg border border-border">
        <h2 className="text-xl font-bold text-primary mb-4">🔬 Algoritmos de Grafos</h2>
        <div className="bg-muted/30 border-l-4 border-primary p-3 mb-4 rounded text-sm text-muted-foreground">
          BFS + DFS + Dijkstra
        </div>

        <ControlSection label="Seleccionar Película">
          <select
            value={selectedMovie}
            onChange={(e) => setSelectedMovie(e.target.value)}
            className="w-full px-3 py-2 bg-background border border-border rounded-lg text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
          >
            <option value="">Elige una película...</option>
            {movies.map(m => (
              <option key={m.peliculaId} value={m.peliculaId}>{m.titulo}</option>
            ))}
          </select>

          <AlgorithmButton
            onClick={handleBFS}
            label="BFS - Amplitud"
            icon="🔵"
            variant="bfs"
          />
          <AlgorithmButton
            onClick={handleDFS}
            label="DFS - Profundidad"
            icon="🟣"
            variant="dfs"
          />
        </ControlSection>

        <ControlSection label="Camino Más Corto">
          <select
            value={startMovie}
            onChange={(e) => setStartMovie(e.target.value)}
            className="w-full px-3 py-2 bg-background border border-border rounded-lg text-foreground focus:outline-none focus:ring-2 focus:ring-primary mb-2"
          >
            <option value="">Película de inicio...</option>
            {movies.map(m => (
              <option key={m.peliculaId} value={m.peliculaId}>{m.titulo}</option>
            ))}
          </select>

          <select
            value={endMovie}
            onChange={(e) => setEndMovie(e.target.value)}
            className="w-full px-3 py-2 bg-background border border-border rounded-lg text-foreground focus:outline-none focus:ring-2 focus:ring-primary mb-2"
          >
            <option value="">Película de fin...</option>
            {movies.map(m => (
              <option key={m.peliculaId} value={m.peliculaId}>{m.titulo}</option>
            ))}
          </select>

          <AlgorithmButton
            onClick={handleDijkstraManual}
            label="Dijkstra Manual (Java)"
            icon="🟢"
            variant="dijkstra-manual"
          />
        </ControlSection>
      </div>

      {/* Other Algorithms */}
      <div className="bg-card rounded-xl p-6 shadow-lg border border-border">
        <h2 className="text-xl font-bold text-primary mb-4">⚡ Otros Algoritmos (3 pts)</h2>
        <div className="bg-muted/30 border-l-4 border-primary p-3 mb-4 rounded text-sm text-muted-foreground">
          Greedy + QuickSort + Backtracking
        </div>

        <ControlSection label="GREEDY">
          <AlgorithmButton
            onClick={handleGreedy}
            label="Recomendación Rápida"
            icon="⚡"
            variant="greedy"
          />

          <div className="space-y-2">
            <label className="block text-sm font-medium text-foreground">Tiempo de maratón (min)</label>
            <input
              type="number"
              value={marathonTime}
              onChange={(e) => setMarathonTime(Number(e.target.value))}
              className="w-full px-3 py-2 bg-background border border-border rounded-lg text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
            />
          </div>

          <AlgorithmButton
            onClick={handleMarathonGreedy}
            label="Maratón Greedy"
            icon="📺"
            variant="greedy"
          />
        </ControlSection>

        <ControlSection label="QUICKSORT">
          <AlgorithmButton
            onClick={handleQuickSortRating}
            label="Ordenar por Calificación"
            icon="📊"
            variant="quicksort"
          />
          <AlgorithmButton
            onClick={handleQuickSortYear}
            label="Ordenar por Año"
            icon="📅"
            variant="quicksort"
          />
        </ControlSection>

        <ControlSection label="MERGESORT (Estable)">
          <AlgorithmButton
            onClick={handleMergeSortRating}
            label="MergeSort por Rating"
            icon="📊"
            variant="mergesort"
          />
          <AlgorithmButton
            onClick={handleMergeSortYear}
            label="MergeSort por Año"
            icon="📅"
            variant="mergesort"
          />
          <AlgorithmButton
            onClick={handleMergeSortDuration}
            label="MergeSort por Duración"
            icon="⏱️"
            variant="mergesort"
          />
          <AlgorithmButton
            onClick={handleMergeSortTitle}
            label="MergeSort Alfabético"
            icon="🔤"
            variant="mergesort"
          />
        </ControlSection>

        <ControlSection label="BACKTRACKING">
          <AlgorithmButton
            onClick={handleBacktrackingMix}
            label="Mezcla de Géneros"
            icon="🎭"
            variant="backtracking"
          />

          <div className="space-y-2">
            <label className="block text-sm font-medium text-foreground">Tiempo exacto (min)</label>
            <input
              type="number"
              value={exactTime}
              onChange={(e) => setExactTime(Number(e.target.value))}
              className="w-full px-3 py-2 bg-background border border-border rounded-lg text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
            />
          </div>

          <AlgorithmButton
            onClick={handleBacktrackingExact}
            label="Maratón Exacto"
            icon="⏱️"
            variant="backtracking"
          />
        </ControlSection>
      </div>

      {/* DP, Prim, Kruskal */}
      <div className="bg-card rounded-xl p-6 shadow-lg border border-border">
        <h2 className="text-xl font-bold text-primary mb-4">🚀 Algoritmos Avanzados (+3 pts)</h2>
        <div className="bg-muted/30 border-l-4 border-primary p-3 mb-4 rounded text-sm text-muted-foreground">
          Programación Dinámica + Prim + Kruskal
        </div>

        <ControlSection label="PROGRAMACIÓN DINÁMICA">
          <div className="space-y-2">
            <label className="block text-sm font-medium text-foreground">Tiempo disponible (min)</label>
            <input
              type="number"
              value={dpTime}
              onChange={(e) => setDpTime(Number(e.target.value))}
              className="w-full px-3 py-2 bg-background border border-border rounded-lg text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
            />
          </div>

          <AlgorithmButton
            onClick={handleDPOptimo}
            label="Maratón Óptimo (Max Rating)"
            icon="💰"
            variant="dp"
          />
          <AlgorithmButton
            onClick={handleDPCantidad}
            label="Máxima Cantidad"
            icon="📚"
            variant="dp"
          />
        </ControlSection>

        <ControlSection label="ÁRBOL DE EXPANSIÓN MÍNIMO (MST)">
          <AlgorithmButton
            onClick={handlePrim}
            label="Algoritmo de Prim"
            icon="🌳"
            variant="prim"
          />
          <AlgorithmButton
            onClick={handleKruskal}
            label="Algoritmo de Kruskal"
            icon="🌲"
            variant="kruskal"
          />
          <div className="mt-2 text-xs text-muted-foreground bg-muted/20 p-2 rounded">
            📌 MST encuentra la red mínima de conexiones entre películas
          </div>
        </ControlSection>
      </div>

      {/* Branch & Bound */}
      <div className="bg-card rounded-xl p-6 shadow-lg border border-border">
        <h2 className="text-xl font-bold text-primary mb-4">⚡ Branch & Bound (+1 pt)</h2>
        <div className="bg-muted/30 border-l-4 border-primary p-3 mb-4 rounded text-sm text-muted-foreground">
          Optimización con Poda Inteligente - Explora nodos de manera eficiente
        </div>

        <ControlSection label="BRANCH & BOUND">
          <div className="space-y-2">
            <label className="block text-sm font-medium text-foreground">Tiempo disponible (min)</label>
            <input
              type="number"
              value={bbTime}
              onChange={(e) => setBbTime(Number(e.target.value))}
              className="w-full px-3 py-2 bg-background border border-border rounded-lg text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
            />
          </div>

          <AlgorithmButton
            onClick={handleBBOptimo}
            label="B&B Maratón Óptimo"
            icon="🌿"
            variant="bb"
          />
          <AlgorithmButton
            onClick={handleBBCantidad}
            label="B&B Máxima Cantidad"
            icon="🎬"
            variant="bb"
          />
          <div className="mt-2 text-xs text-muted-foreground bg-muted/20 p-2 rounded">
            ✂️ B&B poda ramas que no pueden mejorar la solución actual
          </div>
        </ControlSection>
      </div>
    </div>
  );
}
