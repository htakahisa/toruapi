package htakahisa.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBattleResultStatusReq {

    private String roomId;
    private String userId;
}
