package com.unal.prestamos.data;

/**
 * Excepción lanzada cuando el servicio externo de disponibilidad de equipos no responde
 * correctamente (código HTTP distinto a 200/404) o ocurre un error de red.
 */
public class ServicioNoDisponible extends RuntimeException {

    public ServicioNoDisponible(String mensaje) {
        super(mensaje);
    }

    public ServicioNoDisponible(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
