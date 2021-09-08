package htakahisa.controller.dto;

import htakahisa.domain.toru.entity.CharactersEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCharacterRes {

    private CharactersEntity character;
}
