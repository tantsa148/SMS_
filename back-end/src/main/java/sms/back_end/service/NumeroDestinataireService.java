package sms.back_end.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import sms.back_end.dto.NumeroDestinataireRequest;
import sms.back_end.dto.NumeroDestinataireResponse;
import sms.back_end.entity.NumeroDestinataire;
import sms.back_end.exception.BadRequestException;
import sms.back_end.exception.NotFoundException;
import sms.back_end.repository.NumeroDestinataireRepository;

@Service
public class NumeroDestinataireService {

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
}

    // ============================
    // CREATE
    // ============================
    public NumeroDestinataireResponse createNumero(NumeroDestinataireRequest request) {

    Optional<NumeroDestinataire> existant = repository.findByValeurNumero(request.getValeur());

    NumeroDestinataire numero;

    // ------------------------------------
    // CAS 1 : Le numéro existe déjà
    // ------------------------------------
    if (existant.isPresent()) {

        numero = existant.get();

        // Si une plateforme est fournie
        if (request.getPlateformeId() != null) {

    // Vérifie si la plateforme existe
    plateformeService.getPlateformeOrThrow(request.getPlateformeId());

    boolean dejaAssocie = disponibleSurService.existeAssociation(
            numero.getIdNumero(), request.getPlateformeId()
    );

    if (!dejaAssocie) {
        disponibleSurService.addDisponible(numero.getIdNumero(), request.getPlateformeId());
        NumeroDestinataireResponse response = buildResponseDTO(numero);
        response.setMessage("Plateforme ajoutée avec succès au numéro.");
        return response;
    }

    // Déjà associé
    NumeroDestinataireResponse response = buildResponseDTO(numero);
    response.setMessage("Le numéro est déjà sur cette plateforme.");
    return response;
}


        // Aucun changement (pas de plateforme fournie)
        NumeroDestinataireResponse response = buildResponseDTO(numero);
        response.setMessage("Le numéro existe déjà.");
        return response;
    }

    // ------------------------------------
    // CAS 2 : Le numéro n'existe pas → créer
    // ------------------------------------
    numero = new NumeroDestinataire();
    numero.setValeurNumero(request.getValeur());

    NumeroDestinataire saved = repository.save(numero);

    if (request.getPlateformeId() != null) {
        disponibleSurService.addDisponible(saved.getIdNumero(), request.getPlateformeId());
    }

    NumeroDestinataireResponse response = buildResponseDTO(saved);
    response.setMessage("Numéro ajoutee avec succès !");
    return response;
}

    // ============================
    // READ ALL
    // ============================
    public List<NumeroDestinataireResponse> getAllNumeros() {
        return repository.findAll()
                .stream()
                .map(this::buildResponseDTO)
                .collect(Collectors.toList());
    }

    // ============================
    // READ BY ID
    // ============================
    public NumeroDestinataireResponse getNumeroById(Long id) {
    NumeroDestinataire numero = repository.findById(id)
            .orElseThrow(() ->
                    new NotFoundException("Le numéro destinataire avec l'ID " + id + " est introuvable"));

    return buildResponseDTO(numero);
}

    // ============================
    // UPDATE
    // ============================
    public NumeroDestinataire updateNumero(Long id, NumeroDestinataire updatedNumero) {

        // Vérifie si changement en doublon
        Optional<NumeroDestinataire> existant = repository.findByValeurNumero(
                updatedNumero.getValeurNumero()
        );
        if (existant.isPresent() && !existant.get().getIdNumero().equals(id)) {
            throw new BadRequestException("Ce numéro existe déjà !");
        }

        return repository.findById(id)
                .map(numero -> {
                    numero.setValeurNumero(updatedNumero.getValeurNumero());
                    return repository.save(numero);
                })
                .orElseThrow(() ->
                        new NotFoundException("Numéro destinataire ID=" + id + " non trouvé"));
    }

    // ============================
    // DELETE
    // ============================
    public void deleteNumero(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Impossible de supprimer : numéro ID=" + id + " introuvable");
        }
        repository.deleteById(id);
    }

    // ============================
    // UTILITAIRE DTO
    // ============================
private NumeroDestinataireResponse buildResponseDTO(NumeroDestinataire numero) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    NumeroDestinataireResponse dto = new NumeroDestinataireResponse();
    dto.setId(numero.getIdNumero());
    dto.setValeur(numero.getValeurNumero());
    dto.setDateCreation(numero.getDateCreation().format(formatter));

    // ✅ Récupérer toutes les plateformes associées sous forme de liste
    List<String> nomsPlateformes = disponibleSurService.getListeNomsPlateformesByNumeroId(numero.getIdNumero());
    dto.setPlateformes(nomsPlateformes);

    return dto;
}

    // ============================
    // GET valeur du numéro
    // ============================
    public String getValeurNumeroById(Long id) {
        NumeroDestinataire destinataire = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Destinataire avec ID=" + id + " non trouvé"));

        return destinataire.getValeurNumero();
    }
}
