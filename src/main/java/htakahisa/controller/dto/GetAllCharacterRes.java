package htakahisa.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GetAllCharacterRes {

    public List<GetCharacterRes> characterResList = new ArrayList<>();

    public void addCharacter(GetCharacterRes res) {
        characterResList.add(res);
    }
}
