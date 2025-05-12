package com.gest.art.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/login",
            "/api/v1/auth/login/**",
            "/api/v1/auth/register",
            "/api/v1/auth/logout",
            "/api/v1/auth/refresh-token",
            "/api/v1/auth/verify-email",
            "/api/v1/auth/verify-email/**",
            "/verify-email/**",
            "/api/v1/report/**",
            "/api/v1/magasins",
            "/api/v1/passwords",
            "/api/v1/banques",
            "/api/v1/banques/**",
            "/api/v1/taxes",
            "/api/v1/taxes/**",
            "/api/v1/succursales",
            "/api/v1/succursales/**",
            "/api/v1/produits",
            "/api/v1/produits/**",
            "/api/v1/fournisseurs",
            "/api/v1/fournisseurs/**",
            "/api/v1/clients",
            "/api/v1/clients/**",
            "/api/v1/entres",
            "/api/v1/entres/**",
            "/api/v1/ventes",
            "/api/v1/ventes/**",
            "/api/v1/typeDocuments",
            "/api/v1/clean/clear",
            "/api/v1/users",
            "/api/v1/resources/**",
            "/api/v1/uploadFile/**",
            "/api/v1/download/**",
            "/api/v1/upload/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/verify-email",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider, LogoutHandler logoutHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.logoutHandler = logoutHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .anyRequest()
                                //.permitAll()
                                .authenticated()
                )
                //.("/", "/index").permitAll()
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        //.logout(logout ->
        //       logout.logoutUrl("/api/v1/auth/logout")
        //              .addLogoutHandler(logoutHandler)
        //              .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
        //  )
        //  ;

        return http.build();
    }

}
