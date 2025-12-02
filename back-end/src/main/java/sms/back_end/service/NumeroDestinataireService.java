package sms.back_end.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import sms.back_end.dto.NumeroDestinataireRequest;
import sms.back_end.dto.NumeroDestinataireResponse;
import sms.back_end.entity.NumeroDestinataire;
import sms.back_end.exception.BadRequestException;
import sms.back_end.exception.NotFoundException;
import sms.back_end.repository.NumeroDestinataireRepository;

@Service
public class NumeroDestinataireService {

    private static final Logger logger = LoggerFactory.getLogger(NumeroDestinataireService.class);
    
    private final NumeroDestinataireRepository repository;
    private final DisponibleSurService disponibleSurService;
    private final PlateformeService plateformeService;

    public NumeroDestinataireService(
        NumeroDestinataireRepository repository,
        DisponibleSurService disponibleSurService,
        PlateformeService plateformeService) {
    this.repository = repository;
    this.disponibleSurService = disponibleSurService;
    this.plateformeService = plateformeService;
    logger.info("NumeroDestinataireService initialisé avec repository: {}, disponibleSurService: {}, plateformeService: {}",
        repository != null, disponibleSurService != null, plateformeService != null);
}

    // ============================
    // CREATE
    // ============================
    public NumeroDestinataireResponse createNumero(NumeroDestinataireRequest request) {
        logger.info("⏩ DEBUT createNumero() - Numéro: {}, PlateformeId: {}", 
            request.getValeur(), request.getPlateformeId());
        
        try {
            Optional<NumeroDestinataire> existant = repository.findByValeurNumero(request.getValeur());
            logger.debug("Recherche du numéro existant: {}", request.getValeur());
            
            NumeroDestinataire numero;

            // CAS 1 : Le numéro existe déjà
            if (existant.isPresent()) {
                logger.info("📞 Numéro existant trouvé: {}", request.getValeur());
                numero = existant.get();

                // Si une plateforme est fournie
                if (request.getPlateformeId() != null) {
                    logger.debug("Vérification de la plateforme ID: {}", request.getPlateformeId());
                    
                    plateformeService.getPlateformeOrThrow(request.getPlateformeId());

                    boolean dejaAssocie = disponibleSurService.existeAssociation(
                        numero.getIdNumero(), request.getPlateformeId()
                    );
                    logger.debug("Association existante: {}", dejaAssocie);

                    if (!dejaAssocie) {
                        disponibleSurService.addDisponible(numero.getIdNumero(), request.getPlateformeId());
                        NumeroDestinataireResponse response = buildResponseDTO(numero);
                        response.setMessage("Plateforme ajoutée avec succès au numéro.");
                        logger.info("✅ Plateforme {} ajoutée au numéro {}", 
                            request.getPlateformeId(), request.getValeur());
                        return response;
                    }

                    // Déjà associé
                    NumeroDestinataireResponse response = buildResponseDTO(numero);
                    response.setMessage("Le numéro est déjà sur cette plateforme.");
                    logger.info("ℹ️ Numéro déjà associé à la plateforme");
                    return response;
                }

                // Aucun changement (pas de plateforme fournie)
                NumeroDestinataireResponse response = buildResponseDTO(numero);
                response.setMessage("Le numéro existe déjà.");
                logger.info("ℹ️ Numéro existe déjà, pas de plateforme à ajouter");
                return response;
            }

            // CAS 2 : Le numéro n'existe pas → créer
            logger.info("🆕 Création d'un nouveau numéro: {}", request.getValeur());
            numero = new NumeroDestinataire();
            numero.setValeurNumero(request.getValeur());

            NumeroDestinataire saved = repository.save(numero);
            logger.info("💾 Numéro sauvegardé avec ID: {}", saved.getIdNumero());

            if (request.getPlateformeId() != null) {
                logger.debug("Ajout de la plateforme ID: {} au nouveau numéro", request.getPlateformeId());
                disponibleSurService.addDisponible(saved.getIdNumero(), request.getPlateformeId());
            }

            NumeroDestinataireResponse response = buildResponseDTO(saved);
            response.setMessage("Numéro ajouté avec succès !");
            logger.info("✅ Création réussie pour le numéro: {}", request.getValeur());
            return response;
            
        } catch (Exception e) {
            logger.error("❌ ERREUR dans createNumero() pour le numéro {}: {}", 
                request.getValeur(), e.getMessage(), e);
            throw e; // Relancer l'exception pour la gestion globale
        }
    }

    // ============================
    // READ ALL
    // ============================
    public List<NumeroDestinataireResponse> getAllNumeros() {
        logger.debug("⏩ DEBUT getAllNumeros()");
        try {
            List<NumeroDestinataireResponse> result = repository.findAll()
                    .stream()
                    .map(this::buildResponseDTO)
                    .collect(Collectors.toList());
            logger.info("📋 getAllNumeros() retourne {} numéros", result.size());
            return result;
        } catch (Exception e) {
            logger.error("❌ ERREUR dans getAllNumeros(): {}", e.getMessage(), e);
            throw e;
        }
    }

    // ============================
    // READ BY ID
    // ============================
    public NumeroDestinataireResponse getNumeroById(Long id) {
        logger.debug("⏩ DEBUT getNumeroById() - ID: {}", id);
        try {
            NumeroDestinataire numero = repository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("⚠️ Numéro non trouvé avec ID: {}", id);
                        return new NotFoundException("Le numéro destinataire avec l'ID " + id + " est introuvable");
                    });
            
            logger.info("✅ Numéro trouvé: ID={}, Valeur={}", id, numero.getValeurNumero());
            return buildResponseDTO(numero);
        } catch (Exception e) {
            logger.error("❌ ERREUR dans getNumeroById({}): {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // ============================
    // UPDATE
    // ============================
    public NumeroDestinataire updateNumero(Long id, NumeroDestinataire updatedNumero) {
        logger.info("⏩ DEBUT updateNumero() - ID: {}, Nouvelle valeur: {}", 
            id, updatedNumero.getValeurNumero());
        
        try {
            // Vérifie si changement en doublon
            Optional<NumeroDestinataire> existant = repository.findByValeurNumero(
                    updatedNumero.getValeurNumero()
            );
            
            if (existant.isPresent() && !existant.get().getIdNumero().equals(id)) {
                logger.warn("⚠️ Tentative de duplication - Numéro {} existe déjà pour ID: {}", 
                    updatedNumero.getValeurNumero(), existant.get().getIdNumero());
                throw new BadRequestException("Ce numéro existe déjà !");
            }

            return repository.findById(id)
                    .map(numero -> {
                        String ancienneValeur = numero.getValeurNumero();
                        numero.setValeurNumero(updatedNumero.getValeurNumero());
                        NumeroDestinataire saved = repository.save(numero);
                        logger.info("✅ Numéro mis à jour - ID: {}, {} → {}", 
                            id, ancienneValeur, updatedNumero.getValeurNumero());
                        return saved;
                    })
                    .orElseThrow(() -> {
                        logger.warn("⚠️ Numéro non trouvé pour update - ID: {}", id);
                        return new NotFoundException("Numéro destinataire ID=" + id + " non trouvé");
                    });
        } catch (Exception e) {
            logger.error("❌ ERREUR dans updateNumero({}): {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // ============================
    // DELETE
    // ============================
    public void deleteNumero(Long id) {
        logger.info("⏩ DEBUT deleteNumero() - ID: {}", id);
        try {
            if (!repository.existsById(id)) {
                logger.warn("⚠️ Tentative de suppression d'un numéro inexistant - ID: {}", id);
                throw new NotFoundException("Impossible de supprimer : numéro ID=" + id + " introuvable");
            }
            
            repository.deleteById(id);
            logger.info("🗑️ Numéro supprimé avec succès - ID: {}", id);
        } catch (Exception e) {
            logger.error("❌ ERREUR dans deleteNumero({}): {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // ============================
    // UTILITAIRE DTO
    // ============================
    private NumeroDestinataireResponse buildResponseDTO(NumeroDestinataire numero) {
        logger.debug("⏩ DEBUT buildResponseDTO() pour ID: {}", numero.getIdNumero());
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            NumeroDestinataireResponse dto = new NumeroDestinataireResponse();
            dto.setId(numero.getIdNumero());
            dto.setValeur(numero.getValeurNumero());
            
            if (numero.getDateCreation() != null) {
                dto.setDateCreation(numero.getDateCreation().format(formatter));
            } else {
                dto.setDateCreation("Non définie");
                logger.warn("⚠️ Date de création nulle pour le numéro ID: {}", numero.getIdNumero());
            }

            // Récupérer toutes les plateformes associées
            List<String> nomsPlateformes = disponibleSurService.getListeNomsPlateformesByNumeroId(numero.getIdNumero());
            dto.setPlateformes(nomsPlateformes);
            
            logger.debug("✅ DTO construit pour ID: {} - Plateformes: {}", 
                numero.getIdNumero(), nomsPlateformes.size());
            
            return dto;
        } catch (Exception e) {
            logger.error("❌ ERREUR dans buildResponseDTO({}): {}", numero.getIdNumero(), e.getMessage(), e);
            throw e;
        }
    }

    // ============================
    // GET valeur du numéro
    // ============================
    public String getValeurNumeroById(Long id) {
        logger.debug("⏩ DEBUT getValeurNumeroById() - ID: {}", id);
        try {
            NumeroDestinataire destinataire = repository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("⚠️ Destinataire non trouvé pour getValeurNumeroById() - ID: {}", id);
                        return new NotFoundException("Destinataire avec ID=" + id + " non trouvé");
                    });

            String valeur = destinataire.getValeurNumero();
            logger.debug("✅ getValeurNumeroById() retourne: {} pour ID: {}", valeur, id);
            return valeur;
        } catch (Exception e) {
            logger.error("❌ ERREUR dans getValeurNumeroById({}): {}", id, e.getMessage(), e);
            throw e;
        }
    }

}