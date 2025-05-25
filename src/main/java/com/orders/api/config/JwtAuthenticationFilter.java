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

/**
 * Filtro de autenticação JWT que intercepta requisições HTTP para validar e autenticar usuários baseados em tokens JWT.
 * <p>
 * Este filtro verifica o cabeçalho "Authorization" para extrair o token JWT,
 * valida o token, e se válido, autentica o usuário na segurança do Spring.
 * </p>
 * <p>
 * Exceções: rotas que iniciam com "/auth/" são ignoradas pois servem para login e registro (não precisam de autenticação).
 * </p>
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    /**
     * Construtor do filtro.
     *
     * @param jwtService     Serviço responsável pela lógica do JWT (geração, validação, extração).
     * @param userRepository Repositório para buscar informações do usuário no banco.
     */
    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    /**
     * Método principal do filtro que é executado para cada requisição HTTP.
     * <p>
     * Passos:
     * <ul>
     *   <li>Ignora requisições para rotas "/auth/*".</li>
     *   <li>Verifica o cabeçalho Authorization para o token JWT.</li>
     *   <li>Extrai o username do token e busca o usuário no banco.</li>
     *   <li>Valida o token e autentica o usuário no contexto de segurança do Spring.</li>
     *   <li>Continua a cadeia de filtros independentemente do resultado da autenticação.</li>
     * </ul>
     * </p>
     *
     * @param request     Requisição HTTP recebida.
     * @param response    Resposta HTTP a ser enviada.
     * @param filterChain Cadeia de filtros para delegar a execução após este filtro.
     * @throws ServletException Caso ocorra erro relacionado ao servlet.
     * @throws IOException      Caso ocorra erro de I/O.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Rotas que não precisam de autenticação JWT (login, registro, etc.)
        String path = request.getServletPath();
        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Captura o cabeçalho Authorization
        String authHeader = request.getHeader("Authorization");

        // Verifica se o token JWT está presente e é do tipo Bearer
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extrai o token JWT
            String token = authHeader.substring(7);

            // Extrai o username do token JWT
            String username = jwtService.extractUsername(token);

            // Busca o usuário no banco de dados pelo username
            User user = userRepository.findByUsername(username)
                    .orElse(null);

            // Se o usuário existir e o token for válido, autentica o usuário no Spring Security
            if (user != null && jwtService.validateToken(token, user)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continua a cadeia de filtros da aplicação
        filterChain.doFilter(request, response);
    }
}

