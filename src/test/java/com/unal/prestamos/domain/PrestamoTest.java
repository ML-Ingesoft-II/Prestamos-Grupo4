package com.unal.prestamos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrestamoTest {

    private static final LocalDate HOY = LocalDate.of(2026, 11, 18);

    private Prestamo prestamo;

    @BeforeEach
    void setUp() {
        prestamo = new Prestamo(1, "1023", 12);
    }

    @Test
    void aprobar_prestamoPendiente_quedaAprobadoConLimiteEnSieteDias() {
        // Arrange
        // (prestamo recién creado, en estado PENDIENTE)

        // Act
        prestamo.aprobar(HOY);

        // Assert
        assertEquals(EstadoPrestamo.APROBADO, prestamo.getEstado());
        assertEquals(LocalDate.of(2026, 11, 25), prestamo.getFechaLimite());
    }

    @Test
    void renovar_porSegundaVez_lanzaError() {
        // Arrange
        prestamo.aprobar(HOY);
        prestamo.renovar();

        // Act
        IllegalStateException ex =
                assertThrows(IllegalStateException.class, () -> prestamo.renovar());

        // Assert
        assertEquals("El préstamo ya fue renovado", ex.getMessage());
    }

    @Test
    void renovar_prestamoAprobadoUnaVez_extiendeFechaLimiteSieteDias() {
        // Arrange
        prestamo.aprobar(HOY);

        // Act
        prestamo.renovar();

        // Assert
        assertEquals(LocalDate.of(2026, 12, 2), prestamo.getFechaLimite());
        assertEquals(1, prestamo.getRenovaciones());
    }

    @Test
    void aprobar_prestamoYaAprobado_lanzaError() {
        // Arrange
        prestamo.aprobar(HOY);

        // Act
        IllegalStateException ex =
                assertThrows(IllegalStateException.class, () -> prestamo.aprobar(HOY));

        // Assert
        assertEquals("Solo se aprueba un préstamo pendiente", ex.getMessage());
    }

    @Test
    void renovar_prestamoPendiente_lanzaError() {
        // Arrange
        // (prestamo recién creado, en estado PENDIENTE)

        // Act
        IllegalStateException ex =
                assertThrows(IllegalStateException.class, () -> prestamo.renovar());

        // Assert
        assertEquals("Solo se renueva un préstamo aprobado", ex.getMessage());
    }

    @Test
    void devolver_prestamoAprobado_quedaEnEstadoDevuelto() {
        // Arrange
        prestamo.aprobar(HOY);

        // Act
        prestamo.devolver();

        // Assert
        assertEquals(EstadoPrestamo.DEVUELTO, prestamo.getEstado());
    }

    @Test
    void devolver_prestamoYaDevuelto_lanzaError() {
        // Arrange
        prestamo.aprobar(HOY);
        prestamo.devolver();

        // Act
        IllegalStateException ex =
                assertThrows(IllegalStateException.class, () -> prestamo.devolver());

        // Assert
        assertEquals("Solo se devuelve un préstamo aprobado", ex.getMessage());
    }
}
