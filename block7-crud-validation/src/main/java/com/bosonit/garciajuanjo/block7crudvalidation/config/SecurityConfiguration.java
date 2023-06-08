package com.bosonit.garciajuanjo.block7crudvalidation.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Esta es la clase de configuración que se encargará de conectar toda la seguridad y por eso va anotada con
 * @EnableWebSecurity. Al comenzar la aplicación buscará esta clase que es la encargada de configurar toda la
 * seguridad de la aplicación.
 *
 * @author Juan José García Navarrete
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    //Para la gestión de sesiones. Si no las usamos para cada solicitud tendrán que autenticarse
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf() //deshabilitamos la verificación por csrf
                .disable()
                .authorizeHttpRequests() // Empezamos con los endpoints que no hace falta un token para acceder
                .requestMatchers("")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //La política de sesión que queremos. Ejm Sin estado
                .and()
                .authenticationProvider(authenticationProvider) //Indicamos el proveedor de autenticación
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
