package sms.back_end.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import sms.back_end.dto.DisponibleSurWithPlateformeName;
import sms.back_end.entity.DisponibleSur;
import sms.back_end.entity.DisponibleSurId;
import sms.back_end.entity.Plateforme;
import sms.back_end.repository.DisponibleSurRepository;
import sms.back_end.repository.PlateformeRepository;

@Service
public class DisponibleSurService {

    private final DisponibleSurRepository repository;
    private final PlateformeRepository plateformeRepository;

    public DisponibleSurService(DisponibleSurRepository repository, PlateformeRepository plateformeRepository) {
        this.repository = repository;
        this.plateformeRepository = plateformeRepository;
    }

    // CREATE ou INSERT et retourne DTO avec le nom de la plateforme
    public DisponibleSurWithPlateformeName addDisponible(Long idNumero, Long idPlateforme) {
        // Créer l'entité DisponibleSur
        DisponibleSurId id = new DisponibleSurId(idNumero, idPlateforme);
        DisponibleSur disponible = new DisponibleSur(id);
        DisponibleSur saved = repository.save(disponible);

        // Récupérer le nom de la plateforme
        String nomPlateforme = plateformeRepository.findById(idPlateforme)
                .map(Plateforme::getNomPlateforme)
                .orElse("");

        // Retourner le DTO
        return new DisponibleSurWithPlateformeName(saved, nomPlateforme);
    }

    // READ ALL
    public List<DisponibleSur> getAllDisponibles() {
        return repository.findAll();
    }

    // READ BY ID
    public DisponibleSur getDisponibleById(Long idNumero, Long idPlateforme) {
        DisponibleSurId id = new DisponibleSurId(idNumero, idPlateforme);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Association non trouvée"));
    }

    // DELETE
    public void deleteDisponible(Long idNumero, Long idPlateforme) {
        DisponibleSurId id = new DisponibleSurId(idNumero, idPlateforme);
        repository.deleteById(id);
    }

    // Récupérer le nom de la plateforme d'un numéro
public List<String> getListeNomsPlateformesByNumeroId(Long idNumero) {
    return repository.findById_IdNumero(idNumero)
                     .stream()
                     .map(dp -> plateformeRepository.findById(dp.getId().getIdPlateforme())
                                                    .map(Plateforme::getNomPlateforme)
                                                    .orElse(""))
                     .collect(Collectors.toList());
}

    
    public boolean existeAssociation(Long idNumero, Long idPlateforme) {
    return repository.existsById(new DisponibleSurId(idNumero, idPlateforme));
}

}
