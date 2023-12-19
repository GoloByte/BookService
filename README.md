# BookService

Import and borrow books. Concurrency pattern db, rest controller test with spring restclient and also mockmvc

## 1. Setup local dev

### 1.1 Database

```
docker run -d --name golo-book-service-db-dev -e TZ=UTC -p 31432:5432 -e POSTGRES_DB=golo-book-service-db -e POSTGRES_USER=golo -e POSTGRES_PASSWORD=qFkJj92N9A4E --restart=always postgres:15

```

## 2. Deploy the Application and Database as docker container
copy target/application.jar to src/main/docker
- in terminal goto /src/main/docker
    - run docker-compose build
    - run docker-compose up

## 3. Table join

SELECT
b.id AS book_id,
b.name AS book_name,
b.borrowed AS book_borrowed,
b.charge_id AS book_charge_id,
a.id AS author_id,
a.name AS author_name

FROM
book b

JOIN book_author ba ON b.id = ba.book_id

JOIN author a ON a.id = ba.author_id;