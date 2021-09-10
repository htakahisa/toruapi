package htakahisa.domain.toru.repository;

import htakahisa.domain.toru.entity.CharacterStatusEntity;
import htakahisa.domain.toru.entity.CharactersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacterStatusRepository extends JpaRepository<CharacterStatusEntity, Long> {

    CharacterStatusEntity findByRoomIdAndUserIdAndCharacterId(String roomId, String userId, Long characterId);

    List<CharacterStatusEntity> findByRoomId(String roomId);
}
