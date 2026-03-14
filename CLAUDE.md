# TrainTrack - CLAUDE.md

## プロジェクト概要

筋トレ記録管理アプリ（TrainTrack）。個人ユーザーがトレーニング記録を管理し、種目・部位ごとに追跡できる。

## 技術スタック

| 区分 | 技術 |
|------|------|
| フロントエンド | React + TypeScript (Vite, ポート 5173) |
| バックエンド | Spring Boot + Kotlin (ポート 8080) |
| DB | MySQL 8.0 (Docker, ホスト側ポート 3307) |
| 認証 | JWT (30分有効, HS256) + BCrypt |
| 認可 | ロールベース (USER / ADMIN) |
| DBマイグレーション | Flyway (V1〜V5) |

## 起動方法

```bash
docker compose up -d                    # MySQL 起動
cd backend && ./gradlew bootRun         # バックエンド (ポート 8080)
cd frontend && npm run dev              # フロントエンド (ポート 5173)
```

## アーキテクチャ

クリーンアーキテクチャ: `Domain → Application → Infrastructure → Presentation`

```
backend/src/main/kotlin/com/traintrack/
├── domain/
│   ├── model/         # ドメインモデル・値オブジェクト
│   ├── repository/    # リポジトリインターフェース
│   └── exception/     # ドメイン例外 (ForbiddenException など)
├── application/
│   ├── usecase/       # ユースケース
│   └── dto/           # DTO
├── infrastructure/
│   ├── persistence/   # JPA エンティティ・リポジトリ実装
│   ├── security/      # JWT 認証 (JwtProvider, JwtAuthenticationFilter)
│   └── config/        # 設定クラス
└── presentation/
    ├── controller/    # REST コントローラー
    ├── request/       # リクエストクラス
    ├── response/      # レスポンスクラス
    ├── security/      # CurrentUser (認証情報抽出・権限チェック)
    └── exception/     # GlobalExceptionHandler

frontend/src/
├── api/               # API クライアント (adminApi.ts など)
├── components/        # React コンポーネント
├── contexts/          # AuthContext
├── hooks/             # カスタムフック
├── pages/             # ページコンポーネント
└── types/             # TypeScript 型定義 (index.ts)
```

## DB スキーマ

- **users**: id, email, username, password_hash, role (DEFAULT 'USER'), created_at, updated_at
- **body_parts**: id, name, display_order, created_at, updated_at
- **exercises**: id, name, body_part_id (FK), is_active, created_at, updated_at
- **training_records**: id, user_id (FK), exercise_id (FK), weight_kg, rep_count, set_count, training_date, memo, created_at, updated_at

### Flyway マイグレーション履歴

| バージョン | 内容 |
|-----------|------|
| V1 | 初期スキーマ (body_parts, exercises, training_records) |
| V2 | マスターデータ (10部位・35種目) |
| V3 | users テーブル作成 |
| V4 | training_records に user_id 追加 |
| V5 | users に role カラム追加 |

## API エンドポイント

| パス | メソッド | 認証 | 権限 | 説明 |
|------|---------|------|------|------|
| `/api/v1/auth/register` | POST | 不要 | - | ユーザー登録 |
| `/api/v1/auth/login` | POST | 不要 | - | ログイン (JWT 取得) |
| `/api/v1/training-records/**` | GET/POST/PUT/DELETE | 必須 | USER以上 | 記録 CRUD |
| `/api/v1/exercises/` | GET | 不要 | - | 種目一覧 |
| `/api/v1/exercises/**` | POST/PUT/DELETE | 必須 | ADMIN | 種目管理 |
| `/api/v1/body-parts/` | GET | 不要 | - | 部位一覧 |
| `/api/v1/body-parts/**` | POST/PUT/DELETE | 必須 | ADMIN | 部位管理 |
| `/api/v1/admin/users` | GET/POST | 必須 | ADMIN | ユーザー一覧・作成 |
| `/api/v1/admin/users/{id}/role` | PATCH | 必須 | ADMIN | ロール変更 |

## 認証・認可

- JWT クレーム: `subject`(userId), `email`, `role`
- リクエスト時: `JwtAuthenticationFilter` がトークンを抽出し `request.setAttribute()` に設定
- 権限チェック: `CurrentUser.requireAdmin()` — 権限なしは `ForbiddenException` (HTTP 403)

### バリデーション

| 項目 | ルール |
|------|--------|
| メール | RFC 準拠, 255文字以内 |
| ユーザー名 | 3〜50文字, 英数字・`_`・`-` のみ |
| パスワード | 8〜100文字, 英字と数字を含む |

## フロントエンド ページ構成

| ルート | ページ | 認証 |
|--------|--------|------|
| `/` | ホーム・カレンダー | 必須 |
| `/login` | ログイン | 不要 |
| `/register` | ユーザー登録 | 不要 |
| `/exercises` | 種目一覧 (ADMIN は CRUD 可) | 不要 |
| `/records/:date` | 日付別記録詳細 | 必須 |
| `/records/new` | 記録作成 | 必須 |
| `/records/:id/edit` | 記録編集 | 必須 |
| `/admin` | 管理画面 (ADMIN のみ) | 必須 |

### AuthContext

```typescript
interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  isAdmin: boolean;        // user?.role === 'ADMIN'
  login: (user, token) => void;
  logout: () => void;
  getToken: () => string | null;
}
```

localStorage に `accessToken`・`user` を保持。

## 重要ファイル

| ファイル | 役割 |
|---------|------|
| `backend/.../security/JwtProvider.kt` | JWT 生成・検証 (generateToken に role 引数) |
| `backend/.../presentation/security/CurrentUser.kt` | 認証情報取得・requireAdmin() |
| `backend/.../domain/exception/ForbiddenException.kt` | 403 例外 |
| `frontend/src/types/index.ts` | TypeScript 型定義 (UserRole, User, Exercise など) |
| `frontend/src/api/adminApi.ts` | 管理者 API クライアント |
| `frontend/src/pages/AdminPage.tsx` | 管理画面 |
| `frontend/src/contexts/AuthContext.tsx` | 認証コンテキスト |

## 例外処理

`GlobalExceptionHandler` で統一処理。

| 例外 | HTTP |
|------|------|
| `MethodArgumentNotValidException` | 400 |
| `IllegalArgumentException` | 400 |
| `AuthenticationException` | 401 |
| `ForbiddenException` | 403 |
| その他 `Exception` | 500 |

## DB 接続・管理者作成

```bash
# MySQL 接続
mysql -h 127.0.0.1 -P 3307 -u traintrack -ptraintrack traintrack

# 管理者ユーザー昇格 (DB 直接操作)
UPDATE users SET role = 'ADMIN' WHERE email = 'your@email.com';
```

AdminPage からも管理者ユーザーを作成可能（ADMIN のみ）。

## テスト環境

`spring.profiles.active=test` で H2 インメモリ DB (MySQL モード) を使用。Flyway 無効、JPA DDL: `create-drop`。
