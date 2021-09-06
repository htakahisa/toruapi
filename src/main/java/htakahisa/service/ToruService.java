package htakahisa.service;

import htakahisa.controller.dto.CreateRoomReq;
import htakahisa.controller.dto.CreateRoomRes;
import htakahisa.domain.toru.ToruLogic;
import htakahisa.domain.toru.entity.RoomEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ToruService {

    @Autowired
    private ToruLogic toruLogic;

    /**
     * 戦うための部屋を作ります
     */
    @Transactional
    public CreateRoomRes createRoom(CreateRoomReq req) {

        RoomEntity room = toruLogic.saveRoom(req);

        CreateRoomRes res = new CreateRoomRes();
        res.setRoomId(room.getRoomId());
        res.setReady(room.ready());
        return res;
    }
}
