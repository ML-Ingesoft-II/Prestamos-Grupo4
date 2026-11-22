package com.unal.prestamos.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class CalculadoraMultaTest {

    private static final LocalDate LIMITE = LocalDate.of(2026, 11, 25);

    @Test
    void calcular_devueltoAntesDelLimite_noHayMulta() {
        // Arrange
        CalculadoraMulta calculadora = new CalculadoraMulta();
        LocalDate devolucion = LIMITE.minusDays(1);

        // Act
        int multa = calculadora.calcular(LIMITE, devolucion, false);

        // Assert
        assertEquals(0, multa);
    }
}
