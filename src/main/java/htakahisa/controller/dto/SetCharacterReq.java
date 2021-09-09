package htakahisa.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetCharacterReq {

    private String roomId;

    private String userId;

    private Long characterId;
}
