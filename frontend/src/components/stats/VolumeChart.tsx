import type { DailyVolume } from '../../types';

interface VolumeChartProps {
  records: DailyVolume[];
}

function formatVolume(v: number): string {
  if (v >= 1000) return `${(v / 1000).toFixed(1)}k`;
  return String(Math.round(v));
}

export function VolumeChart({ records }: VolumeChartProps) {
  if (records.length === 0) {
    return <p className="text-gray-500 text-sm text-center py-4">データがありません</p>;
  }

  const sorted = [...records].sort((a, b) => a.date.localeCompare(b.date));
  const volumes = sorted.map((r) => Number(r.totalVolume));
  const minV = 0;
  const maxV = Math.ceil(Math.max(...volumes) * 1.1) || 1;
  const range = maxV - minV || 1;

  const width = 600;
  const height = 160;
  const padL = 48;
  const padR = 16;
  const padT = 8;
  const padB = 24;
  const chartW = width - padL - padR;
  const chartH = height - padT - padB;

  const xStep = sorted.length > 1 ? chartW / (sorted.length - 1) : chartW;
  const toY = (v: number) => padT + chartH - ((v - minV) / range) * chartH;
  const toX = (i: number) => padL + (sorted.length > 1 ? i * xStep : chartW / 2);

  const points = sorted.map((r, i) => `${toX(i)},${toY(Number(r.totalVolume))}`).join(' ');

  const yMid = Math.round(maxV / 2);
  const yTicks = [0, yMid, maxV];

  const labelIndices =
    sorted.length === 1 ? [0]
    : sorted.length === 2 ? [0, sorted.length - 1]
    : [0, Math.floor((sorted.length - 1) / 2), sorted.length - 1];

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
            {formatVolume(tick)}
          </text>
        </g>
      ))}

      {/* 折れ線 */}
      {sorted.length > 1 && (
        <polyline
          points={points}
          fill="none"
          stroke="#10b981"
          strokeWidth="2"
          strokeLinejoin="round"
        />
      )}

      {/* データポイント */}
      {sorted.map((r, i) => (
        <g key={r.date}>
          <circle
            cx={toX(i)} cy={toY(Number(r.totalVolume))}
            r="4" fill="#10b981"
          />
          <title>{r.date}: {r.totalVolume}kg</title>
        </g>
      ))}

      {/* X軸ラベル */}
      {labelIndices.map((i) => (
        <text
          key={i}
          x={toX(i)} y={height - 4}
          textAnchor="middle" fontSize="9" fill="#9ca3af"
        >
          {sorted[i].date.slice(5)}
        </text>
      ))}
    </svg>
  );
}
