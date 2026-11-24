package com.unal.prestamos.data;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DisponibilidadEquiposHttpTest {

    private MockWebServer servidor;
    private DisponibilidadEquiposHttp adaptador;

    @BeforeEach
    void setUp() throws IOException {
        servidor = new MockWebServer();
        servidor.start();
        adaptador = new DisponibilidadEquiposHttp(servidor.url("/").toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        servidor.shutdown();
    }

    @Test
    void estaDisponible_equipoDisponible_devuelveVerdaderoYLlamaLaRutaCorrecta() throws InterruptedException {
        // Arrange
        servidor.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"id\": 12, \"nombre\": \"Portatil\", \"estado\": \"disponible\"}"));

        // Act
        boolean resultado = adaptador.estaDisponible(12);

        // Assert
        assertTrue(resultado);
        RecordedRequest peticion = servidor.takeRequest();
        assertEquals("GET", peticion.getMethod());
        assertEquals("/api/v1/equipos/12", peticion.getPath());
    }

    @Test
    void estaDisponible_servidorResponde200ConEstadoPrestado_devuelveFalso() {
        // Arrange
        servidor.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"id\": 12, \"nombre\": \"Portatil\", \"estado\": \"prestado\"}"));

        // Act
        boolean resultado = adaptador.estaDisponible(12);

        // Assert
        assertFalse(resultado);
    }

    @Test
    void estaDisponible_servidorResponde404_devuelveFalso() {
        // Arrange
        servidor.enqueue(new MockResponse()
                .setResponseCode(404));

        // Act
        boolean resultado = adaptador.estaDisponible(12);

        // Assert
        assertFalse(resultado);
    }

    @Test
    void estaDisponible_servidorResponde500_lanzaServicioNoDisponible() {
        // Arrange
        servidor.enqueue(new MockResponse()
                .setResponseCode(500));

        // Act
        ServicioNoDisponible ex = assertThrows(
                ServicioNoDisponible.class,
                () -> adaptador.estaDisponible(12)
        );

        // Assert
        assertEquals("HTTP 500", ex.getMessage());
    }

    @Test
    void estaDisponible_servidorRespondeJsonInvalido_lanzaServicioNoDisponible() {
        // Arrange
        servidor.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("{json corrupto}"));

        // Act & Assert
        ServicioNoDisponible ex = assertThrows(
                ServicioNoDisponible.class,
                () -> adaptador.estaDisponible(12)
        );

        assertTrue(ex.getMessage().contains("Error al deserializar JSON"));
    }

    @Test
    void estaDisponible_errorDeRed_lanzaServicioNoDisponible() throws IOException {
        // Arrange: apagar el servidor antes de la petición para simular fallo de red
        servidor.shutdown();

        // Act
        ServicioNoDisponible ex = assertThrows(
                ServicioNoDisponible.class,
                () -> adaptador.estaDisponible(12)
        );

        // Assert
        assertNotNull(ex.getMessage());
        assertNotNull(ex.getCause());
    }
}
