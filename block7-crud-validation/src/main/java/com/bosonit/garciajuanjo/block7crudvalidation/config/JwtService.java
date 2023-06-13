package com.bosonit.garciajuanjo.block7crudvalidation.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Esta clase será el servicio con la lógica de negocio que necesitemos realizar sobre el token JWT.
 * Un JWT token esta compuesto por varias partes (aunque no se vean) que podríamos ver en la siguiente página:
 *  - <a href="https://jwt.io/">jwt.io</a>
 * <p>
 * Estas partes son:
 *  - Header
 *  - Payload
 *  - Signature (firma)
 * La primera parte (header) consta del tipo de token y el algoritmo de cifrado (HS256 por ejemplo)
 * La segunda parte (Payload) contiene información de la entidad (Person en este ejemplo) como el user, role,
 * tiempo de expiración, etc.
 * La tercera parte (Signature) se utiliza para identificar al emisor y que la información no ha cambiado por el camino.
 *
 *
 * @author Juan José García Navarrete
 */
@Service
public class JwtService {

    //Esta clave la genero desde la página: https://www.allkeysgenerator.com/ aunque hay muchas
    //Es de tipo sha-256 con ex
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long expirationToken;
    /**
     * En este método extraemos el usuario del token que se pasa como parámetro
     * @param token de tipo String de donde se extrae el usuario
     * @return nombre de usuario del Person
     */
    public String extractUser(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * En este método extraeremos toda la información del token como su Header, Payload y Signature
     * @param token token del que queremos extraer la información
     * @return objeto Claims que representa esta información
     */
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey()) //se utiliza para especificar la key y comprobar que el mensaje no haya cambiado
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Este solo extrae el Claims que se le pase
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * En este método decodificamos la clase secreta que tenemos y creamos una Key con ella
     * @return objeto Key con la clave secreta
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Este método se encarga de generar un token con la información del usuario y los 'Claims' que le pasemos
     * @param extraClaims Claims para añadir al token
     * @param userDetails UserDetails para añadir al token
     * @return String con el token generado
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())) //Para calcular si el token ha expirado
                .setExpiration(new Date(System.currentTimeMillis() + expirationToken))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) //le pasamos la Key y el tipo de algoritmo utilizado
                .compact();

    }

    /**
     * Este método genera un token pero solo con los detalles del usuario, sin los Claims
     * @param userDetails UserDetails para añadir al token
     * @return String con el token generado
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        final String user = extractUser(token);
        return (user.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    //Si la fecha de expiración es anterior a la de hoy es que ha expirado
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }
}
