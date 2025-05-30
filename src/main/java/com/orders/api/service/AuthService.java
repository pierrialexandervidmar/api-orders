package com.orders.api.service;

import com.orders.api.dto.AuthRequest;
import com.orders.api.dto.AuthResponse;
import com.orders.api.dto.RegisterRequest;
import com.orders.api.entity.User;
import com.orders.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Usuário já cadastrado.");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        Long expiresAtTimestamp = jwtService.getExpirationTimestamp();

        return new AuthResponse(token, expiresAtTimestamp);
    }

    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(user);
        Long expiresAt = jwtService.getExpirationTimestamp();
        return new AuthResponse(token, expiresAt);
    }
}

