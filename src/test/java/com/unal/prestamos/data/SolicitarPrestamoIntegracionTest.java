package com.unal.prestamos.data;

import com.unal.prestamos.domain.EstadoPrestamo;
import com.unal.prestamos.domain.Notificador;
import com.unal.prestamos.domain.Prestamo;
import com.unal.prestamos.domain.SolicitarPrestamo;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SolicitarPrestamoIntegracionTest {

    private MockWebServer servidor;
    private DisponibilidadEquiposHttp adaptadorHttp;
    private PrestamoRepositoryEnMemoria fakeRepositorio;

    @Mock
    private Notificador notificador;

    @BeforeEach
    void setUp() throws IOException {
        servidor = new MockWebServer();
        servidor.start();
        adaptadorHttp = new DisponibilidadEquiposHttp(servidor.url("/").toString());
        fakeRepositorio = new PrestamoRepositoryEnMemoria();
    }

    @AfterEach
    void tearDown() throws IOException {
        servidor.shutdown();
    }

    @Test
    void solicitarPrestamo_conAdaptadorHttpRealYFakeRepo_guardaEnFakeYNotifica() {
        // Arrange
        servidor.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"id\": 12, \"nombre\": \"Portatil\", \"estado\": \"disponible\"}"));

        SolicitarPrestamo casoDeUso =
                new SolicitarPrestamo(adaptadorHttp, fakeRepositorio, notificador);

        // Act
        Prestamo prestamo = casoDeUso.ejecutar("1023", 12);

        // Assert
        assertNotNull(prestamo);
        assertEquals(1, prestamo.getId());
        assertEquals("1023", prestamo.getEstudianteId());
        assertEquals(12, prestamo.getEquipoId());
        assertEquals(EstadoPrestamo.PENDIENTE, prestamo.getEstado());

        // Verificar que el préstamo quedó guardado en el fake
        assertEquals(1, fakeRepositorio.totalGuardados());
        assertTrue(fakeRepositorio.contiene(prestamo));

        // Verificar que el notificador recibió el mensaje esperado
        verify(notificador).notificar("1023", "Solicitud 1 registrada");
    }
}
