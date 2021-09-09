package htakahisa.domain.toru.dto;

import htakahisa.domain.toru.entity.CharacterStatusEntity;
import htakahisa.domain.toru.entity.CharactersEntity;
import htakahisa.domain.toru.entity.RoomEntity;
import htakahisa.domain.toru.entity.WazaEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Random;

@Getter
@Setter
public class BattleInfo {

    private String userId;


    private RoomEntity room;

    private WazaEntity waza;

    private Long changeCharacterId;



    public static BattleInfo of(CharactersEntity character,
                                RoomEntity room,
                                WazaEntity waza,
                                String userId,
                                Long changeCharacterId) {
        BattleInfo b = new BattleInfo();
        b.setRoom(room);
        b.setWaza(waza);
        b.setUserId(userId);
        b.setChangeCharacterId(changeCharacterId);
        return b;
    }

}
