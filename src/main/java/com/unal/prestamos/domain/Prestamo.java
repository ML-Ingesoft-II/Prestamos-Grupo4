package com.unal.prestamos.domain;

import java.time.LocalDate;

public class Prestamo {

    private final int id;
    private final String estudianteId;
    private final int equipoId;
    private EstadoPrestamo estado;
    private LocalDate fechaLimite;
    private int renovaciones;

    public Prestamo(int id, String estudianteId, int equipoId) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.equipoId = equipoId;
        this.estado = EstadoPrestamo.PENDIENTE;
        this.fechaLimite = null;
        this.renovaciones = 0;
    }

    public void aprobar(LocalDate hoy) {
        if (estado != EstadoPrestamo.PENDIENTE) {
            throw new IllegalStateException("Solo se aprueba un préstamo pendiente");
        }
        estado = EstadoPrestamo.APROBADO;
        fechaLimite = hoy.plusDays(7);
    }

    public void renovar() {
        if (estado != EstadoPrestamo.APROBADO) {
            throw new IllegalStateException("Solo se renueva un préstamo aprobado");
        }
        if (renovaciones >= 1) {
            throw new IllegalStateException("El préstamo ya fue renovado");
        }
        fechaLimite = fechaLimite.plusDays(7);
        renovaciones += 1;
    }

    public void devolver() {
        if (estado != EstadoPrestamo.APROBADO) {
            throw new IllegalStateException("Solo se devuelve un préstamo aprobado");
        }
        estado = EstadoPrestamo.DEVUELTO;
    }

    public int getId() {
        return id;
    }

    public String getEstudianteId() {
        return estudianteId;
    }

    public int getEquipoId() {
        return equipoId;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public int getRenovaciones() {
        return renovaciones;
    }
}
