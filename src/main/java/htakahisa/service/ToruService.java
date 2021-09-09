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

        toruLogic.setBattleResultStatus(room.getRoomId(), BattleResultStatus.CHARACTER_SELECT);
        toruLogic.setUserId(room.getRoomId(), req.getUserId());
        toruLogic.setCharacterStatus(room.getRoomId(), req.getUserId(), req.getCharacterId1());
        toruLogic.setCharacterStatus(room.getRoomId(), req.getUserId(), req.getCharacterId2());
        toruLogic.setCharacterStatus(room.getRoomId(), req.getUserId(), req.getCharacterId3());

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
    public void setCharacter(SetCharacterReq req) {
        toruLogic.setCharacter(req);
    }


        @Transactional
    public GetBattleResultStatusRes getBattleResultStatus(GetBattleResultStatusReq req) {

        GetBattleResultStatusRes res = new GetBattleResultStatusRes();
        res.setBattleResultStatus(toruLogic.getBattleResultStatus(req.getRoomId()));
        return res;
    }

    @Transactional
    public BattleRes battle(BattleReq req) {

        toruLogic.setUserId(req.getRoomId(), req.getUserId());

        // roomId とユーザーのチェック
        if(!toruLogic.setReadyBattle(req)) {
            return BattleRes.of(toruLogic.getBattleResultStatus(req.getRoomId()));
        }

        toruLogic.setBattleResultStatus(req.getRoomId(), BattleResultStatus.BATTLE);
        // コマンド実行
        try {
            BattleRes res = toruLogic.battle(req);
            return res;
        } catch (Exception e) {
            toruLogic.setBattleResultStatus(req.getRoomId(), BattleResultStatus.COMMAND_WAITING);
            throw new RuntimeException(e);
        }
    }


    @Transactional
    public BattleResultRes getResult(BattleResultReq req) {


        BattleResultRes res = toruLogic.getBattleResult(req.getRoomId(), req.getUserId());

        return res;
    }

}
