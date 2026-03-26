# Project Structure

## ルートディレクトリ構成
```text
/
└── .sdd/
    ├── description.md   # 実現したい機能の記述
    ├── README.md        # SDD フロー説明
    ├── specs/           # 仕様関連ファイル格納ディレクトリ
    ├── steering/        # ステアリングドキュメント格納ディレクトリ
    └── target-spec.txt  # 対象仕様メモ
```

## コード構成パターン
現時点のリポジトリにはアプリケーション本体のソースコードやビルド設定ファイルは存在せず、SDD 用ドキュメントのみが配置されています。.sdd/description.md には、実装対象として Controller / Service / Repository 構成を採用する方針が記載されています。

## ファイル命名規則
- SDD 文書: `.sdd/` 配下に Markdown ファイルとして配置
- ステアリング文書: `.sdd/steering/` 配下に `product.md`、`tech.md`、`structure.md` を配置

## 主要な設計原則
- 事実ベース記述: 現在のファイル実態と `.sdd/description.md` に基づいて記述する
- 構成分離: プロダクト概要、技術情報、構造情報を別ファイルで管理する
- SDD 前提管理: 実装前の要件と設計作業を `.sdd/` 配下で段階的に整理する
