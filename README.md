# Cancer Type Classifier based on Random Forest Algorithm

## Описание проекта (Abstract)
Данный проект представляет собой программную реализацию классификатора типов рака на основе данных экспрессии генов. Ключевой особенностью является **собственная реализация алгоритма Случайного Леса (Random Forest) и Деревьев Решений на языке Java** без использования сторонних высокоуровневых ML-библиотек (вроде Weka или Deeplearning4j для ядра алгоритма).

Проект разработан в рамках курсовой работы по направлению "Прикладная математика" (КФУ).

Система интегрирована в веб-сервис на базе **Spring Boot**, предоставляющий REST API для загрузки данных пациентов и получения диагноза.

## Технологический стек
*   **Язык:** Java 17+
*   **Фреймворк:** Spring Boot (Web, REST API)
*   **Сборка:** Maven
*   **Архитектура:** Service-Oriented Architecture (SOA)
*   **Данные:** CSV (TCGA-PANCAN dataset)

## Функциональность
1.  **Парсинг данных:** Загрузка и обработка наборов данных TCGA-PANCAN (экспрессия генов RNA-Seq).
2.  **Обучение модели:**
    *   Построение Деревьев Решений (Decision Tree) с использованием критерия **Gini Impurity**.
    *   Реализация ансамблевого метода **Random Forest** (бэггинг + метод случайных подпространств).
3.  **Классификация:** Определение типа опухоли по 5 классам:
    *   BRCA (Рак молочной железы)
    *   KIRC (Светлоклеточный рак почки)
    *   COAD (Аденокарцинома толстой кишки)
    *   LUAD (Аденокарцинома легких)
    *   PRAD (Рак предстательной железы)
4.  **API:** Прием файлов `.csv` через HTTP-запросы и возврат JSON-ответа с предсказанием.

## Структура проекта
*   `src/main/java/com/kpfu/suyundukov/cancer_classifier/`
    *   `algorithms/` — Ядро ML. Классы `DecisionTreeClassifier`, `RandomForestClassifier`, `TreeNode`. Реализация математической логики (расчет энтропии, прироста информации).
    *   `controller/` — `AnalyticsController` для обработки HTTP-запросов.
    *   `service/` — Бизнес-логика (`PredictionService`, `DataProcessingService`), отвечающая за подготовку данных и запуск алгоритмов.
    *   `repository/` — Слой доступа к данным (чтение CSV).
    *   `model/` — DTO и сущности (`CancerSample`, `CancerType`).

## Установка и запуск

### Предварительные требования
*   JDK 17 или выше
*   Maven

### Запуск
1.  Клонируйте репозиторий:
    ```bash
    git clone https://github.com/your-username/cancer-type-classifier.git
    ```
2.  Перейдите в папку проекта и соберите его:
    ```bash
    ./mvnw clean install
    ```
3.  Запустите приложение:
    ```bash
    ./mvnw spring-boot:run
    ```

### Использование API
**Endpoint:** `POST /api/analyze` (пример)

**Request Body:** `multipart/form-data` (file: data.csv)

**Response:**
```json
{
  "sampleId": "sample-001",
  "predictedType": "BRCA",
  "confidence": 0.95
}
```