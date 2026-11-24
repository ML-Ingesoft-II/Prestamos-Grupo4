package com.unal.prestamos.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unal.prestamos.domain.DisponibilidadEquipos;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Adaptador de infraestructura que implementa el contrato DisponibilidadEquipos
 * consultando una API REST externa mediante HttpClient y Jackson.
 */
public class DisponibilidadEquiposHttp implements DisponibilidadEquipos {

    private final String urlBase;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public DisponibilidadEquiposHttp(String urlBase) {
        this(urlBase, HttpClient.newHttpClient(), new ObjectMapper());
    }

    public DisponibilidadEquiposHttp(String urlBase, HttpClient httpClient, ObjectMapper objectMapper) {
        this.urlBase = urlBase.endsWith("/") ? urlBase.substring(0, urlBase.length() - 1) : urlBase;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean estaDisponible(int equipoId) {
        String ruta = urlBase + "/api/v1/equipos/" + equipoId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ruta))
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new ServicioNoDisponible(e.getMessage() != null ? e.getMessage() : "Error de red", e);
        }

        int statusCode = response.statusCode();
        if (statusCode == 200) {
            try {
                EquipoResponse equipo = objectMapper.readValue(response.body(), EquipoResponse.class);
                return equipo != null && "disponible".equalsIgnoreCase(equipo.estado());
            } catch (Exception e) {
                throw new ServicioNoDisponible("Error al deserializar JSON: " + e.getMessage(), e);
            }
        } else if (statusCode == 404) {
            return false;
        } else {
            throw new ServicioNoDisponible("HTTP " + statusCode);
        }
    }
}
