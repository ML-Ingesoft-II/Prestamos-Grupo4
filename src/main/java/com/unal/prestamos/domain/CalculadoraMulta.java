package com.unal.prestamos.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculadoraMulta {

    public int calcular(LocalDate fechaLimite, LocalDate fechaDevolucion, boolean esMonitor) {
        long dias = ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion);
        if (dias <= 0) {
            return 0;
        }
        return (int) dias * 2000;
    }
}
