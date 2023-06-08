package com.bosonit.garciajuanjo.block7crudvalidation.config;

import com.bosonit.garciajuanjo.block7crudvalidation.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Esta va a ser una clase de configuración donde vamos a tener los Beans que nos puedan ser necesarios para
 * la configuración de la parte de seguridad.
 * @author Juan José García Navarrete
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    //Repositorio
    private final PersonRepository personRepository;

    /**
     * UserDetailsService es implementada por UserDetails y se encarga de guardar los datos del usuario (Person para
     * este ejmplo). Podemos devolver un objeto Person porque implementa la interface UserDetails
     * @return objeto Person que contiene los datos de UserDetails
     */
    @Bean
    public UserDetailsService userDetailsService() {
        //Implementamos el método loadUserByUserName con un lambda ( return new UserDetails...)
        return username -> personRepository.findByUser(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in ApplicationConfig"));
    }

    /**
     * AuthenticationProvider es el responsable de obtener los datos del usuario y de codificar la contraseña.
     * Tenemos varias implementaciones de esta interface pero usaremos DaoAuthenticationProvider
     * @return objeto AuthenticationProvider (El proveedor de autenticación)
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        //Creamos el objeto y le especificamos lo que necesitemos (UserDetailsService,...)
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    /**
     * Este objeto AuthenticationManager va a ser el administrador de la seguridad
     * @param configuration configuración de la seguridad
     * @return objeto AuthenticationManager
     * @throws Exception que se puede producir
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
