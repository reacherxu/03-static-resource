package com.richard.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())//  disable CSRF, otherwise POST/PUT/DELETE will 403
//                .oauth2ResourceServer(oauth2 -> oauth2.disable())  // disable OAuth2 / JWT validation
                .anonymous(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))  // allow H2 Console display
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //  no need form Login/basic
                .httpBasic(httpBasic -> httpBasic.disable()).formLogin(form -> form.disable()).logout(logout -> logout.disable());
        return http.build();
    }
}