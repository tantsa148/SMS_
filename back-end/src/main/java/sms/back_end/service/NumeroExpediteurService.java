package sms.back_end.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import sms.back_end.dto.NumeroExpediteurResponse;
import sms.back_end.entity.NumeroExpediteur;
import sms.back_end.entity.Possede;
import sms.back_end.exception.BadRequestException;
import sms.back_end.repository.NumeroExpediteurRepository;

@Service
public class NumeroExpediteurService {

    private final NumeroExpediteurRepository repository;
    private final PossedeService possedeService;
    private final DisponiblePlateformeService disponiblePlateformeService;

    public NumeroExpediteurService(NumeroExpediteurRepository repository,
                                   PossedeService possedeService,
                                   DisponiblePlateformeService disponiblePlateformeService) {
        this.repository = repository;
        this.possedeService = possedeService;
        this.disponiblePlateformeService = disponiblePlateformeService;
    }

    // ============================
    // CREATE OR ATTACH PLATFORM
    // ============================
    public NumeroExpediteurResponse createOrAttachPlateforme(
            String valeur, Long userId, Long idPlateforme, String username) {

        Optional<NumeroExpediteur> existant = repository.findByValeur(valeur);

        NumeroExpediteur numero;
        String message;

        if (existant.isPresent()) {
            // 🔹 Numéro existe déjà → seulement ajouter la plateforme
            numero = existant.get();

            // Vérifier association plateforme
            boolean dejaAssocie = disponiblePlateformeService
                    .existeAssociation(numero.getId(), idPlateforme);

            if (dejaAssocie) {
                return toDto(numero, userId, username,
                        "❌ Le numéro existe déjà et est déjà associé à cette plateforme");
            }

            // Associer la plateforme
            disponiblePlateformeService.addDisponible(numero.getId(), idPlateforme);

            message = "✔️ Le numéro existant a été associé avec succès à la plateforme";
        } else {
            // 🔹 Création du numéro
            numero = new NumeroExpediteur();
            numero.setValeur(valeur);
            numero.setDateCreation(LocalDateTime.now());
            numero = repository.save(numero);

            // Associer user
            possedeService.addNumeroToUser(userId, numero.getId());

            // Associer plateforme
            if (idPlateforme != null) {
                disponiblePlateformeService.addDisponible(numero.getId(), idPlateforme);
            }

            message = "✔️ Numéro créé et associé avec succès";
        }

        return toDto(numero, userId, username, message);
    }

    // ============================
    // GET NUMEROS DU USER
    // ============================
    public List<NumeroExpediteur> getNumerosByUser(Long userId) {
        List<Possede> relations = possedeService.getByUserId(userId);

        return relations.stream()
                .map(rel -> repository.findById(rel.getNumeroId()).orElse(null))
                .filter(num -> num != null)
                .collect(Collectors.toList());
    }

    // ============================
    // READ ALL
    // ============================
    public List<NumeroExpediteur> getAllNumeros() {
        return repository.findAll();
    }

    // ============================
    // READ BY ID
    // ============================
    public Optional<NumeroExpediteur> getNumeroById(Long id) {
        return repository.findById(id);
    }

    // ============================
    // UPDATE NUMERO + plateforme
    // ============================
    public NumeroExpediteurResponse updateNumero(Long id, String nouvelleValeur, Long idPlateforme,
                                                 Long userId, String username) {

        Optional<NumeroExpediteur> existant = repository.findByValeur(nouvelleValeur);
        if (existant.isPresent() && !existant.get().getId().equals(id)) {
            return toDto(existant.get(), userId, username,
                    "❌ Ce numéro existe déjà, impossible de le modifier");
        }

        NumeroExpediteur updated = repository.findById(id)
                .map(numero -> {
                    numero.setValeur(nouvelleValeur);
                    return repository.save(numero);
                }).orElseThrow(() -> new BadRequestException("Numéro non trouvé"));

        if (idPlateforme != null) {
            boolean dejaAssocie = disponiblePlateformeService
                    .existeAssociation(id, idPlateforme);

            if (!dejaAssocie) {
                disponiblePlateformeService.addDisponible(id, idPlateforme);
            }
        }

        return toDto(updated, userId, username,
                "✔️ Numéro mis à jour avec succès");
    }

    // ============================
    // DELETE
    // ============================
    public NumeroExpediteurResponse deleteNumero(Long id) {
        Optional<NumeroExpediteur> numero = repository.findById(id);

        if (numero.isEmpty()) {
            return new NumeroExpediteurResponse(null, null, null, null, null, null,
                    "❌ Numéro introuvable");
        }

        repository.deleteById(id);

        return new NumeroExpediteurResponse(
                id,
                numero.get().getValeur(),
                numero.get().getDateCreation().toString(),
                null,
                null,
                null,
                "✔️ Numéro supprimé avec succès"
        );
    }

    // ============================
    // MAPPING ENTITY → DTO
    // ============================
    public NumeroExpediteurResponse toDto(NumeroExpediteur numero,
                                           Long userId,
                                           String username,
                                           String message) {

        // 🔥 Récupère TOUTES les plateformes liées au numéro
        List<String> plateformes = disponiblePlateformeService
                .getNomsPlateformesByNumeroId(numero.getId());

        return new NumeroExpediteurResponse(
                numero.getId(),
                numero.getValeur(),
                numero.getDateCreation().toString(),
                userId,
                username,
                plateformes,
                message
        );
    }
}
