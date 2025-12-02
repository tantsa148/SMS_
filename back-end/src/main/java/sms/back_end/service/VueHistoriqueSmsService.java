package sms.back_end.service;

import java.util.List;

import org.springframework.stereotype.Service;

import sms.back_end.entity.VueHistoriqueSms;
import sms.back_end.repository.VueHistoriqueSmsRepository;

@Service
public class VueHistoriqueSmsService {

    private final VueHistoriqueSmsRepository repository;

    public VueHistoriqueSmsService(VueHistoriqueSmsRepository repository) {
        this.repository = repository;
    }

    public List<VueHistoriqueSms> getAll() {
        return repository.findAll();
    }

    public VueHistoriqueSms getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log non trouvé"));
    }
}
