# Read Me 
Este proyecto ha tenido muchas modificaciones que no he apuntado desde un principio pero que a partir de ahora voy a ir 
apuntandolas para que sirvan de ayuda al revisarlo más tarde.

Todos estos ejercicios estan controlados con Git por lo que si queremos ver alguno en concreto hay que buscar su commit
que normalmente suelo ponerle finish más el nombre del ejercicio.

### Bloque 7 Spring Data (Básico) Feign y RestTemplate
Feign y RestTemplate son dos formas de hacer peticiones a una url externa y obtener los resultados de esa petición. 
Podemos utilizar las dos que son muy sencillas. Hay un ejemplo en el cual desde el controlador de PersonController 
queremos obtener un 'Teacher' por su id. Llamamos al método getTeacherByIdTeacher en el servicio de Person 
(PersonServiceImpl) y es ahí donde los utilizamos. 

El código de ejemplo esta hecho con FeignClient, si queremos utilizar RestTemplate hay que sustituir la línea de
FeignClient por esta:
* ResponseEntity<TeacherOutputDto> responseEntity = new RestTemplate()
  .getForEntity("http://localhost:8081/teacher/" + teacherId, TeacherOutputDto.class);

Para probar este programa había que simular un cliente y un servidor donde el cliente tendría el puerto 8080 y el servidor
el puerto 8081. Para hacer esto cree un perfil (application-server.properties) con las configuraciones de la base de
datos y el puerto para hacer de servidor. Luego configuré IntellyIdea para poder ejecutar varias veces un mismo programa
y lo ejecuté primero como servidor con una variable de entorno (spring.profiles.active=server) para que se levantara
como servidor y otra vez como cliente con una configuración normal. Estas configuraciones las guardé (/runConfigurations)  
después por lo que si se quiere probar hay que ejecutarlo con las configuraciones mencionadas.

### Bloque 10 Docker
Para realizar este ejercicio lo que hacemos es primero cambiar de base de datos la aplicación por la base de datos de
Postgre. Una vez que la tenemos cambiada y nos funciona en local tenemos que crear el jar de la aplicación que lo hago
con Maven pero hay que hacerlo sin que ejecute los tests porque la url de la base de datos en el .properties ya no será
localhost si no el nombre del contenedor que vamos a utilizar en Docker (podemos ver las dos url en el archivo .properties)
y si ejecuta los test falla. Para esto hacemos:
* mvn clean package -DskipTests

Una vez que tenemos el .jar ya podemos crear el DockerFile y crear la imagen del proyecto así:
* docker build -t block7-crud-validation:v1 .

Para ejecutrar la aplicación hay que levantar un contenedor de Postgres en la misma 'Network' que el contenedor que
contendrá la imagen de nuestra aplicación.

### Bloque 11 Spring Web (Avanzado) Cors
Este ejercicio consiste en crear un controlador nuevo que llamo PersonCorsController el cual puede recibir peticiones 
solo de una ruta que se la especifico con la siguiente anotación:
* @CrossOrigin(origins = "https://cdpn.io")

De esta forma solo esa url podrá realizar peticiones a este endpoint.

### Bloque 12 Spring Data (Avanzado) Criteria API
Este ejercicio consiste en realizar un endpoint dinámico donde se le pueden poner una cantidad de parámetros los cuales
ninguno es obligatorio. Con esos datos se realiza una búsqueda a la base de datos y se devuelven los resultados 
incluyendo paginación. El ejemplo se encuentra en las siguientes clases:
* PersonController en el método findPersonBy
* PersonRepositoryImpl que es donde esta el método que se encarga de la consulta

Como estamos utilizando Criteria API tenemos que tener una clase que se llame igual que el repositorio solo que terminada
en impl (por eso PersonRepositoryImpl) y tiene que estar en el mismo paquete. En el ejemplo se puede ver la consulta
dinámica y el resultado con paginación. 

Como se necesitan bastantes registros para probar la aplicación con la paginación, para no incluirlos a mano tengo un
archivo JSON con los datos en la carpeta resources (mockData.json). Este archivo lo cargo en la base de datos una vez
al ejecutar la aplicación desde la clase Block7CrudValidationApplication.java en el método mockData() y lo ejecuto
con un CommandLineRunner.

En el directorio /postman está el json para hacer las pruebas.

### Bloque 13 Testing con JUnit avanzado
Para este proyecto hay que realizar los test a todo lo que tenga funcionalidad como por ejemplo servicios, repositorios
y controladores.
Para algunas pruebas he hecho pruebas funcionales y para otras pruebas unitarias. 
Si nos fijamos en los test para algunos he creído conveniente levantar una base de datos h2 en memoria por lo que tengo
varios perfiles para estas pruebas en función de lo que necesito en cada caso.
Dentro de mi carpeta programación hay una explicación de como hacer las pruebas con JUnit y Mockito y como correr los
tests con Sonarqube para no tener problemas

### Bloque 14 Spring Security
Este proyecto se ha adaptado a Spring Security con JWT. Cuando se hace login (http://localhost:8080/auth/login) siempre
que se esté registrado (se puede registrar en http://localhost:8080/auth/register) se le asignará un token, el cual se 
tiene que incluir en las cabeceras de las solicitudes para poder acceder a los distintos endpoint. Los usuarios con el
role 'USER' solo pueden acceder a los endpoint de tipo GET mientras que el usuario con el role 'ADMIN' podrá acceder
a cualquier endpoint. Los usuarios que no estén logeados solo podrán acceder a los endpoint dentro del controlador
'auth' para realizar el login y el registro.
Los archivos que componen este apartado de seguridad (aparte de las dependencias en el POM) son los siguientes:
* config: En este directorio se encuentran varias clases de configuración como SecurityConfiguration que se encarga de
filtrar que tipo de peticiones puede hacer cada usuario en función de su Role o JwtAuthenticationFilter que se encarga
de recoger las solicitudes a los endpoint, comprobar que el token es correcto y está en vigor y dar o no acceso, y 
alguna clase más.
* models/auth: En este directorio encontramos los modelos que utilizo para la entrada y salida de datos para el registro
o el login.
* Aparte también tenemos un Service con su implementación para las distintas operaciones o un controlador (auth) para los
endpoint de login y registro.

Para las pruebas dispongo de dos usuarios ya creados en la base de datos que son:
* ADMIN: user: admin, password: admin
* USER: user: user, password: admin

La base de datos para este proyecto la creo con docker en un contenedor con el siguiente nombre:
* postgres-block7-spring-security

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.6/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.6/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

