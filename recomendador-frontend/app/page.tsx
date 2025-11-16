'use client';

import { useState, useEffect } from 'react';
import Header from '@/components/header';
import AlgorithmPanel from '@/components/algorithm-panel';
import ResultsPanel from '@/components/results-panel';
import UtilityPanel from '@/components/utility-panel';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/peliculas';

export interface Movie {
  peliculaId: string;
  titulo: string;
  año: number;
  duracion: number;
  promedioRating: number;
  generos: Array<{ nombre: string }>;
  actores?: Array<{ nombre: string }>;
}

export interface AlgorithmResult {
  title: string;
  algorithm: string;
  movies: Movie[];
  info?: string;
}

export default function Home() {
  const [movies, setMovies] = useState<Movie[]>([]);
  const [results, setResults] = useState<AlgorithmResult | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [status, setStatus] = useState<{ message: string; type: 'success' | 'error' } | null>(null);
  const [selectedGenre, setSelectedGenre] = useState('');

  const showStatus = (message: string, type: 'success' | 'error' = 'success') => {
    setStatus({ message, type });
    setTimeout(() => setStatus(null), 3000);
  };

  const loadMovies = async () => {
    setIsLoading(true);
    try {
      const response = await fetch(`${API_URL}`);
      const data = await response.json();
      setMovies(data);
      setResults({
        title: 'Todas las Películas',
        algorithm: 'browse',
        movies: data,
      });
    } catch (error) {
      showStatus('Error cargando películas', 'error');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadMovies();
  }, []);

  const handleAlgorithmResult = (result: AlgorithmResult) => {
    setResults(result);
  };

  const handleGenreFilter = async (genre: string) => {
    if (!genre) return;
    setIsLoading(true);
    try {
      const response = await fetch(`${API_URL}/genero/${encodeURIComponent(genre)}`);
      const data = await response.json();
      setResults({
        title: `Género: ${genre}`,
        algorithm: 'filter',
        movies: data,
      });
    } catch (error) {
      showStatus('Error filtrando por género', 'error');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <main className="min-h-screen bg-background">
      <Header status={status} />

      <div className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-6">
          {/* Algorithm Panels */}
          <AlgorithmPanel
            movies={movies}
            onResult={handleAlgorithmResult}
            onError={showStatus}
            onLoading={setIsLoading}
          />

          {/* Results */}
          <div className="lg:col-span-2">
            <ResultsPanel
              result={results}
              isLoading={isLoading}
            />
          </div>
        </div>

        {/* Utility Panel */}
        <UtilityPanel
          onLoadAll={loadMovies}
          movies={movies}
          onGenreFilter={handleGenreFilter}
          onError={showStatus}
          onLoading={setIsLoading}
        />
      </div>
    </main>
  );
}
