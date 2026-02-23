// 部位
export interface BodyPart {
  id: number;
  name: string;
  displayOrder: number;
}

// 種目
export interface Exercise {
  id: number;
  name: string;
  bodyPart: BodyPart | null;
  isActive: boolean;
}

export interface CreateExerciseRequest {
  name: string;
  bodyPartId: number;
}

export interface UpdateExerciseRequest {
  name: string;
}

// トレーニング記録
export interface TrainingRecord {
  id: number;
  exercise: Exercise | null;
  weightKg: number;
  repCount: number;
  setCount: number;
  trainingDate: string;
  memo: string | null;
  totalVolume: number;
  createdAt: string | null;
}

export interface CreateTrainingRecordRequest {
  exerciseId: number;
  weightKg: number;
  repCount: number;
  setCount: number;
  trainingDate: string;
  memo?: string;
}

export interface UpdateTrainingRecordRequest {
  exerciseId: number;
  weightKg: number;
  repCount: number;
  setCount: number;
  trainingDate: string;
  memo?: string;
}

// ページネーション
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

// カレンダー
export interface CalendarDay {
  date: string;
  recordCount: number;
  bodyParts: string[];
}

export interface CalendarResponse {
  yearMonth: string;
  days: CalendarDay[];
}
