package htakahisa.domain.toru.dto;

import htakahisa.domain.toru.entity.CharacterStatusEntity;
import htakahisa.domain.toru.entity.CharactersEntity;
import htakahisa.domain.toru.entity.RoomEntity;
import htakahisa.domain.toru.entity.WazaEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BattleInfo {

    private CharactersEntity character;

    private CharacterStatusEntity characterStatus;

    private RoomEntity room;

    private WazaEntity waza;


    public static BattleInfo of(CharactersEntity character,
                                CharacterStatusEntity characterStatus,
                                RoomEntity room,
                                WazaEntity waza) {
        BattleInfo b = new BattleInfo();
        b.setCharacter(character);
        b.setCharacterStatus(characterStatus);
        b.setRoom(room);
        b.setWaza(waza);

        return b;
    }
}
