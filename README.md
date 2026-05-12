# Online Shop

## О проекте

Backend pet-project интернет-магазина электроники, разработанный на Spring Boot.

Проект включает каталог товаров, корзину, оформление заказов, JWT-аутентификацию, роли `USER` и `ADMIN`, а также тесты для сервисного, контроллерного и security-слоя.

Этот проект был создан как часть портфолио и как практика разработки backend-приложения с типичной архитектурой интернет-магазина.

## Реализованный функционал

- регистрация новых пользователей;
- авторизация с выдачей JWT-токена;
- роли `USER` и `ADMIN`;
- просмотр каталога товаров;
- фильтрация товаров по бренду и типу;
- получение товара по `id`;
- создание, обновление и удаление товаров для администратора;
- добавление товаров в корзину;
- изменение количества товара в корзине;
- удаление товара из корзины;
- просмотр только своей корзины;
- создание заказа на основе корзины;
- удаление заказа;
- просмотр только своих заказов;
- изменение статуса заказа администратором;
- Swagger / OpenAPI документация;
- глобальная обработка ошибок;
- тесты для основных сценариев.

## Технологии

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Security
- Spring Data JPA
- PostgreSQL
- H2 Database
- Maven
- Swagger / OpenAPI
- JUnit 5
- Mockito

## Архитектура проекта

Проект разделён на основные слои:

- `web` — REST-контроллеры и DTO;
- `service` — бизнес-логика;
- `repository` — работа с базой данных;
- `repository.entity` — JPA-сущности;
- `mapper` — преобразование сущностей в DTO;
- `security` — JWT, фильтры, конфигурация безопасности;
- `globalException` — централизованная обработка ошибок.

## Безопасность

В проекте реализована JWT-аутентификация через Spring Security.

Особенности:
- неавторизованные пользователи не имеют доступа к защищённым endpoint-ам;
- пользователь может работать только со своей корзиной и своими заказами;
- административные операции доступны только пользователям с ролью `ADMIN`.

## API

### Аутентификация

- `POST /api/auth/signup` — регистрация пользователя
- `POST /api/auth/signin` — вход в систему и получение JWT

### Каталог

- `GET /api/catalog` — получить список товаров
- `GET /api/catalog/{id}` — получить товар по `id`
- `POST /api/catalog` — создать товар (`ADMIN`)
- `PUT /api/catalog/{id}` — обновить товар (`ADMIN`)
- `DELETE /api/catalog/{id}` — удалить товар (`ADMIN`)

### Корзина

- `GET /api/cart` — получить свою корзину
- `POST /api/cart` — добавить товар в корзину
- `PATCH /api/cart/{id}` — изменить количество товара в корзине
- `DELETE /api/cart/{id}` — удалить товар из корзины

### Заказы

- `GET /api/order` — получить свои заказы
- `POST /api/order/{cartId}` — создать заказ по записи корзины
- `DELETE /api/order/{orderId}` — удалить свой заказ
- `POST /api/order/start/{orderId}` — перевести заказ в `IN_PROGRESS` (`ADMIN`)
- `POST /api/order/complete/{orderId}` — перевести заказ в `COMPLETED` (`ADMIN`)

## Конфигурация

В проекте используются следующие конфигурационные файлы:

- `application.properties` — базовая конфигурация;
- `application-example.properties` — пример настроек;
- `application-local.properties` — локальная конфигурация для запуска у себя, не коммитится в Git.

### Переменные окружения

Проект использует следующие переменные:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_MS`

Пример значений можно посмотреть в:

- `src/main/resources/application-example.properties`

## Запуск проекта

### 1. Клонировать репозиторий

```bash
git clone <repo-url>
cd online-shop
```

### 2. Настроить базу данных

Создайте PostgreSQL-базу данных и укажите параметры подключения через переменные окружения или локальный профиль.

### 3. Локальный запуск через профиль `local`

Если используется `application-local.properties`, можно запускать проект так:

```powershell
$env:SPRING_PROFILES_ACTIVE="local"
.\mvnw.cmd spring-boot:run
```

### 4. Запуск через переменные окружения

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/postgres"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="password"
$env:JWT_SECRET="your_secret_key_here_12345678901234567890"
$env:JWT_EXPIRATION_MS="600000"
.\mvnw.cmd spring-boot:run
```

## Swagger

После запуска проекта Swagger UI доступен по адресу:

```text
http://localhost:8080/swagger-ui/index.html
```

## Тесты

Запуск тестов:

Linux / macOS:

```bash
./mvnw test
```

Windows PowerShell:

```powershell
.\mvnw.cmd test
```