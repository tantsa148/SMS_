package sms.back_end.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sms.back_end.entity.NumeroDestinataire;

public interface NumeroDestinataireRepository extends JpaRepository<NumeroDestinataire, Long> {
    Optional<NumeroDestinataire> findByValeurNumero(String valeurNumero);
}
