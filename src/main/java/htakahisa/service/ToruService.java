package htakahisa.service;

import htakahisa.controller.dto.*;
import htakahisa.domain.toru.ToruLogic;
import htakahisa.domain.toru.entity.CharactersEntity;
import htakahisa.domain.toru.entity.RoomEntity;
import htakahisa.domain.toru.enums.BattleResultStatus;
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
        if (room.ready()) {
            toruLogic.setBattleResultStatus(room.getRoomId(), BattleResultStatus.BATTLE);

        } else {
            toruLogic.setBattleResultStatus(room.getRoomId(), BattleResultStatus.COMMAND_WAITING);
        }
        toruLogic.setUserId(room.getRoomId(), req.getUserId());

        CreateRoomRes res = new CreateRoomRes();
        res.setRoomId(room.getRoomId());
        res.setReady(room.ready());
        return res;
    }

    @Transactional
    public GetCharacterRes getCharacter(GetCharacterReq req) {
        CharactersEntity character = toruLogic.getCharacter(req);

        GetCharacterRes res = new GetCharacterRes();
        res.setCharacter(character);
        return res;
    }

    @Transactional
    public BattleRes battle(BattleReq req) {

        toruLogic.setUserId(req.getRoomId(), req.getUserId());

        // roomId とユーザーのチェック
        if(!toruLogic.setReadyBattle(req)) {
            return new BattleRes();
        }

        // コマンド実行
        BattleRes res = toruLogic.battle(req);


        return res;
    }


    @Transactional
    public BattleResultRes getResult(BattleResultReq req) {


        BattleResultRes res = toruLogic.getBattleResult(req.getRoomId(), req.getUserId());

        return res;
    }

}
