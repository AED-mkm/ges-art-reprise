package com.gest.art.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gest.art.parametre.entite.dto.PasswordDTO;
import com.gest.art.parametre.service.PasswordService;
import com.gest.art.security.config.JwtService;
import com.gest.art.security.config.mailer.EmailService;
import com.gest.art.security.repository.RoleRepository;
import com.gest.art.security.repository.TokenRepository;
import com.gest.art.security.repository.UserRepository;
import com.gest.art.security.token.Token;
import com.gest.art.security.token.TokenType;
import com.gest.art.security.user.Role;
import com.gest.art.security.user.User;
import com.gest.art.security.user.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final PasswordService passwordService;
    private final RoleRepository roleRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Transactional
    public AuthenticationResponse register(UserDTO request) {

        List<Role> cli = roleRepository.findByName("USER");
        System.out.println("role user" + cli.stream().toList());
        var user = User.builder()
                .statut(request.getStatut())
                .police(request.getPolice())
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .telephone(request.getTelephone().replace("-", ""))
                .dateDebut(request.getDateDebut())
                .dateFin(request.getDateFin())
                .enabled(request.getEnabled())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(new HashSet<>(cli))
                .build();
        if (repository.existsByEmail(user.getEmail())) {
            ResponseEntity.badRequest().body("Error: Email est déjà utilisé!");
            return null;

        }

        var jwtToken = jwtService.generateToken(user);
        user.setVerificationToken(jwtToken);
        var savedUser = repository.save(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String validateVerificationToken(String token) {
        User user = repository.findByVerificationToken(token).orElse(null);
        String res;
        if (user == null) {
            res = "invalid";
        } else {
            user.setEnabled(true);
            repository.save(user);
            res = "valid";
        }
        return res;
    }

    @Transactional
    public AuthenticationResponse registerInterene(UserDTO request) {
        List<PasswordDTO> passDemoList = passwordService.findAll().stream().filter(PasswordDTO::isEtat).toList();
        var user = User.builder()
                .statut(request.getStatut())
                .police(request.getPolice())
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .telephone(request.getTelephone().replace("-", ""))
                .dateDebut(request.getDateDebut())
                .dateFin(request.getDateFin())
                .enabled(true)
                .password(passwordEncoder.encode(passDemoList.get(0).getLibelle()))
                .roles(request.getRoleDTOS())
                .build();
        if (repository.existsByEmail(user.getEmail())) {
            ResponseEntity.badRequest().body("Error: Email est déjà utilisé!");
            return null;
        }
        var jwtToken = jwtService.generateToken(user);
        user.setVerificationToken(jwtToken);
        var savedUser = repository.save(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        if (user.getEnabled()) {
            User userDTO = repository.findById(user.getId()).orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return AuthenticationResponse.builder()
                    .authUser(UserDTO.fromEntity(userDTO))
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            return null;
        }


    }

    @Transactional
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Transactional
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Transactional
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Transactional
    public Optional<User> findUser(String email) {
        return repository.findByEmail(email);
    }

    @Transactional
    public List<User> findAllUser() {
        return repository.findAll();
    }
}
