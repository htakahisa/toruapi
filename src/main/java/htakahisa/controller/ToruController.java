package htakahisa.controller;

import htakahisa.controller.dto.CreateRoomReq;
import htakahisa.controller.dto.CreateRoomRes;
import htakahisa.controller.dto.GetInfoReq;
import htakahisa.controller.dto.GetInfoRes;
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
}

