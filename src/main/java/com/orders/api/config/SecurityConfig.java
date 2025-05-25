package com.orders.api.config;

import com.orders.api.exception.CustomAccessDeniedHandler;
import com.orders.api.repository.UserRepository;
import com.orders.api.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) //Desabilita a proteção contra CSRF em REST usa JWT
                .formLogin(formLogin -> formLogin.disable()) // Desabilita o formulário de login html
                .httpBasic(httpBasic -> httpBasic.disable()) // Desabilita o HTTP Basic Auth, usamos JWT
                .authorizeHttpRequests(auth -> auth // Inicia a configuração de autorização: define quais endpoints são públicos e quais exigem autenticação.
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // Para que novos usuários possam se cadastrar sem precisar estar autenticados
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .anyRequest().authenticated() // Qualquer outra requisição deve ser autenticada.
                )
                .exceptionHandling(e -> e.accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement( // Em aplicações que usam JWT, o servidor não armazena sessões, cada requisição é independente e carrega sua autenticação no próprio token
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        // Responsável por interceptar as requisições, validar o token JWT e autenticar o usuário no contexto de segurança.
        return new JwtAuthenticationFilter(jwtService, userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


