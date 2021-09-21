package htakahisa.controller.dto;

import htakahisa.domain.toru.enums.Waza;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BattleReq {

    private String roomId;

    private String userId;

    private Waza waza;

    private Long changeCharacterId;

    public Long getChangeCharacterId() {
        if (changeCharacterId == null || changeCharacterId == 0) {
            return null;
        }
        return changeCharacterId;
    }
}
