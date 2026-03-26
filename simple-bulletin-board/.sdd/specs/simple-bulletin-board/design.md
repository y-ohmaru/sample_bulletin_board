# 技術設計書

## アーキテクチャ概要
本機能は、`.sdd/steering/tech.md` に記載された Spring Boot の Controller / Service / Repository 構成へ統合する。HTTP リクエストは Controller で受け取り、投稿の取得・登録・削除と入力検証後の処理を Service に委譲する。投稿データの永続化は Repository 経由で H2 Database に行い、画面表示は Thymeleaf テンプレート `posts/list.html` を用いて実装する。アプリ起動時に投稿テーブルが利用可能な状態になるよう、JPA のエンティティ定義またはテーブル自動作成設定を前提とする。

## 主要コンポーネント
### コンポーネント1：PostController
- 責務：`/posts` の一覧表示、投稿登録、投稿削除の HTTP エンドポイントを提供する
- 入力：GET リクエスト、投稿フォームの `name` と `content`、削除対象の投稿 ID
- 出力：投稿一覧とフォーム状態を含む `posts/list` ビュー、成功時のリダイレクト、バリデーションエラー時の画面再表示
- 依存関係：PostService、投稿フォームオブジェクト、Thymeleaf テンプレート

### コンポーネント2：PostService
- 責務：投稿一覧取得、投稿登録、投稿削除、登録前のビジネスルール適用を担当する
- 入力：投稿名、投稿内容、削除対象 ID
- 出力：投稿一覧、保存済み投稿、削除完了結果、バリデーション済み処理結果
- 依存関係：PostRepository、Post エンティティ

### コンポーネント3：PostRepository
- 責務：投稿データの保存、一覧取得、削除などの永続化操作を担当する
- 入力：Post エンティティ、投稿 ID
- 出力：保存済み Post、投稿一覧、削除対象の検索結果
- 依存関係：H2 Database、Post エンティティ、Spring Data JPA 相当の永続化基盤

### コンポーネント4：Post
- 責務：掲示板投稿を表す永続化エンティティとしてデータ構造を定義する
- 入力：名前、内容、作成日時
- 出力：データベースに保存される投稿レコード、画面表示用の投稿情報
- 依存関係：JPA マッピング設定、H2 の投稿テーブル

### コンポーネント5：PostForm
- 責務：投稿フォーム入力を受け取り、必須入力と文字数制限の検証対象を表現する
- 入力：画面から送信された `name`、`content`
- 出力：Controller が扱うフォームデータ、バリデーションエラー情報
- 依存関係：Bean Validation、PostController

### コンポーネント6：posts/list.html
- 責務：投稿一覧、投稿入力フォーム、削除操作、エラーメッセージを 1 画面に表示する
- 入力：投稿一覧、フォーム入力値、バリデーションメッセージ
- 出力：ブラウザへ返す HTML
- 依存関係：Thymeleaf、PostController が渡すモデル属性

## データモデル
### Post
- `id`：`Long`、投稿を一意に識別する ID
- `name`：`String`、投稿者名。必須
- `content`：`String`、投稿本文。必須、100文字以内
- `createdAt`：`LocalDateTime`、投稿作成日時

### PostForm
- `name`：`String`、フォーム入力された投稿者名
- `content`：`String`、フォーム入力された投稿本文

### データベーススキーマ
- テーブル名：`posts`
- 主キー：`id`
- カラム：`id`、`name`、`content`、`created_at`
- 制約：`name` と `content` は空でないことをアプリケーション側で検証する

## 処理フロー
1. 利用者が `GET /posts` にアクセスすると、PostController が PostService を通じて投稿一覧を取得する。
2. PostController は空の PostForm と投稿一覧をモデルに格納し、`posts/list.html` を返す。
3. 利用者が投稿フォームを送信すると、PostController が `name` と `content` を受け取り、PostForm に対してバリデーションを実行する。
4. 入力が不正な場合は、エラーメッセージと入力値を保持したまま `posts/list.html` を再表示する。
5. 入力が妥当な場合は、PostService が Post エンティティを生成し、PostRepository 経由で H2 Database に保存する。
6. 保存完了後、PostController は `GET /posts` へリダイレクトし、更新後の一覧を表示する。
7. 利用者が削除操作を行うと、PostController が対象投稿 ID を受け取り、PostService を通じて削除処理を実行する。
8. 削除完了後、PostController は `GET /posts` へリダイレクトし、対象投稿が除外された一覧を表示する。

## エラーハンドリング
- 名前未入力：投稿を保存せず、名前が必須である旨のメッセージを画面表示する
- 内容未入力：投稿を保存せず、内容が必須である旨のメッセージを画面表示する
- 内容が100文字超過：投稿を保存せず、文字数上限を超えた旨のメッセージを画面表示する
- 削除対象が存在しない：削除処理で例外を避け、一覧へ戻すか、対象が存在しない旨を扱える設計とする
- データベース初期化失敗：アプリ起動時にテーブルを利用できない場合は起動失敗として検知できる構成にする

## 既存コードとの統合
- 変更が必要なファイル：
  - 現時点では既存のアプリケーションコードは確認できていないため、変更対象ファイルは未確定

- 新規作成ファイル：
  - `src/main/java/.../controller/PostController.java`：投稿一覧、登録、削除のエンドポイントを提供する
  - `src/main/java/.../service/PostService.java`：投稿の取得、登録、削除の業務処理を担当する
  - `src/main/java/.../repository/PostRepository.java`：投稿データの永続化アクセスを担当する
  - `src/main/java/.../entity/Post.java`：投稿エンティティを定義する
  - `src/main/java/.../form/PostForm.java`：入力フォームとバリデーション定義を持つ
  - `src/main/resources/templates/posts/list.html`：投稿一覧と入力フォーム画面を表示する
  - `src/main/resources/application.properties` または `application.yml`：H2 とテーブル自動作成設定を定義する
