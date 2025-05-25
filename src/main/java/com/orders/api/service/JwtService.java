package com.orders.api.service;

import com.orders.api.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Serviço para geração, extração e validação de tokens JWT (JSON Web Tokens).
 * <p>
 * Utiliza uma chave secreta HMAC-SHA256 para assinar e validar os tokens.
 * Os tokens gerados contêm o nome do usuário como "subject" e a role do usuário como claim personalizada.
 * </p>
 */
@Service
public class JwtService {

    /**
     * Chave secreta usada para assinar os tokens JWT.
     * Deve conter exatamente 32 caracteres para garantir segurança adequada no algoritmo HS256.
     */
    private final String SECRET = "Kf7L9zQ2V3xW8NcYbD4R6MpZtHqXsJvU";

    /**
     * Chave criptográfica derivada da {@link #SECRET} para uso em assinaturas HMAC SHA-256.
     */
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * Tempo de expiração dos tokens em milissegundos (1 hora).
     */
    private final long EXPIRATION_MILLIS = 1000 * 60 * 60;

    /**
     * Gera um token JWT para o usuário informado.
     *
     * @param user Usuário para o qual o token será gerado.
     * @return Token JWT assinado contendo o username no "subject" e o role como claim.
     */
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())        // identifica o usuário
                .claim("role", user.getRole())          // adiciona role como claim personalizada
                .setIssuedAt(new Date())                 // data de emissão do token
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))  // data de expiração
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)  // assina o token usando HMAC SHA-256
                .compact();                             // gera a string compacta do token
    }

    /**
     * Retorna o timestamp de expiração padrão do token baseado no tempo atual.
     *
     * @return Timestamp (em milissegundos) que representa a data/hora de expiração do token.
     */
    public long getExpirationTimestamp() {
        return System.currentTimeMillis() + EXPIRATION_MILLIS;
    }

    /**
     * Extrai o nome do usuário (subject) de um token JWT válido.
     *
     * @param token Token JWT assinado.
     * @return Nome do usuário presente no campo "subject" do token.
     * @throws io.jsonwebtoken.JwtException se o token for inválido ou mal formatado.
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Valida se um token JWT é válido para o usuário informado.
     * Verifica se o token pertence ao usuário (username) e se não está expirado.
     *
     * @param token Token JWT a ser validado.
     * @param user  Usuário para comparação do username.
     * @return {@code true} se o token é válido e corresponde ao usuário; {@code false} caso contrário.
     */
    public boolean validateToken(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Verifica se um token JWT já expirou.
     *
     * @param token Token JWT a ser verificado.
     * @return {@code true} se o token está expirado; {@code false} caso contrário.
     */
    private boolean isTokenExpired(String token) {
        final Date expiration = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}

