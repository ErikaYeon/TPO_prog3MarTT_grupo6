export function ControlSection({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div className="mb-4">
      <h3 className="text-sm font-semibold text-primary mb-3">{label}</h3>
      <div className="space-y-2">{children}</div>
    </div>
  );
}
