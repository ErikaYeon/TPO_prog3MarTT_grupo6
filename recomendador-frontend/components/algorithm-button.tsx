interface AlgorithmButtonProps {
  onClick: () => void;
  label: string;
  icon: string;
  variant: 'bfs' | 'dfs' | 'dijkstra' | 'greedy' | 'quicksort' | 'backtracking';
}

const variantStyles = {
  bfs: 'bg-blue-600 hover:bg-blue-700 text-white',
  dfs: 'bg-purple-600 hover:bg-purple-700 text-white',
  dijkstra: 'bg-green-600 hover:bg-green-700 text-white',
  greedy: 'bg-yellow-500 hover:bg-yellow-600 text-black',
  quicksort: 'bg-red-600 hover:bg-red-700 text-white',
  backtracking: 'bg-teal-600 hover:bg-teal-700 text-white',
};

export default function AlgorithmButton({
  onClick,
  label,
  icon,
  variant,
}: AlgorithmButtonProps) {
  return (
    <button
      onClick={onClick}
      className={`w-full py-2 px-3 rounded-lg font-semibold transition-all duration-200 transform hover:scale-105 active:scale-95 ${variantStyles[variant]}`}
    >
      {icon} {label}
    </button>
  );
}
