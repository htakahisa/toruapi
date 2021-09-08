package htakahisa.domain.toru.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "character_status")
@Getter
@Setter
public class CharacterStatusEntity {

    @Id
    private Long id;

    private long turnCount;
}
