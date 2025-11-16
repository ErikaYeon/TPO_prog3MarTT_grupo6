'use client';

import { AlgorithmResult, Movie } from '@/app/page';
import MovieGrid from './movie-grid';

interface ResultsPanelProps {
  result: AlgorithmResult | null;
  isLoading: boolean;
}

const algorithmColors = {
  BFS: 'bg-blue-600',
  DFS: 'bg-purple-600',
  DIJKSTRA: 'bg-green-600',
  GREEDY: 'bg-yellow-500',
  QUICKSORT: 'bg-red-600',
  BACKTRACKING: 'bg-teal-600',
  browse: 'bg-slate-600',
  filter: 'bg-indigo-600',
};

export default function ResultsPanel({ result, isLoading }: ResultsPanelProps) {
  return (
    <div className="bg-card rounded-xl p-6 shadow-lg border border-border">
      <div className="mb-6">
        <div className="flex items-center gap-3 mb-4">
          <h2 className="text-2xl font-bold text-foreground">
            {result?.title || '🎥 Películas'}
          </h2>
          {result?.algorithm && (
            <span className={`px-3 py-1 rounded-full text-sm font-bold text-white ${algorithmColors[result.algorithm as keyof typeof algorithmColors] || 'bg-slate-600'}`}>
              {result.algorithm}
            </span>
          )}
        </div>
      </div>

      {isLoading ? (
        <div className="text-center py-12">
          <div className="inline-block animate-spin">⏳</div>
          <p className="text-muted-foreground mt-2">Cargando...</p>
        </div>
      ) : result && result.movies && result.movies.length > 0 ? (
        <MovieGrid movies={result.movies} />
      ) : (
        <div className="text-center py-12">
          <p className="text-muted-foreground">👈 Selecciona un algoritmo para ver resultados</p>
        </div>
      )}
    </div>
  );
}
