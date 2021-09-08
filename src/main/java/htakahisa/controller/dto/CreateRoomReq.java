package htakahisa.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoomReq {

    private String userId;

    private Long characterId1;
    private Long characterId2;
    private Long characterId3;
}
