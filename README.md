# HackerNews Crawler

A Spring Boot application that scrapes the top entries from Hacker News, filters them by title length, and exposes three REST endpoints.

## Features

- **GET `/api/news/top?limit={n}`**  
  Returns the first _n_ entries (rank, title, points, commentCount).

- **GET `/api/news/long?limit={n}`**  
  Only entries whose titles have **> 5** words, ordered by descending comment count.

- **GET `/api/news/short?limit={n}`**  
  Only entries whose titles have **≤ 5** words, ordered by descending points.

## Architecture

- **Hexagonal** (Ports & Adapters)
    - `core` – domain models & port interfaces
    - `application` – DTOs, mappers, services (use-cases)
    - `infrastructure` – Jsoup scraper & Spring MVC controller
    - `exceptions` – cross-cutting error types

## Getting Started

### Prerequisites

- Java 17+
- Gradle
- Internet access to `https://news.ycombinator.com/`

### Build & Run

```bash
./gradlew clean bootRun
