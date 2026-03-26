# Bulletin Board App

Spring Bootを使用して作成したシンプルな掲示板アプリです。

---

## 📝 概要
投稿の一覧表示・登録・削除ができる基本的なCRUDアプリケーションです。  
Spec Driven Development（SDD）ベースで設計・実装を行っています。

---

## 🚀 機能
- 投稿一覧表示
- 投稿登録
- 投稿削除
- バリデーション

---

## 🛠 技術スタック
- Java 21
- Spring Boot
- Thymeleaf
- H2 Database
- JUnit / Mockito

---

## 🧪 テスト
以下のテストを実装しています。

- フォームバリデーションテスト
- サービステスト（Mockito）
- コントローラテスト
- 統合テスト

---

## 📂 ディレクトリ構成
```
src/
 ├─ controller
 ├─ service
 ├─ entity
 ├─ form
 └─ repository

.sdd/ （設計資料）
```

---

## 🧠 設計について
本プロジェクトは Spec Driven Development をベースに開発しています。  
設計資料は `.sdd/` 配下に格納しています。

---

## ▶️ 起動方法
1. Mavenでビルド
```
mvn clean install
```

2. アプリケーション起動
```
mvn spring-boot:run
```

3. ブラウザでアクセス
```
http://localhost:8080/posts
```

---

## ⚠️ 注意
- H2コンソールは開発用途のみ想定しています
- 認証機能は未実装です

---

## 📌 今後の改善予定
- 更新機能の追加
- ログイン機能（Spring Security）
- ページング対応
