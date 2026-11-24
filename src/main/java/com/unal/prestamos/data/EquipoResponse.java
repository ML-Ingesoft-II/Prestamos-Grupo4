package com.unal.prestamos.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO que representa la respuesta JSON devuelta por la API de equipos:
 * {"id": 12, "nombre": "Portatil", "estado": "disponible"}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EquipoResponse(int id, String nombre, String estado) {
}
