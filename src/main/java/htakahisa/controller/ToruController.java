package htakahisa.controller;

import htakahisa.controller.dto.*;
import htakahisa.service.ToruService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ToruController {


    @Autowired
    private ToruService toruService;

    @PostMapping(value="/getInfo")
    public GetInfoRes getInfo(@RequestBody GetInfoReq req) {

        GetInfoRes res = new GetInfoRes();

        res.setHp("100");

        return res;
    }

    @PostMapping(value="/createRoom")
    public CreateRoomRes createRoom(@RequestBody CreateRoomReq req) {
        return toruService.createRoom(req);

    }

    @PostMapping(value="/getBattleResultStatus")
    public GetBattleResultStatusRes getBattleResultStatus(@RequestBody GetBattleResultStatusReq req) {
        return toruService.getBattleResultStatus(req);
    }

    @PostMapping(value="/getCharacter")
    public GetCharacterRes getCharacter(@RequestBody GetCharacterReq req) {
        return toruService.getCharacter(req);
    }


    @PostMapping(value="/setCharacter")
    @Deprecated
    public void setCharacter(@RequestBody SetCharacterReq req) {
//        toruService.setCharacter(req);
    }

    @PostMapping(value="/battle")
    public BattleRes battle(@RequestBody BattleReq req) {
        return toruService.battle(req);
    }

    @PostMapping(value="/getResult")
    public BattleResultRes getResult(@RequestBody BattleResultReq req) {
        return toruService.getResult(req);
    }

}

