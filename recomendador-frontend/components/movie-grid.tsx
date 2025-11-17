import { Movie } from '@/app/page';

interface MovieGridProps {
  movies: Movie[];
}

export default function MovieGrid({ movies }: MovieGridProps) {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-2 gap-4">
      {movies.map((movie, index) => (
        <div
          key={movie.peliculaId ? `${movie.peliculaId}-${index}` : `movie-${index}`}
          className="bg-gradient-to-br from-slate-700 to-slate-800 rounded-lg p-4 hover:shadow-lg transition-all duration-300 transform hover:scale-105 border border-border"
        >
          <h3 className="font-bold text-lg text-foreground mb-2">{movie.titulo}</h3>
          <div className="space-y-1 text-sm text-muted-foreground mb-3">
            <div>📅 {movie.año}</div>
            <div>⏱️ {movie.duracion} min</div>
          </div>
          <div className="flex items-center justify-between mb-3">
            <span className="bg-yellow-500 text-black px-2 py-1 rounded font-bold text-sm">
              ⭐ {movie.promedioRating}
            </span>
          </div>

          {movie.actores && movie.actores.length > 0 && (
            <div className="mb-3">
              <p className="text-xs font-semibold text-foreground mb-1">🎭 Actores:</p>
              <p className="text-xs text-muted-foreground line-clamp-2">
                {movie.actores.map(actor => actor.nombre).join(', ')}
              </p>
            </div>
          )}

          {movie.generos && movie.generos.length > 0 && (
            <div>
              <p className="text-xs font-semibold text-foreground mb-1">🎬 Géneros:</p>
              <div className="flex flex-wrap gap-1">
                {movie.generos.map((genre, idx) => (
                  <span
                    key={`${genre.nombre}-${idx}`}
                    className="bg-primary/30 text-primary px-2 py-0.5 rounded-full text-xs font-medium"
                  >
                    {genre.nombre}
                  </span>
                ))}
              </div>
            </div>
          )}
        </div>
      ))}
    </div>
  );
}
