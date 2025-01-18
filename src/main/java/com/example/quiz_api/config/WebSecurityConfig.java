package com.example.quiz_api.config;

import com.example.quiz_api.security.JwtAuthenticationEntryPoint;
import com.example.quiz_api.security.JwtAuthenticationFilter;
import com.example.quiz_api.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Crea un PasswordEncoder (BCrypt).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura un AuthenticationProvider que use tu CustomUserDetailsService.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Expone el AuthenticationManager como bean, usando la nueva forma.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configura la cadena de filtros de Spring Security (reemplaza a configure(HttpSecurity)).
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Inyectar nuestro DaoAuthenticationProvider
        http.authenticationProvider(authenticationProvider());

        // Configurar las reglas de seguridad
        http
                // (1) Deshabilitar CSRF para uso de JWT
                .csrf(csrf -> csrf.disable())

                // (2) Manejo de excepciones (401 unauthorized)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))

                // (3) No crear sesiones -> stateless
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // (4) Autorizaciones
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login/**", "/api/auth/**","/api/users/**")
                        .permitAll() // Endpoints libres
                        .anyRequest().authenticated() // El resto requiere auth
                );

//        .authorizeHttpRequests(auth -> auth
//                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
//                .requestMatchers("/api/login/**", "/api/auth/**").permitAll()
//                .anyRequest().authenticated()
//        )
        // (5) Insertar nuestro filtro JWT antes del UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Construir la configuración
        return http.build();
    }
}
