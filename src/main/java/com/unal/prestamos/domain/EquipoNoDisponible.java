package com.unal.prestamos.domain;

/**
 * Error de negocio: el equipo solicitado no está disponible para préstamo.
 */
public class EquipoNoDisponible extends RuntimeException {

    public EquipoNoDisponible(int equipoId) {
        super("Equipo " + equipoId + " no disponible");
    }
}
