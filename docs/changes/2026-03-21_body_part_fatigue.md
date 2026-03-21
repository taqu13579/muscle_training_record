# 部位別疲労表示機能

## 概要

ホーム画面に「部位別疲労度」セクションを追加する。
直近3日間のトレーニングボリューム（重量 × 回数 × セット数）を部位ごとに集計し、
過去90日の最大値に対する割合をプログレスバーで表示する。

## 背景・目的

筋トレの効果的な回復管理のため、各部位がどれだけ疲労しているかを一目で確認できるようにする。
次のトレーニングメニュー選択時に、どの部位を鍛えるべきか・休めるべきかの判断材料とする。

## 確定仕様

| 項目 | 内容 |
|------|------|
| 疲労の定義 | 直近3日間のボリューム（重量kg × 回数 × セット数）累積 |
| 補助筋 | 含める（`exercise.auxiliaryMuscles` も対象） |
| 表示場所 | ホーム画面（BodyWeightSection の下） |
| UI表現 | プログレスバー（0〜100%、色分けあり） |
| 色分け | 0-39% = 緑（回復済み）/ 40-69% = 黄（疲労中）/ 70%以上 = 赤（高疲労） |
| 100%基準 | 過去90日間の3日間ウィンドウ中の最大ボリューム |
| 表示部位 | 全10部位を常に表示（未トレーニング部位は 0%） |

## バックエンド変更

### 新規ファイル

| ファイル | 内容 |
|---------|------|
| `backend/.../application/dto/BodyPartFatigueDto.kt` | 部位別疲労 DTO |
| `backend/.../application/usecase/training/GetBodyPartFatigueUseCase.kt` | 疲労計算ユースケース |
| `backend/.../presentation/response/BodyPartFatigueResponse.kt` | APIレスポンスクラス |

### 変更ファイル

| ファイル | 変更内容 |
|---------|---------|
| `backend/.../presentation/controller/TrainingRecordController.kt` | `GET /api/v1/training-records/stats/fatigue` エンドポイント追加 |

### 疲労計算ロジック

1. 過去90日のトレーニング記録を取得（exercise・bodyPart・auxiliaryMuscles をeager load）
2. 各レコードについて、主要部位 + 補助筋すべてにボリュームを加算
3. 全部位を取得し、各部位について以下を計算:
   - `currentVolume`: 直近3日間（今日〜2日前）のボリューム合計
   - `maxVolume`: 過去90日間における最大3日間ウィンドウのボリューム合計
   - `fatiguePercentage`: `min(currentVolume / maxVolume * 100, 100)` (maxVolume=0の場合は0)

## フロントエンド変更

### 新規ファイル

| ファイル | 内容 |
|---------|------|
| `frontend/src/hooks/useBodyPartFatigue.ts` | 疲労データフェッチフック |
| `frontend/src/components/fatigue/FatigueSection.tsx` | 疲労表示セクションコンポーネント |

### 変更ファイル

| ファイル | 変更内容 |
|---------|---------|
| `frontend/src/types/index.ts` | `BodyPartFatigue` インターフェース追加 |
| `frontend/src/api/trainingRecordApi.ts` | `getBodyPartFatigue()` 関数追加 |
| `frontend/src/pages/HomePage.tsx` | `FatigueSection` を BodyWeightSection の下に追加 |

## APIエンドポイント

```
GET /api/v1/training-records/stats/fatigue
認証: 必須 (USER以上)

レスポンス例:
[
  {
    "bodyPartId": 1,
    "bodyPartName": "胸",
    "currentVolume": 5400.00,
    "maxVolume": 7200.00,
    "fatiguePercentage": 75
  },
  ...
]
```
