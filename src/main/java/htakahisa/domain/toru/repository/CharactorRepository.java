package htakahisa.domain.toru.repository;

import htakahisa.domain.toru.entity.CharactorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharactorRepository extends JpaRepository<CharactorEntity, Long> {


}