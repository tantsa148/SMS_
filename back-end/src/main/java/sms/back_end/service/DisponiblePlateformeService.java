package sms.back_end.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import sms.back_end.dto.DisponiblePlateformeDTO;
import sms.back_end.entity.DisponiblePlateforme;
import sms.back_end.entity.DisponiblePlateformeId;
import sms.back_end.entity.Plateforme;
import sms.back_end.repository.DisponiblePlateformeRepository;
import sms.back_end.repository.PlateformeRepository;

@Service
public class DisponiblePlateformeService {

    private final DisponiblePlateformeRepository repository;
    private final PlateformeRepository plateformeRepository;

    public DisponiblePlateformeService(DisponiblePlateformeRepository repository,
                                      PlateformeRepository plateformeRepository) {
        this.repository = repository;
        this.plateformeRepository = plateformeRepository;
    }

    // ============================
    // Ajouter une association et renvoyer le DTO
    // ============================
    public DisponiblePlateformeDTO addDisponible(Long idNumero, Long idPlateforme) {
        DisponiblePlateformeId id = new DisponiblePlateformeId(idNumero, idPlateforme);
        DisponiblePlateforme dispo;

        if (!repository.existsById(id)) {
            dispo = new DisponiblePlateforme(id);
            repository.save(dispo);
        } else {
            dispo = repository.findById(id).orElse(null);
        }

        // Récupérer le nom de la plateforme
        String nomPlateforme = plateformeRepository.findById(idPlateforme)
                .map(Plateforme::getNomPlateforme)
                .orElse("");

        return new DisponiblePlateformeDTO(dispo, nomPlateforme);
    }

    // ============================
    // Récupérer toutes les associations pour un numéro
    // ============================
    public List<DisponiblePlateforme> getAllByNumeroId(Long idNumero) {
        return repository.findById_IdNumero(idNumero);
    }

    // ============================
    // Récupérer le nom de la première plateforme associée à un numéro
    // ============================
    public List<String> getNomsPlateformesByNumeroId(Long idNumero) {
    return repository.findById_IdNumero(idNumero)
            .stream()
            .map(dispo -> dispo.getId().getIdPlateforme())
            .map(id -> plateformeRepository.findById(id)
                    .map(Plateforme::getNomPlateforme)
                    .orElse(null))
            .filter(nom -> nom != null)
            .collect(Collectors.toList());
}


    // ============================
    // Supprimer une association
    // ============================
    public void removeDisponible(Long idNumero, Long idPlateforme) {
        DisponiblePlateformeId id = new DisponiblePlateformeId(idNumero, idPlateforme);
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }
    
    // ======================================
    // voir si une association  existe deja
    // ======================================
    public boolean existeAssociation(Long idNumero, Long idPlateforme) {
    return repository.existsById_IdNumeroAndId_IdPlateforme(idNumero, idPlateforme);
}

}
