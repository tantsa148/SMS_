package sms.back_end.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sms.back_end.entity.Possede;
import sms.back_end.entity.PossedeId;

@Repository
public interface PossedeRepository extends JpaRepository<Possede, PossedeId> {
    List<Possede> findByUserId(Long userId);
}
