package com.unal.prestamos.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculadoraMulta {

    private static final int SIN_MULTA = 0;
    private static final int MULTA_POR_DIA = 2000;
    private static final int MULTA_MAXIMA = 50000;
    private static final int DIVISOR_DESCUENTO_MONITOR = 2;

    public int calcular(LocalDate fechaLimite, LocalDate fechaDevolucion, boolean esMonitor) {
        long diasDeRetraso = ChronoUnit.DAYS.between(fechaLimite, fechaDevolucion);
        if (diasDeRetraso <= 0) {
            return SIN_MULTA;
        }
        int multaConTope = (int) Math.min(diasDeRetraso * MULTA_POR_DIA, MULTA_MAXIMA);
        return esMonitor ? aplicarDescuentoMonitor(multaConTope) : multaConTope;
    }

    private int aplicarDescuentoMonitor(int multa) {
        return multa / DIVISOR_DESCUENTO_MONITOR;
    }
}
