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
    const movieName = movies.find(m => m.peliculaId === selectedMovie)?.titulo || '';
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
    const movieName = movies.find(m => m.peliculaId === selectedMovie)?.titulo || '';
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
      `${API_URL}/camino/${startMovie}/${endMovie}`,
      'Camino Más Corto Entre Películas',
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
            onClick={handleDijkstra}
            label="Dijkstra"
            icon="🟢"
            variant="dijkstra"
          />
        </ControlSection>
      </div>

      {/* Other Algorithms */}
      <div className="bg-card rounded-xl p-6 shadow-lg border border-border">
        <h2 className="text-xl font-bold text-primary mb-4">⚡ Otros Algoritmos (3 pts)</h2>
        <div className="bg-muted/30 border-l-4 border-primary p-3 mb-4 rounded text-sm text-muted-foreground">
          Greedy (1pt) + QuickSort (1pt) + Backtracking (1pt)
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
    </div>
  );
}
