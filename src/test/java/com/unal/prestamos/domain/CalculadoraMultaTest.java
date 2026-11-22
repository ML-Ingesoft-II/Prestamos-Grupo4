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

    @Test
    void calcular_unDiaDeRetraso_cobraDosMil() {
        // Arrange
        CalculadoraMulta calculadora = new CalculadoraMulta();
        LocalDate devolucion = LIMITE.plusDays(1);

        // Act
        int multa = calculadora.calcular(LIMITE, devolucion, false);

        // Assert
        assertEquals(2000, multa);
    }

    @Test
    void calcular_devueltoElDiaLimite_noHayMulta() {
        // Arrange
        CalculadoraMulta calculadora = new CalculadoraMulta();
        LocalDate devolucion = LIMITE;

        // Act
        int multa = calculadora.calcular(LIMITE, devolucion, false);

        // Assert
        assertEquals(0, multa);
    }

    @Test
    void calcular_cuarentaDiasDeRetraso_aplicaTope() {
        // Arrange
        CalculadoraMulta calculadora = new CalculadoraMulta();
        LocalDate devolucion = LIMITE.plusDays(40);

        // Act
        int multa = calculadora.calcular(LIMITE, devolucion, false);

        // Assert
        assertEquals(50000, multa);
    }

    @Test
    void calcular_veinticincoDiasDeRetraso_justoElTope() {
        // Arrange
        CalculadoraMulta calculadora = new CalculadoraMulta();
        LocalDate devolucion = LIMITE.plusDays(25);

        // Act
        int multa = calculadora.calcular(LIMITE, devolucion, false);

        // Assert
        assertEquals(50000, multa);
    }
}
