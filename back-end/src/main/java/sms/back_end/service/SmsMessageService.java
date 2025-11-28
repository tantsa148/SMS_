package sms.back_end.service;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sms.back_end.entity.SmsMessage;
import sms.back_end.repository.SmsMessageRepository;

@Service
public class SmsMessageService {

    private final SmsMessageRepository repository;

    public SmsMessageService(SmsMessageRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public SmsMessage createMessage(SmsMessage message) {
        return repository.save(message);
    }

    // READ ALL
    public List<SmsMessage> getAllMessages() {
        return repository.findAll();
    }

    // READ BY ID
    public Optional<SmsMessage> getMessageById(Long id) {
        return repository.findById(id);
    }

    // UPDATE
    public SmsMessage updateMessage(Long id, SmsMessage updatedMessage) {
        return repository.findById(id).map(message -> {
            message.setTexte(updatedMessage.getTexte());
            return repository.save(message);
        }).orElseThrow(() -> new RuntimeException("Message non trouvé"));
    }

    // DELETE
    public void deleteMessage(Long id) {
        repository.deleteById(id);
    }
}
