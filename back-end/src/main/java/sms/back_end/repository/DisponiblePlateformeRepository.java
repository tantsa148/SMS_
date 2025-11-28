package sms.back_end.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sms.back_end.entity.DisponiblePlateforme;
import sms.back_end.entity.DisponiblePlateformeId;

public interface DisponiblePlateformeRepository extends JpaRepository<DisponiblePlateforme, DisponiblePlateformeId> {

    // Récupérer toutes les plateformes disponibles pour un numéro
    List<DisponiblePlateforme> findById_IdNumero(Long idNumero);
    boolean existsById_IdNumeroAndId_IdPlateforme(Long idNumero, Long idPlateforme);

}
