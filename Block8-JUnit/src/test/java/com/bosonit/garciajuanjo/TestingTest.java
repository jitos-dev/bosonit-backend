package com.bosonit.garciajuanjo;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matcher.*;
import static org.hamcrest.Matchers.*;

public class TestingTest {

    /**
     * Distintas operaciones que podemos realizar para comparar si son iguales. Para este ejemplo utilizo
     * string pero podemos utilizar cualquier objeto. El que comprueba sin los caracteres en blanco o el
     * de ignoreCase son especificos para cadenas de caracteres
     */
    @Test
    public void exampleEqual() {
        assertThat("text", is("text"));
        assertThat("text", equalTo("text"));
        assertThat("text", equalToIgnoringCase("TEXT"));
        assertThat("text show", equalToCompressingWhiteSpace("   text  show   "));
        assertThat("text", endsWith("xt"));
        assertThat("text", startsWith("te"));
        assertThat("text", endsWithIgnoringCase("XT"));
        assertThat("text", startsWithIgnoringCase("TE"));
        assertThat("text", containsString("ex"));
        assertThat("text", containsStringIgnoringCase("EX"));

        //En este comprueba que después de una e venga una t aunque no tiene que ser justo después
        assertThat("text", stringContainsInOrder("e", "t"));

        //Este falla porque después de la x nunca va una e
        //assertThat("text", stringContainsInOrder("x", "e"));
    }

    /**
     * Lo mismo que el metodo de exampleEqual pero ahora con negación
     */
    @Test
    public void exampleNot(){
        assertThat("text", not("texto"));
        assertThat("text", is(not("texto")));
        assertThat("text", not(equalToIgnoringCase("TEXTO")));
    }

    /*------------------------------------------------------------------
    *
    * Forma de comparar con objeto*/

    /**
     * Esta es la forma de comparar objetos. Los compara por Objects.equals(). Se pueden combinar todas
     * las opciones como antes con not, is, etc.
     */
    @Test
    public void exampleEqualTo() {
        String[] values = new String[] {"uno", "dos"};

        assertThat(values, equalTo(new String[]{"uno", "dos"}));
        assertThat(values, not(equalTo(new String[]{"dos", "uno"})));
        assertThat(values, is(equalTo(new String[]{"uno", "dos"})));

        //Falla porque las posiciones son distintas
        //assertThat(values, equalTo(new String[]{"dos", "uno"}));
    }

    /**
     * Este comprueba que dos objetos son la misma instancia
     */
    @Test
    public void exampleSameInstance() {
        String[] values = new String[] {"uno", "dos"};
        String[] otherValues = new String[] {"uno", "dos"};

        assertThat(values, sameInstance(values));

        //Este falla porque aunque son iguales no son la misma instancia
        //assertThat(values, sameInstance(otherValues));
    }

    /**
     * Este metodo como su nombre indica comprueba el tipo de dato de la variable que pasemos
     */
    @Test
    public void exampleInstanceOf(){
        var text = "1";

        assertThat(text, instanceOf(String.class));

        //isA es una abreviatura de utilizar instanceOf
        assertThat(text, isA(String.class));

        //Este falla porque no es de tipo Ingeger
        //assertThat(text, instanceOf(Integer.class));
    }

    /**
     * Este también comprueba el tipo de dato de la variable pero a diferencia de instanceOf directamente
     * sabe que el valor real es de un tipo y si el esperado no es del mismo no compila
     */
    @Test
    public void exampleAny() {
        var text = "1";

        assertThat(text, any(String.class));

        //Esta línea directamente no compila el programa
        //assertThat(text, any(Integer.class));
    }

    /**
     * Tenemos que tener en cuenta que lo que pasemos tienen que ser expresiones boleanas para hacer las
     * distintas comprobaciones
     * - allOf(): Es como el && de Java
     * - anyOf(): Es como el || de Java
     * - both(): Igual que allOf() pero con otra sintaxis
     * - either(): Igual que anyOf() pero con otra sintaxis
     */
    @Test
    public void exampleBooleanExpression() {
        var text = "text";

        assertThat(text, allOf(startsWith("te"), endsWith("xt"), containsString("ex")));

        //Este pasa porque aunque el segundo es falso estamos comprobando un OR (||) y el primero si es verdadero
        assertThat(text, anyOf(startsWith("te"), endsWith("te")));

        //se pueden combinar como queramos. La ultima es false pero como es un or pasa la prueba
        assertThat(text, both(startsWith("te")).and(endsWith("xt")).and(containsString("ex")).or(startsWith("tex")));

        //either y both son practicamente iguales
        assertThat(text, either(startsWith("te")).or(startsWith("tr")).and(containsString("ex")));
    }

    /*------------------------------------------------------------------------------------------------------
    *
    * Comprobando colecciones*/

    /**
     * Esta es la manera de comprobar colecciones.
     */
    @Test
    public void exampleCollections() {
        var collection = Arrays.asList("uno", "dos", "tres", "cuatro");

        assertThat(collection, hasItem("dos"));

        //Comprueba que exista un item que empieza por d
        assertThat(collection, hasItem(startsWith("d")));

        //comprueba que existan varios items
        assertThat(collection, hasItems("uno", "cuatro"));
    }


    /**
     * Esta es la forma de comprobar null y texto vacio
     */
    @Test
    public void exampleNullOrEmpty() {
        String first = "";
        String second = " ";
        String third = null;
        String fourth = "null";

        assertThat(first, blankString());
        assertThat(second, blankString());
        //assertThat(third, blankString());
        //assertThat(fourth, blankString());

        assertThat(first, blankOrNullString());
        assertThat(second, blankOrNullString());
        assertThat(third, blankOrNullString());
        //assertThat(fourth, blankOrNullString());

        //Estas hacen lo mismo que blankString()
        assertThat(first, emptyString());
        assertThat(third, emptyOrNullString());
    }

    /**
     * Aquí podemos personalizarlo to lo que queramos porque podemos pasarle un patron y que lo compruebe
     */
    @Test
    public void examplePattern() {
        var text = "pattern";
        var pattern = "[a-z]+";

        assertThat(text, matchesPattern(pattern));
    }


}
