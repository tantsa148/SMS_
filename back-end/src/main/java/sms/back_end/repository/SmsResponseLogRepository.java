package sms.back_end.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sms.back_end.entity.SmsResponseLog;

public interface SmsResponseLogRepository extends JpaRepository<SmsResponseLog, Long> {
}
