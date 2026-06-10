# Training App Portfolio

筋トレ記録アプリケーション

## 概要

Java + Spring Boot + React を使用した筋トレ記録アプリです。
ユーザーが日々の筋トレ記録を管理し、進捗を可視化できます。

## 技術スタック

### バックエンド
- **言語**：Java 17
- **フレームワーク**：Spring Boot 3.2
- **データベース**：PostgreSQL 16
- **認証**：JWT（JSON Web Token）
- **テスト**：JUnit 5

### フロントエンド（7月実装予定）
- **ライブラリ**：React
- **スタイリング**：Tailwind CSS
- **チャート**：Recharts

## データベース設計

### ER図

## 機能一覧

### 実装済み
- [x] 環境構築
- [x] Spring Boot プロジェクト初期化
- [x] Hello World API

### 実装中（6月）
- [ ] User Entity・Repository（6/17）
- [ ] TrainingRecord Entity・Repository（6/18-19）
- [ ] Service 層実装（6/22）
- [ ] Controller 実装（6/23）
- [ ] CRUD API 完成（6/23）
- [ ] 認証機能（JWT）（6/24-30）
- [ ] テストコード作成（7/1-7）

### 実装予定（7月）
- [ ] React フロントエンド
- [ ] グラフ表示機能
- [ ] Docker 化
- [ ] Render へのデプロイ

## セットアップ方法

### 前提条件
- Java 17 以上
- Maven 3.6 以上
- PostgreSQL 16 以上

### インストール手順

```bash
# 1. リポジトリをクローン
git clone git@github.com:Takemura-dev/training-app-portfolio.git
cd training-app

# 2. PostgreSQL で training_app データベースを作成
createdb -U postgres training_app

# 3. Spring Boot を起動
mvn spring-boot:run
```

### 動作確認

```bash
# ブラウザで以下にアクセス
http://localhost:8080/

# 出力：Hello World! Training App is running!
```

## API エンドポイント（実装予定）

### 認証
- `POST /api/auth/register` - ユーザー登録
- `POST /api/auth/login` - ログイン

### 筋トレ記録
- `GET /api/records` - 全記録取得
- `POST /api/records` - 記録作成
- `PUT /api/records/{id}` - 記録更新
- `DELETE /api/records/{id}` - 記録削除

## 開発進捗

| フェーズ | 期間 | 状態 |
|---------|------|------|
| 環境構築 | 6/9-6/16 | 進行中 |
| バックエンド | 6/17-7/7 | 予定中 |
| フロントエンド | 7/8-7/14 | 予定中 |
| デプロイ | 7/15+ | 予定中 |

## 作成者

竹村　駿人（Takemura　Hayato）
- GitHub: https://github.com/Takemura-dev

## ライセンス

MIT License
