package sms.back_end.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sms.back_end.entity.DisponibleSur;
import sms.back_end.entity.DisponibleSurId;

@Repository
public interface DisponibleSurRepository extends JpaRepository<DisponibleSur, DisponibleSurId> {

    // Le chemin complet vers l'attribut dans la clé composite
    List<DisponibleSur> findById_IdNumero(Long idNumero);
    boolean existsById(DisponibleSurId id);

}
