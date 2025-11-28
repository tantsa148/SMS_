package sms.back_end.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sms.back_end.dto.NumeroExpediteurRequest;
import sms.back_end.dto.NumeroExpediteurResponse;
import sms.back_end.entity.NumeroExpediteur;
import sms.back_end.security.JwtUtils;
import sms.back_end.service.NumeroExpediteurService;

@RestController
@RequestMapping("/api/numero-expediteur")
public class NumeroExpediteurController {

    private final NumeroExpediteurService numeroService;
    private final JwtUtils jwtUtils;

    public NumeroExpediteurController(
            NumeroExpediteurService numeroService,
            JwtUtils jwtUtils) {
        this.numeroService = numeroService;
        this.jwtUtils = jwtUtils;
    }

    // ============================
    // UTIL - EXTRACTION USER
    // ============================
    private Long getUserId(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtils.getUserIdFromJwt(token);
    }

    private String getUsername(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtils.getUsernameFromJwt(token);
    }

    // ============================
    // CREATE OR ATTACH PLATEFORME
    // ============================
    @PostMapping
    public ResponseEntity<NumeroExpediteurResponse> createNumero(
            @RequestBody NumeroExpediteurRequest request,
            @RequestHeader("Authorization") String authHeader) {

        Long userId = getUserId(authHeader);
        String username = getUsername(authHeader);

        NumeroExpediteurResponse response = numeroService.createOrAttachPlateforme(
                request.getValeur(),
                userId,
                request.getIdPlateforme(),
                username
        );

        return ResponseEntity.ok(response);
    }

    // ============================
    // GET NUMEROS DU USER
    // ============================
    @GetMapping
    public ResponseEntity<List<NumeroExpediteurResponse>> getMesNumeros(
            @RequestHeader("Authorization") String authHeader) {

        Long userId = getUserId(authHeader);
        String username = getUsername(authHeader);

        List<NumeroExpediteur> numeros = numeroService.getNumerosByUser(userId);

        List<NumeroExpediteurResponse> responses = numeros.stream()
                .map(n -> numeroService.toDto(n, userId, username, "OK"))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    // ============================
    // GET PAR ID
    // ============================
    @GetMapping("/{id}")
    public ResponseEntity<NumeroExpediteurResponse> getNumeroById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        Long userId = getUserId(authHeader);
        String username = getUsername(authHeader);

        return numeroService.getNumeroById(id)
                .map(n -> ResponseEntity.ok(numeroService.toDto(n, userId, username, "OK")))
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================
    // UPDATE NUMERO
    // ============================
    @PutMapping("/{id}")
    public ResponseEntity<NumeroExpediteurResponse> updateNumero(
            @PathVariable Long id,
            @RequestBody NumeroExpediteurRequest request,
            @RequestHeader("Authorization") String authHeader) {

        Long userId = getUserId(authHeader);
        String username = getUsername(authHeader);

        NumeroExpediteurResponse response = numeroService.updateNumero(
                id,
                request.getValeur(),
                request.getIdPlateforme(),
                userId,
                username
        );

        return ResponseEntity.ok(response);
    }

    // ============================
    // DELETE NUMERO
    // ============================
    @DeleteMapping("/{id}")
    public ResponseEntity<NumeroExpediteurResponse> deleteNumero(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        Long userId = getUserId(authHeader); // si tu veux valider que le numéro appartient au user
        // TODO: dans le service → vérifier que userId == propriétaire

        NumeroExpediteurResponse response = numeroService.deleteNumero(id);

        return ResponseEntity.ok(response);
    }
}
