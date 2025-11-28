package sms.back_end.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import sms.back_end.entity.SmsMessage;

public interface SmsMessageRepository extends JpaRepository<SmsMessage, Long> {
    // CRUD standard fourni par JpaRepository
}
