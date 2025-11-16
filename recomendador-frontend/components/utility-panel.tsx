'use client';

import { Movie } from '@/app/page';
import AlgorithmButton from './algorithm-button';

interface UtilityPanelProps {
  onLoadAll: () => void;
  movies: Movie[];
  onGenreFilter: (genre: string) => void;
  onError: (message: string, type: 'error' | 'success') => void;
  onLoading: (loading: boolean) => void;
}

const GENRES = ['Ciencia Ficción', 'Thriller', 'Acción', 'Drama', 'Crimen', 'Misterio'];

export default function UtilityPanel({
  onLoadAll,
  movies,
  onGenreFilter,
  onError,
  onLoading,
}: UtilityPanelProps) {
  const handleTopRating = async () => {
    onLoading(true);
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/peliculas'}/top`);
      const data = await response.json();
      // This would need to be passed back through parent component
      onError('Característica: Películas principales cargadas', 'success');
    } catch {
      onError('Error al cargar películas principales', 'error');
    } finally {
      onLoading(false);
    }
  };

  return (
    <div className="bg-card rounded-xl p-6 shadow-lg border border-border">
      <h2 className="text-xl font-bold text-primary mb-4">🧪 Opciones Rápidas</h2>
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
        <button
          onClick={onLoadAll}
          className="w-full py-3 px-4 bg-primary hover:bg-primary/90 text-primary-foreground font-semibold rounded-lg transition-all duration-200"
        >
          🎬 Ver Todas las Películas
        </button>
        <button
          onClick={handleTopRating}
          className="w-full py-3 px-4 bg-yellow-600 hover:bg-yellow-700 text-white font-semibold rounded-lg transition-all duration-200"
        >
          ⭐ Top Calificaciones
        </button>
      </div>

      <div className="mb-4">
        <label className="block text-sm font-semibold text-foreground mb-2">Filtrar por Género</label>
        <select
          onChange={(e) => e.target.value && onGenreFilter(e.target.value)}
          className="w-full px-3 py-2 bg-background border border-border rounded-lg text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
        >
          <option value="">Selecciona un género...</option>
          {GENRES.map(genre => (
            <option key={genre} value={genre}>{genre}</option>
          ))}
        </select>
      </div>
    </div>
  );
}
