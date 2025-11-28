package sms.back_end.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sms.back_end.service.DisponiblePlateformeService;

@RestController
@RequestMapping("/api/test-disponible")
public class TestDisponiblePlateformeController {

    private final DisponiblePlateformeService disponibleService;

    public TestDisponiblePlateformeController(DisponiblePlateformeService disponibleService) {
        this.disponibleService = disponibleService;
    }

    // Endpoint pour ajouter une association
    @PostMapping("/add")
    public ResponseEntity<String> addDisponible(
            @RequestParam Long idNumero,
            @RequestParam Long idPlateforme) {

        disponibleService.addDisponible(idNumero, idPlateforme);
        return ResponseEntity.ok("Association créée avec succès !");
    }

    // Endpoint pour lister toutes les associations d’un numéro
    @GetMapping("/list")
    public ResponseEntity<?> listByNumero(@RequestParam Long idNumero) {
        return ResponseEntity.ok(disponibleService.getAllByNumeroId(idNumero));
    }
}
