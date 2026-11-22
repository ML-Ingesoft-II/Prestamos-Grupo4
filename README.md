# Prestamos-Grupo4

Laboratorio 6 — Ingeniería de Software II (UNAL 2026-II).

## Stack de pruebas elegido

| Pieza | Herramienta | Dependencia / Artefacto |
|-------|-------------|-------------------------|
| Framework de pruebas | JUnit 5 | `org.junit.jupiter:junit-jupiter` |
| Librería de dobles (mocks) | Mockito | `org.mockito:mockito-core` + `org.mockito:mockito-junit-jupiter` |
| Cliente HTTP + JSON | `java.net.http.HttpClient` + Jackson | `com.fasterxml.jackson.core:jackson-databind` |
| Servidor HTTP de prueba | OkHttp MockWebServer | `com.squareup.okhttp3:mockwebserver` |
| Herramienta de cobertura | JaCoCo | `org.jacoco:jacoco-maven-plugin` |

## Requisitos

- Java 17
- Maven 3.9+

## Comandos

Ejecutar las pruebas:

```bash
mvn test
```

Generar el reporte de cobertura:

```bash
mvn clean test
```

El reporte HTML queda disponible en:

```
target/site/jacoco/index.html
```
