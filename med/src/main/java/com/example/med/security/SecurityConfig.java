package com.example.med.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints - Authentication
                .requestMatchers("/api/auth/**").permitAll()

                // Public endpoints - Registration
                .requestMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/societes").permitAll()

                // Public endpoints - Catalogue (lecture seule)
                .requestMatchers(HttpMethod.GET, "/api/vehicules/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/vehicules/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/vehicules").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/catalogue/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/options/**").permitAll()

                // Public endpoints - Forms (Bridge Pattern)
                .requestMatchers(HttpMethod.GET, "/api/form/**").permitAll()

                // Public endpoints - Static files
                .requestMatchers("/uploads/**").permitAll()

                // Admin only endpoints - Stats
                .requestMatchers("/api/stats/**").hasRole("ADMIN")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // Admin only - CRUD vehicules (sauf GET)
                .requestMatchers(HttpMethod.POST, "/api/vehicules/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/vehicules").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/vehicules/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/vehicules/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/vehicules/**").hasRole("ADMIN")
                // Custom endpoints (sans /api prefix)
                .requestMatchers(HttpMethod.POST, "/vehicules").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/vehicules/").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/vehicules/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/vehicules/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/vehicules").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/vehicules/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/vehicules").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/vehicules/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/vehicules").hasRole("ADMIN")

                // Admin only - Gestion clients/societes
                .requestMatchers(HttpMethod.GET, "/api/clients").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/societes").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/clients/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/societes/**").hasRole("ADMIN")

                // Authenticated users - Commandes et panier
                .requestMatchers("/api/commandes/**").authenticated()
                .requestMatchers("/api/panier/**").authenticated()

                // Les utilisateurs peuvent modifier leur propre profil
                .requestMatchers(HttpMethod.GET, "/api/clients/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/clients/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/societes/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/societes/**").authenticated()

                // Liasse documents - authenticated
                .requestMatchers("/api/liasses/**").authenticated()
                .requestMatchers("/api/documents/**").authenticated()

                // Tout le reste requiert authentification
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
