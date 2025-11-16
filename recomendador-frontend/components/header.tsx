interface HeaderProps {
  status?: { message: string; type: 'success' | 'error' } | null;
}

export default function Header({ status }: HeaderProps) {
  return (
    <header className="bg-gradient-to-r from-primary via-purple-600 to-blue-600 text-white py-12 px-4 shadow-lg">
      <div className="container mx-auto text-center">
        <h1 className="text-4xl md:text-5xl font-bold mb-2 drop-shadow-lg">
          🎬 Recomendador de Películas
        </h1>
        <p className="text-lg md:text-xl opacity-90 mb-4">
          Trabajo Práctico Obligatorio de Programación III
        </p>
        
      </div>

      {status && (
        <div className={`mt-4 mx-auto max-w-md p-4 rounded-lg text-center font-medium ${
          status.type === 'success'
            ? 'bg-green-500/20 text-green-200 border border-green-500'
            : 'bg-red-500/20 text-red-200 border border-red-500'
        }`}>
          {status.message}
        </div>
      )}
    </header>
  );
}
