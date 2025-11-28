package sms.back_end.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sms.back_end.dto.NumeroDestinataireRequest;
import sms.back_end.dto.NumeroDestinataireResponse;
import sms.back_end.entity.NumeroDestinataire;
import sms.back_end.service.NumeroDestinataireService;

@RestController
@RequestMapping("/api/numero-destinataire")
public class NumeroDestinataireController {

    private final NumeroDestinataireService service;

    @Autowired
    public NumeroDestinataireController(NumeroDestinataireService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<NumeroDestinataireResponse> createNumero(@RequestBody NumeroDestinataireRequest request) {
    NumeroDestinataireResponse savedNumero = service.createNumero(request);
    return ResponseEntity.ok(savedNumero);
    }


    // READ ALL
    @GetMapping
    public ResponseEntity<List<NumeroDestinataireResponse>> getAllNumeros() {
        List<NumeroDestinataireResponse> numeros = service.getAllNumeros();
        return ResponseEntity.ok(numeros);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<NumeroDestinataireResponse> getNumeroById(@PathVariable Long id) {
        NumeroDestinataireResponse numero = service.getNumeroById(id);
        return ResponseEntity.ok(numero);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<NumeroDestinataire> updateNumero(
            @PathVariable Long id,
            @RequestBody NumeroDestinataire updatedNumero) {
        try {
            NumeroDestinataire numero = service.updateNumero(id, updatedNumero);
            return ResponseEntity.ok(numero);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNumero(@PathVariable Long id) {
        service.deleteNumero(id);
        return ResponseEntity.noContent().build();
    }
}
