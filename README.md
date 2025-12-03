# ニュースアプリ

> [!IMPORTANT]
> このREADMEはAI生成によるものです。  
> このアプリは WorkManager を利用してみたくて作成したサンプルです。そのため、ニュースアプリとしての機能は必要最小限の実装に留めています。 

KotlinとモダンなAndroid開発技術で構築された、シンプルなニュース閲覧アプリケーションです。  

## ✨ 主な機能

- 📰 最新ニュース記事の一覧表示
- 📄 シンプルで読みやすい記事詳細画面
- 🔗 他のアプリへの記事共有機能
- 💾 オフライン時のための記事キャッシュ機能

※ニュースに関しては、News APIで取得できるコンテンツをもとに表示しているため、本文が表示されない場合があります。

## 🛠️ 技術スタックとアーキテクチャ

このプロジェクトはMVVM (Model-View-ViewModel) アーキテクチャパターンを採用し、以下のモダンなライブラリと技術を使用しています。

- **UI層**: **Jetpack Compose** を全面的に採用し、UIコンポーネントには **Material 3** を使用しています。
- **アーキテクチャ**: **MVVM**
  - **ViewModel**: `androidx.lifecycle.ViewModel` を使用し、UI関連のデータをライフサイクルを意識して管理します。
  - **画面遷移**: **Navigation Compose** を用いて、コンポーザブル間の画面遷移を制御します。
- **DI (Dependency Injection)**: **Hilt** を使用して、アプリ全体の依存関係を管理します。
- **データ層**:
  - **通信**: **Retrofit** を使用してニュースAPIへアクセスし、JSONのパースには **Kotlinx Serialization** を利用しています。
  - **データベース**: **Room** を使用して、ニュース記事をローカルにキャッシュします。
  - **非同期処理**: **Kotlin Coroutines** を用いて、バックグラウンド処理を管理します。
  - **バックグラウンド処理**: **WorkManager** を使用して、定期的なデータのバックグラウンド同期を行います。
- **画像読み込み**: **Coil** を使用して、画像の読み込みと表示を効率的に行います。

## ⚙️ セットアップ方法

このプロジェクトをビルドして実行するには、[NewsAPI.org](https://newsapi.org/) のAPIキーが必要です。

1. リポジトリをクローンします:
   ```bash
   git clone https://github.com/hir05o1/android-news-app.git
   ```

2. プロジェクトのルートディレクトリに `local.properties` ファイルを作成します。

3. `local.properties` ファイルにあなたのNewsAPIキーを追加します:
   ```properties
   NEWS_API_API_KEY="ここにあなたのAPIキーを記述"
   ```

4. Gradleプロジェクトを同期し、アプリを実行します。

## 📸 スクリーンショット

|                   一覧画面                    |                 詳細画面                 |
|:-----------------------------------------:|:------------------------------------:|
| ![記事一覧画面](./images/article-list-view.png) | ![記事詳細画面](./images/article-view.png) |


## 依存関係グラフ
```mermaid
graph TB
    subgraph Application["アプリケーションレベル"]
        App["Application<br/>@HiltAndroidApp"]
        HWF["HiltWorkerFactory"]
    end

    subgraph SingletonComponent["SingletonComponent (Singleton)"]
        subgraph DatabaseModule["DatabaseModule"]
            DB["ArticleDatabase<br/>@Provides"]
            DAO["ArticleDao<br/>@Provides"]
        end
        
        subgraph NetworkModule["NetworkModule"]
            API["NewsApiClient<br/>@Provides"]
        end
    end

    subgraph Repository["Repository層"]
        Repo["NewsApiRepository<br/>@Inject constructor"]
    end

    subgraph ViewModelLayer["ViewModel層"]
        VM1["ArticleListViewModel<br/>@HiltViewModel"]
        VM2["ArticleViewModel<br/>@HiltViewModel"]
    end

    subgraph WorkerLayer["Worker層"]
        Worker["RefreshArticlesWorker<br/>@HiltWorker<br/>@AssistedInject"]
    end

    subgraph ActivityLayer["Activity層"]
        MainActivity["MainActivity<br/>@AndroidEntryPoint"]
    end

    %% Application relationships
    App -.->|provides| HWF

    %% Database relationships
    DB -->|provides| DAO

    %% Repository dependencies
    API -->|injected into| Repo
    DAO -->|injected into| Repo

    %% ViewModel dependencies
    Repo -->|injected into| VM1
    Repo -->|injected into| VM2

    %% Worker dependencies
    Repo -->|injected into| Worker
    HWF -.->|creates| Worker

    %% Activity relationships
    MainActivity -.->|hosts| VM1
    MainActivity -.->|hosts| VM2

    %% Styling
    classDef appClass fill:#e1f5ff,stroke:#01579b,stroke-width:2px
    classDef moduleClass fill:#fff3e0,stroke:#e65100,stroke-width:2px
    classDef repoClass fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    classDef vmClass fill:#e8f5e9,stroke:#1b5e20,stroke-width:2px
    classDef workerClass fill:#fff9c4,stroke:#f57f17,stroke-width:2px
    classDef activityClass fill:#fce4ec,stroke:#880e4f,stroke-width:2px

    class App,HWF appClass
    class DB,DAO,API moduleClass
    class Repo repoClass
    class VM1,VM2 vmClass
    class Worker workerClass
    class MainActivity activityClass
```
```mermaid
graph LR
    subgraph Legend["凡例"]
        A[依存を提供] -->|injected into| B[依存を受け取る]
        C[Factory] -.->|creates/provides| D[インスタンス]
    end

    subgraph Details["詳細な依存関係"]
        NewsApiClient["NewsApiClient"]
        ArticleDao["ArticleDao"]
        ArticleDatabase["ArticleDatabase"]
        
        NewsApiRepository["NewsApiRepository<br/>(@Inject constructor)"]
        
        ArticleListViewModel["ArticleListViewModel"]
        ArticleViewModel["ArticleViewModel"]
        RefreshArticlesWorker["RefreshArticlesWorker"]
        
        ArticleDatabase -->|provides| ArticleDao
        NewsApiClient -->|injected| NewsApiRepository
        ArticleDao -->|injected| NewsApiRepository
        
        NewsApiRepository -->|injected| ArticleListViewModel
        NewsApiRepository -->|injected| ArticleViewModel
        NewsApiRepository -->|injected| RefreshArticlesWorker
    end

    style Legend fill:#f5f5f5,stroke:#999,stroke-width:1px
    style Details fill:#ffffff,stroke:#333,stroke-width:2px
```
