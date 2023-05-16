package com.bosonit.garciajuanjo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TagTest {

    private static Calculadora calculadora;

    @BeforeAll
    public static void initCalculator() {
        calculadora = new Calculadora();
    }

    @Test
    void suma() {
        Integer result = calculadora.suma(4, 3);
        assertThat("The result of 4 plus 3 should be 7",
                result, equalTo(7));
    }

    @Test
    void resta() {
        Integer rest = calculadora.resta(9, 4);
        assertThat("The result of 9 minus 4 should be 5",
                rest, equalTo(5));
    }

    /*Anotando estos métodos de esta forma podemos ejecutar los test solo a estos. Para esto hay que crear una
    * nueva configuración para ejecutar Intelly Idea. Para esto creamos nueva configuración de JUnit. Dentro
    * cambiamos el nombre y donde pone all in directory pinchamos y elegimos tag. Le damos como valor el que
    * le hayamos puesto en la anotación. Con esto solo se correran los test anotados con @Tag y el valor*/
    @Test
    @Tag("advance")
    void multiplicacion() {
        Integer multiply = calculadora.multiplicacion(10, 5);
        assertThat("The result of 10 multiplied by 5 should be 50",
                multiply, equalTo(50));
    }

    @Test
    @Tag("advance")
    void division() {
        Double divide = calculadora.division(10, 5);
        assertThat("The result of 10 divided by 5 should be 2",
                divide, equalTo(2.0));
    }
}
