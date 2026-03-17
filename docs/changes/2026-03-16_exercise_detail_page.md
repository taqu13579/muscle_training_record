# 種目詳細ページ追加

## 概要

各種目のフォーム・補助筋情報を閲覧できる `/exercises/:id` ページを追加。
ADMIN が管理画面（`/admin` > 種目管理タブ）から説明・補助筋を編集できる。

## DB変更

- `exercises.description` (TEXT, NULL) カラム追加
- `exercise_auxiliary_muscles` 中間テーブル新設（PK: exercise_id + body_part_id）
- マイグレーション: V7

## バックエンド変更

| ファイル | 変更内容 |
|---------|---------|
| `V7__add_exercise_detail.sql` | 新規マイグレーション |
| `Exercise.kt` | `description`, `auxiliaryMuscles`, `updateDetail()` 追加 |
| `ExerciseEntity.kt` | `description` カラム, `@ManyToMany auxiliaryMuscles` 追加 |
| `ExerciseMapper.kt` | `description`, `auxiliaryMuscles` マッピング追加 |
| `ExerciseDto.kt` | `description`, `auxiliaryMuscles` フィールド追加 |
| `ExerciseResponse.kt` | `description`, `auxiliaryMuscles` フィールド追加 |
| `UpdateExerciseRequest.kt` | `description`, `auxiliaryMuscleBodyPartIds` 追加 |
| `UpdateExerciseUseCase.kt` | `description` / `auxiliaryMuscles` 更新対応 |
| `GetExerciseUseCase.kt` | 新規: 単一種目取得 |
| `ExerciseController.kt` | `GET /api/v1/exercises/{id}` 追加, PUT 更新 |
| `ExerciseRepositoryImpl.kt` | `auxiliaryMuscles` の save 対応 |

## フロントエンド変更

| ファイル | 変更内容 |
|---------|---------|
| `types/index.ts` | `Exercise` に `description`, `auxiliaryMuscles` 追加; `UpdateExerciseRequest` 拡張 |
| `api/exerciseApi.ts` | `getById()` 追加 |
| `pages/ExerciseDetailPage.tsx` | 新規: 詳細ページ |
| `pages/ExercisesPage.tsx` | 種目名を `/exercises/:id` リンクに変更 |
| `pages/AdminPage.tsx` | 種目管理タブ追加（説明・補助筋編集フォーム） |
| `App.tsx` | `/exercises/:id` ルート追加 |
