package htakahisa.domain.toru.repository;

import htakahisa.domain.toru.entity.CharactersEntity;
import htakahisa.domain.toru.entity.WazaEntity;
import htakahisa.domain.toru.enums.Waza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WazaRepository extends JpaRepository<WazaEntity, Waza> {
}
