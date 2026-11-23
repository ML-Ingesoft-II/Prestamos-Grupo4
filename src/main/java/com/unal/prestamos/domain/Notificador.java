package com.unal.prestamos.domain;

/**
 * Contrato (puerto) para notificar al estudiante.
 */
public interface Notificador {

    void notificar(String estudianteId, String mensaje);
}
