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

    private String userId;

    private CharactersEntity character;

    private CharacterStatusEntity me;
    private CharacterStatusEntity op;

    private RoomEntity room;

    private WazaEntity waza;

    public Long attack() {
        Long hp = getOp().getHp() - (waza.getPower() + me.getAttack());
        if (hp < 0) {
            hp = 0L;
        }
        return hp;
    }


    public static BattleInfo of(CharactersEntity character,
                                CharacterStatusEntity me,
                                CharacterStatusEntity op,
                                RoomEntity room,
                                WazaEntity waza,
                                String userId) {
        BattleInfo b = new BattleInfo();
        b.setCharacter(character);
        b.setMe(me);
        b.setOp(op);
        b.setRoom(room);
        b.setWaza(waza);
        b.setUserId(userId);
        return b;
    }
}
