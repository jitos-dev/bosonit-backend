package com.bosonit.garciajuanjo.block7crudvalidation.config;

import com.bosonit.garciajuanjo.block7crudvalidation.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Esta es la clase de configuración que se encargará de conectar toda la seguridad y por eso va anotada con
 *
 * @author Juan José García Navarrete
 * @EnableWebSecurity. Al comenzar la aplicación buscará esta clase que es la encargada de configurar toda la
 * seguridad de la aplicación.
 */
@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)  //para habilitar la seguridad con Roles en los endpoint con @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfiguration {

    //Para la gestión de sesiones. Si no las usamos para cada solicitud tendrán que autenticarse
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final String admin = Role.ADMIN.name();
    private final String user = Role.USER.name();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET).hasAnyAuthority(admin, user)
                        .requestMatchers(HttpMethod.POST).hasAuthority(admin)
                        .requestMatchers(HttpMethod.PUT).hasAuthority(admin)
                        .requestMatchers(HttpMethod.DELETE).hasAuthority(admin)
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}
