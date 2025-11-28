package sms.back_end.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sms.back_end.dto.LoginRequestDTO;
import sms.back_end.dto.RegisterRequestDTO;
import sms.back_end.dto.UserDTO;
import sms.back_end.entity.User;
import sms.back_end.repository.UserRepository;
import sms.back_end.security.JwtUtils;
import sms.back_end.service.AuthService;
import sms.back_end.service.CustomUserDetailsService;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;
    
      @Autowired
    private UserRepository userRepository; // ❗ Ajouter ceci
    
    @Autowired
    private AuthService authService;
    // --- Login ---
    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
    try {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
            )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        // Récupérer l'id depuis la DB
        User user = userRepository.findByUsername(loginRequest.getUsername()).get();

        String token = jwtUtils.generateToken(user.getId(), user.getUsername());

        UserDTO userDTO = new UserDTO(user.getUsername(), user.getRole());

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Connexion réussie",
            "token", token,
            "user", userDTO
        ));

    } catch (BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
            "success", false,
            "message", "Nom d’utilisateur ou mot de passe incorrect"
        ));
    }
}

     @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequest) {
        try {
            User user = authService.register(
                    registerRequest.getUsername(),
                    registerRequest.getPassword(),
                    registerRequest.getRole()
            );

            UserDTO userDTO = new UserDTO(user.getUsername(), user.getRole());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Utilisateur créé avec succès",
                    "user", userDTO
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

@GetMapping("/me")
public ResponseEntity<?> getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || !(auth.getPrincipal() instanceof UserDetails)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "success", false,
                        "message", "Utilisateur non connecté !"
                ));
    }

    UserDetails userDetails = (UserDetails) auth.getPrincipal();

    // Récupérer l'utilisateur complet depuis la DB
    User user = userRepository.findByUsername(userDetails.getUsername())
            .orElse(null);

    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "success", false,
                        "message", "Utilisateur non trouvé en base !"
                ));
    }

    // Construire le DTO
    UserDTO userDTO = new UserDTO(user.getUsername(), user.getRole());

    return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Utilisateur connecté récupéré",
            "user", userDTO
    ));
}
}
