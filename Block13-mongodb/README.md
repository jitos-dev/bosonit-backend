# Read Me
Este proyecto es para una app de prueba con la base de datos de MongoDb y voy a utilizar MongoTemplate. La base de datos
la creo en un contenedor de docker con el mismo nombre de la aplicación y tambien creo otro contenedor para correr el 
cliente Mongo-express. 

Nombres de los contenedores:
* Contenedor de Mongodb: block13-mongodb
* Contenedor de Mongo-express: block13-mongo-express
* La red de los contenedores: block13-mongodb

Los comandos que he utilizado para crear los contenedores son:
* Contenedor de Mongodb: 
docker run -p 27017:27017 -v C:\databases\MongoDB\block13-mongodb:/data/db -e MONGO_INITDB_ROOT_USERNAME=root 
-e MONGO_INITDB_ROOT_PASSWORD=root --network block13-mongodb --name block13-mongodb -d mongo:6.0.6

* Contenedor de Mongo-express:
  docker run -p 8081:8081  --network block13-mongodb --name block13-mongo-express -e ME_CONFIG_OPTIONS_EDITORTHEME="dracula" 
-e ME_CONFIG_MONGODB_SERVER=block13-mongodb -e ME_CONFIG_MONGODB_AUTH_USERNAME=root -e ME_CONFIG_MONGODB_AUTH_PASSWORD=root 
-d mongo-express

El contenedor de Mongo-express para que se conecte con el de Mongodb hay que ponerle las mismas configuraciones de user
y password que al de Mongodb. Aparte tiene el apartado de ME_CONFIG_MONGODB_SERVER que tiene que tener como valor el
nombre del contenedor de Mongodb y EDITORTHEME que eso no es obligatorio pero es para que se vea mejor Mongo-express.

Si queremos ejecutar la consola del contenedor de Mongodb hay que ejecutar lo siguiente:
* docker exec -it nombre_contenedor mongosh -u root -p root

Una vez hecho todo esto podemos acceder a la consola de Mongo o si queremos ver las bases de datos en el cliente
Mongo-express en el navegador accedemos a el con: localhost:8081 (el puerto que hemos dado de salida en el contenedor)

Páginas importantes que podemos visitar para este proyecto:
* [Mongodb Shell](https://www.mongodb.com/docs/mongodb-shell/reference/methods/)
* [Mongosh - Collection Methods](https://www.mongodb.com/docs/manual/reference/method/js-collection/)
* [Cambios en Mongodb Shell](https://www.mongodb.com/docs/mongodb-shell/reference/compatibility/#std-label-compatibility)
* [Comando mas utilizados](https://geekflare.com/es/mongodb-queries-examples/)

### Importante antes de ejecutar la aplicación
Antes de ejecutar la aplicación tenemos que crearnos un usuario para la base de datos porque el usuario que especificamos
(root) se crea sobre la base de datos admin y no lo podemos utilizar. Para esto ejecutamos:
* Primero nos situamos sobre la base de datos que queremos crear: use nombre_base_datos
* Segundo creamos el usuario de la siguiente forma:

db.createUser({user: "root",pwd: "root",roles: [{role: "readWrite", db: "block13-mongodb"}]})

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.0/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.0/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.1.0/reference/htmlsingle/#web)
* [Spring Data MongoDB](https://docs.spring.io/spring-boot/docs/3.1.0/reference/htmlsingle/#data.nosql.mongodb)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb/)

