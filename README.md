# BookService
Import and lend  books. Concurrency pattern db, rest API, controller IT

## 1. Setup local dev

### 1.1 Database

```
docker run -d --name golo-book-service-db-dev -e TZ=UTC -p 30432:5432 -e POSTGRES_DB=golo-book-service-db -e POSTGRES_USER=golo -e POSTGRES_PASSWORD=qFkJj92N9A4E --restart=always postgres:15

```

## 2. Deploy the Application and Database as docker container
copy target/application.jar to src/main/docker
- in terminal goto /src/main/docker
    - run docker-compose build
    - run docker-compose up