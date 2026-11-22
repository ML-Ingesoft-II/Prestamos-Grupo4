package com.unal.prestamos.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculadoraMulta {

    private static final int MULTA_POR_DIA = 2000;
    private static final int MULTA_MAXIMA = 50000;

    public int calcular(LocalDate fechaLimite, LocalDate fechaDevolucion, boolean esMonitor) {
        long diasDeRetraso = ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion);
        if (diasDeRetraso <= 0) {
            return 0;
        }
        int multa = Math.min((int) diasDeRetraso * MULTA_POR_DIA, MULTA_MAXIMA);
        if (esMonitor) {
            return multa / 2;
        }
        return multa;
    }
}
