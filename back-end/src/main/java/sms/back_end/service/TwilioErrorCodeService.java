package sms.back_end.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sms.back_end.entity.TwilioErrorCode;
import sms.back_end.repository.TwilioErrorCodeRepository;

@Service
public class TwilioErrorCodeService {

    private final TwilioErrorCodeRepository repository;

    public TwilioErrorCodeService(TwilioErrorCodeRepository repository) {
        this.repository = repository;
    }

    public TwilioErrorCode create(TwilioErrorCode errorCode) {
        return repository.save(errorCode);
    }

    public Optional<TwilioErrorCode> getById(Integer code) {
        return repository.findById(code);
    }

    public List<TwilioErrorCode> getAll() {
        return repository.findAll();
    }

    public TwilioErrorCode update(Integer code, TwilioErrorCode errorCode) {
        return repository.findById(code)
                .map(existing -> {
                    existing.setMessage(errorCode.getMessage());
                    existing.setMoreInfo(errorCode.getMoreInfo());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Error code not found: " + code));
    }

    public void delete(Integer code) {
        if (!repository.existsById(code)) {
            throw new RuntimeException("Error code not found: " + code);
        }
        repository.deleteById(code);
    }
}
