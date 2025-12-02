package sms.back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sms.back_end.entity.VueHistoriqueSms;

public interface VueHistoriqueSmsRepository
        extends JpaRepository<VueHistoriqueSms, Long> {
}
