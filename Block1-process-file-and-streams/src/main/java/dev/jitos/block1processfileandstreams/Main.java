package dev.jitos.block1processfileandstreams;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Main {

    public static List<Person> getPersonList(String path) {
        List<Person> persons = new ArrayList<>();

        // Flujo de datos para leer el fichero
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(":");

                // numero de delimitadores de la fila
                int delimiterCount = line.length() - line.replace(":", "").length();

                try {
                    //Cuando faltan los dos delimitadores de campo o el ultimo
                    if (delimiterCount == 0 || delimiterCount == 1) {
                        throw new InvalidLineFormatException("Los campos de la linea del fichero son insuficientes.\n" +
                                "LINEA: 32 funcion getPersonList().\n" +
                                "CAUSA: Faltan delimitadores");
                    }

                    // si no contiene nombre
                    if (fields[0].isBlank()) {
                        throw new InvalidLineFormatException("El campo nombre es obligatorio.\n" +
                                "LINEA: 39 funcion getPersonList().\n" +
                                "CAUSA: El campo nombre no puede estar vacio");
                    }

                    Person person = new Person();

                    //Asignamos la edad
                    if (fields.length == 2 || fields[2].isBlank()) {
                        person.setAge("0");
                    } else if (fields[2].equals("0")) {
                        person.setAge("unknown");
                    } else {
                        person.setAge(fields[2]);
                    }

                    //Asignamos la ciudad
                    String town = fields[1].isBlank() ? "unknown" : fields[1];
                    person.setTown(town);

                    //Asignamos el nombre
                    person.setName(fields[0]);

                    persons.add(person);

                } catch (InvalidLineFormatException ife) {
                    // si lanza la excepcion la controlamos aquí para que no termine el programa por esto
                    System.out.println(ife.getMessage());
                }
            }


        } catch (FileNotFoundException fnfe) {
            System.out.println("No se ha encontrado el fichero en la ruta especificada");
        } catch (IOException ioe) {
            System.out.println("Ha ocurrido un error con la lectura de datos");
        }

        return persons;
    }

    public static List<Person> filterPersonsByPredicate(Predicate<Person> predicate, List<Person> personList) {
        return personList.stream().filter(predicate).toList();
    }

    public static void main(String[] args) {

        try {
            Path pathFile = Paths.get("./files/persons.csv");

            //lista de personas del fichero .csv
            var persons = Main.getPersonList(pathFile.toString());

            //predicado para el filtro de personas que sean menores de 25 años
            Predicate<Person> ageLess25 = person ->
                    !person.getAge().equalsIgnoreCase("unknown")
                            && Integer.parseInt(person.getAge()) < 25
                            && Integer.parseInt(person.getAge()) > 0;

            Predicate<Person> startWithLetterA = person ->
                    !person.getName().startsWith("A")
                            && !person.getName().startsWith("Á")
                            && !person.getName().startsWith("a");

            Main.filterPersonsByPredicate(ageLess25, persons)
                    .forEach(System.out::println);

            System.out.println("--------------------------------");

            var personsFilter = Main.filterPersonsByPredicate(startWithLetterA, persons);
            personsFilter.forEach(System.out::println);

            System.out.println("--------------------------------");

            var personMadrid = personsFilter.stream()
                    .filter(person -> person.getTown().equalsIgnoreCase("Madrid"))
                    .findFirst();

            personMadrid.ifPresent(System.out::println);

            System.out.println("--------------------------------");

            var personBarcelona = personsFilter.stream()
                    .filter(person -> person.getTown().equalsIgnoreCase("barcelona"))
                    .findFirst();

            personBarcelona.ifPresent(System.out::println);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
