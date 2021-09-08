package htakahisa.domain.toru.repository;

import htakahisa.domain.toru.entity.CharactersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharactersRepository extends JpaRepository<CharactersEntity, Long> {


}