import type { BodyWeight } from '../../types';

interface BodyWeightChartProps {
  records: BodyWeight[];
}

export function BodyWeightChart({ records }: BodyWeightChartProps) {
  if (records.length === 0) {
    return <p className="text-gray-500 text-sm text-center py-4">データがありません</p>;
  }

  const sorted = [...records].sort((a, b) => a.recordedDate.localeCompare(b.recordedDate));
  const weights = sorted.map((r) => Number(r.weightKg));
  const minW = Math.floor(Math.min(...weights) - 1);
  const maxW = Math.ceil(Math.max(...weights) + 1);
  const range = maxW - minW || 1;

  const width = 600;
  const height = 160;
  const padL = 40;
  const padR = 16;
  const padT = 8;
  const padB = 24;
  const chartW = width - padL - padR;
  const chartH = height - padT - padB;

  const xStep = sorted.length > 1 ? chartW / (sorted.length - 1) : chartW;
  const toY = (w: number) => padT + chartH - ((w - minW) / range) * chartH;
  const toX = (i: number) => padL + (sorted.length > 1 ? i * xStep : chartW / 2);

  const points = sorted.map((r, i) => `${toX(i)},${toY(Number(r.weightKg))}`).join(' ');

  // Y軸目盛り（3本）
  const yTicks = [minW, Math.round((minW + maxW) / 2), maxW];

  return (
    <svg viewBox={`0 0 ${width} ${height}`} className="w-full" style={{ maxHeight: 160 }}>
      {/* Y軸目盛り */}
      {yTicks.map((tick) => (
        <g key={tick}>
          <line
            x1={padL} y1={toY(tick)} x2={width - padR} y2={toY(tick)}
            stroke="#e5e7eb" strokeWidth="1"
          />
          <text x={padL - 4} y={toY(tick) + 4} textAnchor="end" fontSize="10" fill="#6b7280">
            {tick}
          </text>
        </g>
      ))}

      {/* 折れ線 */}
      {sorted.length > 1 && (
        <polyline
          points={points}
          fill="none"
          stroke="#3b82f6"
          strokeWidth="2"
          strokeLinejoin="round"
        />
      )}

      {/* データポイント */}
      {sorted.map((r, i) => (
        <g key={r.id}>
          <circle
            cx={toX(i)} cy={toY(Number(r.weightKg))}
            r="4" fill="#3b82f6"
          />
          <title>{r.recordedDate}: {r.weightKg}kg</title>
        </g>
      ))}

      {/* X軸ラベル（最初・最後・中間） */}
      {sorted.length > 0 && (() => {
        const indices =
          sorted.length === 1 ? [0]
          : sorted.length === 2 ? [0, sorted.length - 1]
          : [0, Math.floor((sorted.length - 1) / 2), sorted.length - 1];
        return indices.map((i) => (
          <text
            key={i}
            x={toX(i)} y={height - 4}
            textAnchor="middle" fontSize="9" fill="#9ca3af"
          >
            {sorted[i].recordedDate.slice(5)}
          </text>
        ));
      })()}
    </svg>
  );
}
