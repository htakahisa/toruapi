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
}
