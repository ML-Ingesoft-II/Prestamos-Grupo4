package com.unal.prestamos.data;

import com.unal.prestamos.domain.Prestamo;
import com.unal.prestamos.domain.PrestamoRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Doble de prueba tipo Fake para el contrato PrestamoRepository.
 * Utiliza una lista en memoria y un contador autoincremental para simular la persistencia.
 */
public class PrestamoRepositoryEnMemoria implements PrestamoRepository {

    private final List<Prestamo> prestamos = new ArrayList<>();
    private int contadorId = 1;

    @Override
    public int siguienteId() {
        return contadorId++;
    }

    @Override
    public void guardar(Prestamo prestamo) {
        prestamos.add(prestamo);
    }

    public List<Prestamo> getPrestamos() {
        return Collections.unmodifiableList(prestamos);
    }

    public boolean contiene(Prestamo prestamo) {
        return prestamos.contains(prestamo);
    }

    public int totalGuardados() {
        return prestamos.size();
    }
}
