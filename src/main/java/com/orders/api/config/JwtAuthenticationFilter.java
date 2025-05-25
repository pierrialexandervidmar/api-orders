package com.orders.api.config;

import com.orders.api.entity.User;
import com.orders.api.repository.UserRepository;
import com.orders.api.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //  Essas rotas não precisam de autenticação JWT, pois são para registrar ou logar e gerar o token
        String path = request.getServletPath();
        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // O filtro tenta autenticar o usuário baseado no JWT presente no cabeçalho Authorization
        String authHeader = request.getHeader("Authorization");

        // Verifica se o Authorization está presente e começa com Bearer
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extrai o token
            String username = jwtService.extractUsername(token); // Extrai o username do token

            // Busca o usuário no banco
            User user = userRepository.findByUsername(username)
                    .orElse(null);

            // Valida se o token é legítimo
            if (user != null && jwtService.validateToken(token, user)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}

