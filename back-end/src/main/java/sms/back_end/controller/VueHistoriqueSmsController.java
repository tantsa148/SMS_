package sms.back_end.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sms.back_end.entity.VueHistoriqueSms;
import sms.back_end.service.VueHistoriqueSmsService;

@RestController
@RequestMapping("/api/historique-sms")
public class VueHistoriqueSmsController {

    private final VueHistoriqueSmsService service;

    public VueHistoriqueSmsController(VueHistoriqueSmsService service) {
        this.service = service;
    }

    @GetMapping
    public List<VueHistoriqueSms> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public VueHistoriqueSms getById(@PathVariable Long id) {
        return service.getById(id);
    }
}
