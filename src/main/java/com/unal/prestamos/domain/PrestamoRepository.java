package com.unal.prestamos.domain;

/**
 * Contrato (puerto) de persistencia del agregado Prestamo.
 */
public interface PrestamoRepository {

    int siguienteId();

    void guardar(Prestamo prestamo);
}
