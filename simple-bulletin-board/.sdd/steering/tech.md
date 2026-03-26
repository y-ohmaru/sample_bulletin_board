# Technology Stack

## アーキテクチャ
`.sdd/description.md` では、Spring Boot をベースにした Controller / Service / Repository 構成のサーバーサイド Web アプリケーションとして記述されています。画面テンプレートに `posts/list.html` を使用し、データ永続化に H2 Database を利用します。

## 使用技術
### 言語とフレームワーク
- Java: Spring Boot アプリケーションの実装言語として利用予定
- Spring Boot: Web アプリケーション基盤
- Thymeleaf: 一覧画面 `posts/list.html` のテンプレートエンジン
- H2 Database: 投稿データの保存先

### 依存関係
- Spring Boot 関連依存: Web 層、DI、アプリケーション起動基盤
- Thymeleaf 関連依存: HTML テンプレート描画
- H2 関連依存: 組み込みデータベース利用

## 開発環境
### 必要なツール
- Java 実行環境: Spring Boot アプリケーション実行に必要
- ビルドツール: Maven または Gradle を想定するが、このリポジトリでは未確認

### よく使うコマンド
- 起動: 未確認
- テスト: 未確認
- ビルド: 未確認

## 環境変数
- 現時点で確認できる環境変数定義はありません
