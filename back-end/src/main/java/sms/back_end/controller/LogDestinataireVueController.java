package sms.back_end.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import sms.back_end.entity.LogDestinataireVue;
import sms.back_end.repository.LogDestinataireVueRepository;

@RestController
public class LogDestinataireVueController {

    private final LogDestinataireVueRepository repository;

    public LogDestinataireVueController(LogDestinataireVueRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/historique")
    public List<LogDestinataireVue> getAllSmsResponses() {
        return repository.findAll(); // retourne tous les logs de la vue
    }
}
