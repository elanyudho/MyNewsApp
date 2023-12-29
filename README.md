# News App

A mobile native application to display news using the [News API](https://newsapi.org).

## User Stories

1. **News Categories:**
   - Create a screen to display the list of news categories. User can choose category by category tab.

2. **News Sources Screen:**
   - Show news sources when the user clicks on one of the news categories.

3. **News Articles Screen:**
   - Show news articles when the user clicks on one of the news sources.

4. **Article Detail Screen:**
   - Show the article detail in a web view when the user clicks on one of the articles.

5. **Search Functionality:**
   - Provide a function to search for news sources and news articles.

6. **Endless Scrolling:**
   - Implement endless scrolling on the news sources and articles screens.

7. **Error Handling:**
   - Cover positive and negative cases to ensure a smooth user experience.

# Tech Stack

- **Kotlin:** The programming language used for the Android app.
- **MVVM Pattern:** Architectural pattern used to separate concerns and improve maintainability.
- **Clean Architecture:** Software design philosophy that separates the concerns of the business logic, application, and infrastructure layers.
- **Hilt:** Dependency injection library for Android, making it easy to manage app components.
- **Room Database:** SQLite object mapping library for local data storage.
- **Coroutines:** Kotlin's native asynchronous programming paradigm for managing background tasks.
- **Modularization:** Project is structured with modularization to enhance maintainability and scalability.
