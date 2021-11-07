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

//        toruLogic.setBattleResultStatus(room.getRoomId(), BattleResultStatus.INIT_CHANGE);
//        toruLogic.setUserId(room.getRoomId(), req.getUserId());

        toruLogic.setCharacterStatus(room.getRoomId(), req.getUserId(), req.getCharacterId1());
        toruLogic.setCharacterStatus(room.getRoomId(), req.getUserId(), req.getCharacterId2());
        toruLogic.setCharacterStatus(room.getRoomId(), req.getUserId(), req.getCharacterId3());

        toruLogic.setBattleStatusForCreateRoom(BattleResultStatus.INIT_CHANGE, req, room);
        toruLogic.initBattleStatus(room.getRoomId());
        CreateRoomRes res = new CreateRoomRes();
        res.setRoomId(room.getRoomId());
        res.setReady(room.ready());
        return res;
    }

    @Transactional
    public GetCharacterRes getCharacter(GetCharacterReq req) {
        GetCharacterRes res = toruLogic.getCharacter(req);

        return res;
    }

    @Transactional
    public void setCharacter(SetCharacterReq req) {
        toruLogic.setCharacter(req);
    }


        @Transactional
    public GetBattleResultStatusRes getBattleResultStatus(GetBattleResultStatusReq req) {

        GetBattleResultStatusRes res = toruLogic.getBattleResultStatus(req.getRoomId(), req.getUserId());
        return res;
    }

    @Transactional
    public BattleRes battle(BattleReq req) {

        toruLogic.setUserId(req.getRoomId(), req.getUserId());

//        if (toruLogic.getBattleResultStatus(req.getRoomId()) == BattleResultStatus.BATTLE_FINISED) {
//            return BattleRes.of(BattleResultStatus.BATTLE_FINISED);
//        }

        // roomId とユーザーのチェック
//        if(!toruLogic.setReadyBattle(req)) {
//            return BattleRes.of(toruLogic.getBattleResultStatus(req.getRoomId()));
//        }

//        toruLogic.setBattleResultStatus(req.getRoomId(), BattleResultStatus.BATTLE);


        boolean isReady = toruLogic.updateBattleStatus(req);
        if (!isReady) {
            return null;
        }

        // コマンド実行
//        try {
            BattleRes res = toruLogic.battle(req);


        toruLogic.setBattleStatus(BattleResultStatus.GET_RESULT, req.getRoomId());

            return res;
//        } catch (Exception e) {
//            toruLogic.setBattleResultStatus(req.getRoomId(), BattleResultStatus.COMMAND_INPUT);
//            throw new RuntimeException(e);
//        }


    }


    @Transactional
    public BattleResultRes getResult(BattleResultReq req) {
//        if (toruLogic.getBattleResultStatus(req.getRoomId()) == BattleResultStatus.BATTLE_FINISED) {
//            BattleResultRes r  = new BattleResultRes();
//            r.setBattleResultStatus(BattleResultStatus.BATTLE_FINISED);
//            return r;
//        }

        BattleResultRes res = toruLogic.getBattleResult(req.getRoomId(), req.getUserId());

        return res;
    }


}
