package htakahisa.domain.toru.repository;

import htakahisa.domain.toru.entity.CharacterStatusEntity;
import htakahisa.domain.toru.entity.CharactersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterStatusRepository extends JpaRepository<CharacterStatusEntity, Long> {
}
