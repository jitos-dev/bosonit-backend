package com.bosonit.garciajuanjo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DisplayNameTest {

    private static Calculadora calculadora;

    @BeforeAll
    public static void initCalculator() {
        calculadora = new Calculadora();
    }

    /*Con @DisplayName lo unico que hacemos es que en la parte izquierda de la consola donde pone el nombre
    * del test que le ponga el de la anotación*/
    @Test
    @DisplayName("metodo: sumar")
    void suma() {
        Integer result = calculadora.suma(4, 3);
        assertThat("The result of 4 plus 3 should be 7",
                result, equalTo(7));
    }

    @Test
    @DisplayName("metodo: restar")
    void resta() {
        Integer rest = calculadora.resta(9, 4);
        assertThat("The result of 9 minus 4 should be 5",
                rest, equalTo(5));
    }

    @Test
    @DisplayName("metodo: multiplicar")
    void multiplicacion() {
        Integer multiply = calculadora.multiplicacion(10, 5);
        assertThat("The result of 10 multiplied by 5 should be 50",
                multiply, equalTo(50));
    }

    @Test
    @DisplayName("metodo: dividir")
    void division() {
        Double divide = calculadora.division(10, 5);
        assertThat("The result of 10 divided by 5 should be 2",
                divide, equalTo(2.0));
    }
}
