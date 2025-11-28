package sms.back_end.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sms.back_end.entity.Possede;
import sms.back_end.entity.PossedeId;
import sms.back_end.repository.PossedeRepository;

@Service
public class PossedeService {

    @Autowired
    private PossedeRepository repository;

    // Récupérer tous les numéros d'un utilisateur
    public List<Possede> getByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    // Ajouter un numéro à l'utilisateur (id récupéré depuis JWT)
    public Possede addNumeroToUser(Long userId, Long numeroId) {
        Possede possede = new Possede();
        possede.setUserId(userId);
        possede.setNumeroId(numeroId);
        possede.setDateCreation(LocalDateTime.now());
        return repository.save(possede);
    }

    // Supprimer un numéro pour l'utilisateur
    public boolean removeNumero(Long userId, Long numeroId) {
        if(repository.existsById(new PossedeId(userId, numeroId))) {
            repository.deleteById(new PossedeId(userId, numeroId));
            return true;
        }
        return false;
    }
}
