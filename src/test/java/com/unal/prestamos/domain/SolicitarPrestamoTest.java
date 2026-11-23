package com.unal.prestamos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SolicitarPrestamoTest {

    @Mock
    private DisponibilidadEquipos disponibilidad;

    @Mock
    private PrestamoRepository repositorio;

    @Mock
    private Notificador notificador;

    @Test
    void ejecutar_equipoDisponible_guardaYNotifica() {
        // Arrange
        when(disponibilidad.estaDisponible(12)).thenReturn(true);
        when(repositorio.siguienteId()).thenReturn(50);
        SolicitarPrestamo casoDeUso =
                new SolicitarPrestamo(disponibilidad, repositorio, notificador);

        // Act
        Prestamo prestamo = casoDeUso.ejecutar("1023", 12);

        // Assert
        assertEquals(EstadoPrestamo.PENDIENTE, prestamo.getEstado());
        verify(repositorio).guardar(prestamo);
        verify(notificador).notificar("1023", "Solicitud 50 registrada");
    }

    @Test
    void ejecutar_equipoNoDisponible_noGuardaNiNotifica() {
        // Arrange
        when(disponibilidad.estaDisponible(12)).thenReturn(false);
        SolicitarPrestamo casoDeUso =
                new SolicitarPrestamo(disponibilidad, repositorio, notificador);

        // Act
        EquipoNoDisponible ex =
                assertThrows(EquipoNoDisponible.class, () -> casoDeUso.ejecutar("1023", 12));

        // Assert
        assertEquals("Equipo 12 no disponible", ex.getMessage());
        verify(repositorio, never()).guardar(org.mockito.ArgumentMatchers.any());
        verifyNoInteractions(notificador);
    }

    @Test
    void ejecutar_falloAlGuardar_noNotifica() {
        // Arrange
        when(disponibilidad.estaDisponible(12)).thenReturn(true);
        when(repositorio.siguienteId()).thenReturn(50);
        org.mockito.Mockito.doThrow(new RuntimeException("BD caída"))
                .when(repositorio).guardar(org.mockito.ArgumentMatchers.any());
        SolicitarPrestamo casoDeUso =
                new SolicitarPrestamo(disponibilidad, repositorio, notificador);

        // Act
        RuntimeException ex =
                assertThrows(RuntimeException.class, () -> casoDeUso.ejecutar("1023", 12));

        // Assert
        assertEquals("BD caída", ex.getMessage());
        verifyNoInteractions(notificador);
    }
}
