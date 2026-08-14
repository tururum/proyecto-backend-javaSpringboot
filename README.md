# proyecto-backend-javaSpringboot

Backend de microservicios construido con **Java 21**, **Spring Boot 4.1.0** y **Apache Kafka**, orquestado con **Docker Compose**.

> Proyecto en desarrollo.

## Arquitectura

| Módulo | Puerto | Base de datos | Kafka | Descripción |
| --- | --- | --- | --- | --- |
| `studentservice` | `8080` | MySQL `student_db` | Producer | CRUD de estudiantes y publicación de eventos |
| `groupservice` | `8081` | MySQL `group_db` | — | CRUD de grupos |

**Dependencias de infraestructura** (definidas en los archivos `compose.yml`):

- **Kafka 4.3.1** (KRaft, sin Zookeeper): puerto `9092`.
- **MySQL**: un contenedor por servicio (`mysql-studentservice` en `3306` y `mysql-groupservice` en `3307`).

### Kafka

`studentservice` publica eventos al crear o editar un estudiante (topics `student-created` y `student-updated`). Los topics se crean automáticamente al arrancar la aplicación:

- `student-created`
- `student-updated`
- `student-deleted`

> `groupservice` aún no consume los eventos de Kafka (funcionalidad pendiente).

## Requisitos

- **JDK 21**
- **Maven** (opcional: los módulos incluyen Maven Wrapper `mvnw`)
- **Docker** y **Docker Compose**

## Cómo iniciarlo

### 1. Levantar Kafka

Desde la raíz del proyecto:

```bash
docker compose up -d
```

### 2. Levantar MySQL

Cada servicio tiene su propia instancia de MySQL. Levanta ambas:

```bash
docker compose -f studentservice/compose.yml up -d
docker compose -f groupservice/compose.yml up -d
```

### 3. Ejecutar los servicios

En dos terminales distintas (o en paralelo):

```bash
./studentservice/mvnw -f studentservice/pom.xml spring-boot:run
./groupservice/mvnw -f groupservice/pom.xml spring-boot:run
```

O directamente dentro de cada directorio:

```bash
cd studentservice && ./mvnw spring-boot:run
cd groupservice && ./mvnw spring-boot:run
```

Cada servicio arranca con `server.port` propio (8080 y 8081) y las bases de datos
`student_db` / `group_db` se crean automáticamente al primer arranque
(`createDatabaseIfNotExist=true`).

### Credenciales MySQL

| Variable | Valor |
| --- | --- |
| Usuario | `root` |
| Contraseña | `Karma30102001.` |
| Host | `localhost` |

## Cómo testearlo

### Tests automáticos

Ejecuta los tests de cada servicio:

```bash
./studentservice/mvnw -f studentservice/pom.xml test
./groupservice/mvnw -f groupservice/pom.xml test
```

### Probar la API

#### studentservice (puerto 8080) — `/students`

Listar estudiantes:

```bash
curl http://localhost:8080/students
```

Obtener por id:

```bash
curl http://localhost:8080/students/1
```

Crear un estudiante:

```bash
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Juan","lastName":"Pérez","telefono":"+56912345678"}'
```

> Al crear un estudiante se publica un evento en el topic `student-created`.

Editar un estudiante:

```bash
curl -X PUT http://localhost:8080/students/1 \
  -H "Content-Type: application/json" \
  -d '{"firstName":"María","lastName":"Gómez","telefono":"+56987654321"}'
```

> Al editar un estudiante se publica un evento en el topic `student-updated`.

Eliminar un estudiante:

```bash
curl -X DELETE http://localhost:8080/students/1
```

#### groupservice (puerto 8081)

Listar grupos:

```bash
curl http://localhost:8081
```

Crear un grupo:

```bash
curl -X POST http://localhost:8081 \
  -H "Content-Type: application/json" \
  -d '{"groupName":"Grupo A"}'
```

Editar un grupo:

```bash
curl -X PUT http://localhost:8081/1 \
  -H "Content-Type: application/json" \
  -d '{"groupName":"Grupo B"}'
```

Eliminar un grupo:

```bash
curl -X DELETE http://localhost:8081/1
```

### Verificar eventos en Kafka

Conecta un consumidor al contenedor de Kafka para ver los eventos publicados:

```bash
docker exec -it kafka /opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic student-created \
  --from-beginning
```

## Estructura del proyecto

```
.
├── compose.yml                  # Kafka (KRaft)
├── pom.xml                      # POM padre (multi-módulo)
├── studentservice/
│   ├── compose.yml              # MySQL del servicio (puerto 3306)
│   ├── mvnw
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/karmantial/studentservice/
│       │   ├── Config/KafkaTopicConfig.java
│       │   ├── Kafka/StudentKafkaProducer.java
│       │   ├── controller/StudentController.java
│       │   ├── model/
│       │   ├── repository/StudentRepository.java
│       │   └── service/StudentService.java
│       └── test/...
├── groupservice/
│   ├── compose.yml              # MySQL del servicio (puerto 3307)
│   ├── mvnw
│   ├── pom.xml
│   └── src/...
└── APIDocumentation/            # Colecciones OpenAPI de los servicios
```

## Configuración

La configuración de cada servicio está en `src/main/resources/application.properties`
(puertos, credenciales MySQL, y bootstrap-servers de Kafka en el caso de `studentservice`).

## Notas de desarrollo

- Los eventos de Kafka se serializan como JSON (Jackson).
- Las tablas se gestionan con `ddl-auto=update` de Hibernate, por lo que se crean/actualizan automáticamente.
- Endpoints para consumir eventos en `groupservice` están pendientes.
