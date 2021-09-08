package htakahisa.domain.toru;

import htakahisa.controller.dto.*;
import htakahisa.domain.toru.dto.BattleInfo;
import htakahisa.domain.toru.dto.BattleResult;
import htakahisa.domain.toru.entity.*;
import htakahisa.domain.toru.enums.BattleResultStatus;
import htakahisa.domain.toru.enums.ClientAction;
import htakahisa.domain.toru.enums.SpecialAbility;
import htakahisa.domain.toru.enums.Waza;
import htakahisa.domain.toru.repository.CharacterStatusRepository;
import htakahisa.domain.toru.repository.CharactersRepository;
import htakahisa.domain.toru.repository.RoomRepository;
import htakahisa.domain.toru.repository.WazaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ToruLogic {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private CharactersRepository characterRepository;
    @Autowired
    private CharacterStatusRepository characterStatusRepository;
    @Autowired
    private WazaRepository wazaRepository;

    private BattleResult battleResult = new BattleResult();

    public RoomEntity saveRoom(CreateRoomReq req) {

        List<RoomEntity> rooms = roomRepository.findByUserId1(req.getUserId());

        if (CollectionUtils.isEmpty(rooms)) {

            // userId2 にあるなら戦闘中なのでその roomId を返す
            List<RoomEntity> user2Rooms = roomRepository.findByUserId2(req.getUserId());
            if (user2Rooms.size() > 0) {
                return user2Rooms.get(0);
            }

            List<RoomEntity> undefineds =
                    roomRepository.findByUserId2("UNDEFINED");
            if (undefineds.size() == 0) {
                RoomEntity room = new RoomEntity();
                room.setRoomId(UUID.randomUUID().toString());
                room.setUserId1(req.getUserId());
                room.setUserId2("UNDEFINED");
                return roomRepository.save(room);
            } else {

                RoomEntity room = undefineds.get(0);
                room.setUserId2(req.getUserId());
                return roomRepository.save(room);
            }
        } else {
            return rooms.get(0);
        }
    }

    public void setBattleResultStatus(String roomId, BattleResultStatus battleResultStatus) {
        battleResult.setStatus(roomId, battleResultStatus);
    }

    public CharactersEntity getCaracter(GetCharacterReq req) {
        return characterRepository.findById(req.getCharacterId()).get();
    }

    public boolean setReadyBattle(BattleReq req) {
        // room 取得
        RoomEntity room = roomRepository.findById(req.getRoomId()).get();
        room.setWaza(req.getUserId(), req.getWaza());
        roomRepository.save(room);

        boolean commandReady = room.commandReady();
        return commandReady;
    }

    public BattleResultRes getBattleResult(String roomId, String userId) {
        return battleResult.getBattleResult(roomId, userId);
    }

    public BattleRes battle(BattleReq req) {

        RoomEntity room = roomRepository.findById(req.getRoomId()).get();

        CharactersEntity char1 = characterRepository.findById(room.getCharacterId1()).get();
        CharactersEntity char2 = characterRepository.findById(room.getCharacterId2()).get();

        CharacterStatusEntity charStatus1 = characterStatusRepository.findById(room.getCharacterId1()).get();
        CharacterStatusEntity charStatus2 = characterStatusRepository.findById(room.getCharacterId2()).get();

        WazaEntity waza1 = wazaRepository.findById(room.getWazaUser1()).get();
        WazaEntity waza2 = wazaRepository.findById(room.getWazaUser2()).get();

        BattleInfo b1 = BattleInfo.of(char1, charStatus1, room, waza1);
        BattleInfo b2 = BattleInfo.of(char2, charStatus2, room, waza2);


        // 順番を決める
        List<BattleInfo> battleInfos = List.of(b1, b2);

        BattleRes res = new BattleRes();


        // バトル
        {
            BattleResultRes.BattleResult result = new BattleResultRes.BattleResult();
            for (BattleInfo battleInfo : battleInfos) {
                // 最初に場に出た時
                if (battleInfo.getCharacterStatus().getTurnCount() == 0) {
                    if (InAction.IN_THE_BATTLE == battleInfo.getWaza().getInAction()) {

                    }

                    if (SpecialAbility.TORU == battleInfo.getCharacter().getSpecialAbility()) {

                    }
                }
            }
        }

        {
            BattleResultRes.BattleResult result = new BattleResultRes.BattleResult();
            for (BattleInfo battleInfo : battleInfos) {
                //
                if (InAction.BEFORE_ATTACK == battleInfo.getWaza().getInAction()) {

                }

                if (InAction.IN_ATTACK == battleInfo.getWaza().getInAction()) {
                    if (Waza.PUNCH == battleInfo.getWaza().getWaza()) {
                        BattleResultRes.ResultAction resultAction = new BattleResultRes.ResultAction();
                        resultAction.setMessage("とるのぱんち");
                        resultAction.setAction(ClientAction.ATTACK);

                        result.setInAttack(resultAction);
                    }
                }

                if (InAction.AFTER_ATTACK == battleInfo.getWaza().getInAction()) {

                }
            }
        }


        for (BattleInfo battleInfo : battleInfos) {
            if (InAction.END_THE_BATTLE == battleInfo.getWaza().getInAction()) {

            }
        }

        // 最後に入力されていた技をクリアする
//        room.clearWaza();
        roomRepository.save(room);


        battleResult.setStatus(room.getRoomId(), BattleResultStatus.FINISHED);

        return res;
    }
}
