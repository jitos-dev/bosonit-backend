package com.bosonit.garciajuanjo.block7crudvalidation.config;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Esta clase será el filtro que tendrán que pasar todas las peticiones que se hagan a nuestra api y a través de
 * ella consultaremos a la base de datos para ver las credenciales, si el token es válido, etc.
 * Al heredar de OncePerRequestFilter hay que sobrescribir el método doFilterInternal que es el encargado de recibir
 * todas las peticiones a la api.
 *
 * @author Juan José García Navarrete
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        //Encabezado de autorización donde viene el token
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String user;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Extraemos el jwt del header. El 7 son las letras de arriba cuando empezaría el token (Bearer )
        jwt = authHeader.substring(7);
        user = jwtService.extractUser(jwt);

        //Esta parte se da cuando el usuario no se ha autenticado, por lo que tiene que autenticarse
        if (user == null || SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(user);
        }
    }
}
