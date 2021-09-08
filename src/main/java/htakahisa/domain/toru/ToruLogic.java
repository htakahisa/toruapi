package htakahisa.domain.toru;

import htakahisa.controller.dto.*;
import htakahisa.domain.toru.dto.BattleInfo;
import htakahisa.domain.toru.dto.BattleResult;
import htakahisa.domain.toru.entity.CharacterStatusEntity;
import htakahisa.domain.toru.entity.CharactersEntity;
import htakahisa.domain.toru.entity.RoomEntity;
import htakahisa.domain.toru.entity.WazaEntity;
import htakahisa.domain.toru.enums.BattleResultStatus;
import htakahisa.domain.toru.enums.ClientAction;
import htakahisa.domain.toru.enums.InAction;
import htakahisa.domain.toru.enums.SpecialAbility;
import htakahisa.domain.toru.repository.CharacterStatusRepository;
import htakahisa.domain.toru.repository.CharactersRepository;
import htakahisa.domain.toru.repository.RoomRepository;
import htakahisa.domain.toru.repository.WazaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
                room.setCharacterId1(req.getCharacterId1());
                room.setUser1Char1(req.getCharacterId1());
                room.setUser1Char2(req.getCharacterId2());
                room.setUser1Char3(req.getCharacterId3());
                room.setUserId2("UNDEFINED");
                return roomRepository.save(room);
            } else {

                RoomEntity room = undefineds.get(0);
                room.setUserId2(req.getUserId());
                room.setCharacterId2(req.getCharacterId1());
                room.setUser2Char1(req.getCharacterId1());
                room.setUser2Char2(req.getCharacterId2());
                room.setUser2Char3(req.getCharacterId3());
                return roomRepository.save(room);
            }
        } else {
            return rooms.get(0);
        }
    }

    public void setBattleResultStatus(String roomId, BattleResultStatus battleResultStatus) {
        battleResult.setStatus(roomId, battleResultStatus);
    }

    public void setUserId(String roomId, String userId) {
        battleResult.setUserId(roomId, userId);
    }

    public void setCharacterStatus(String roomId, String userId, Long characterId) {
        CharacterStatusEntity characterStatus =
                characterStatusRepository.findByRoomIdAndUserIdAndCharacterId(roomId, userId, characterId);

        if (characterStatus == null) {
            CharactersEntity character = characterRepository.findById(characterId).get();
            characterStatus = CharacterStatusEntity.of(roomId, userId, character);
            characterStatusRepository.save(characterStatus);
        }
    }

    public CharactersEntity getCharacter(GetCharacterReq req) {
        return characterRepository.findById(req.getCharacterId()).get();
    }

    public boolean setReadyBattle(BattleReq req) {
        // room 取得
        RoomEntity room = roomRepository.findById(req.getRoomId()).get();
        room.setWaza(req.getUserId(), req.getWaza());
        roomRepository.save(room);

        boolean commandReady = room.commandReady() && battleResult.getBattleResultStatus(req.getRoomId()) == BattleResultStatus.COMMAND_WAITING;
        return commandReady;
    }

    public BattleRes battle(BattleReq req) {

        RoomEntity room = roomRepository.findById(req.getRoomId()).get();

        CharactersEntity char1 = characterRepository.findById(room.getCharacterId1()).get();
        CharactersEntity char2 = characterRepository.findById(room.getCharacterId2()).get();

        WazaEntity waza1 = wazaRepository.findById(room.getWazaUser1()).get();
        WazaEntity waza2 = wazaRepository.findById(room.getWazaUser2()).get();

        CharacterStatusEntity characterStatus1 =
                characterStatusRepository.findByRoomIdAndUserIdAndCharacterId(
                        room.getRoomId(),
                        room.getUserId1(),
                        room.getCharacterId1());
        CharacterStatusEntity characterStatus2 = characterStatusRepository.findByRoomIdAndUserIdAndCharacterId(
                room.getRoomId(),
                room.getUserId2(),
                room.getCharacterId2());

        BattleInfo b1 = BattleInfo.of(char1, characterStatus1, characterStatus2, room, waza1, room.getUserId1());
        BattleInfo b2 = BattleInfo.of(char2, characterStatus2, characterStatus1, room, waza2, room.getUserId2());

        // 順番を決める
        List<BattleInfo> battleInfos = List.of(b1, b2).stream()
                .sorted((c1, c2) -> {
                    if (c1.getWaza().getPriority().compareTo(c2.getWaza().getPriority()) > 0) {
                        return 1;
                    } else if (c1.getWaza().getPriority().compareTo(c2.getWaza().getPriority()) < 0) {
                        return -1;
                    } else {
                        return c1.getCharacter().getSpeed().compareTo(c2.getCharacter().getSpeed());
                    }
                })
                .collect(Collectors.toList());

        // バトル
        BattleResultRes battleResultRes = new BattleResultRes();
        {

            for (BattleInfo battleInfo : battleInfos) {
                BattleResultRes.BattleResult result = new BattleResultRes.BattleResult();
                // 最初に場に出た時
                if (battleInfo.getMe().getTurnCount() == 0) {
                    if (InAction.IN_THE_BATTLE == battleInfo.getWaza().getInAction()) {

                    }

                    if (SpecialAbility.TORU == battleInfo.getCharacter().getSpecialAbility()) {
                        BattleResultRes.ResultAction resultAction = new BattleResultRes.ResultAction();
                        resultAction.setAction(ClientAction.EFFECT);
                        resultAction.setMessage1("とうやまは登場した瞬間に宇宙人からのスーパービームを受け、攻撃力がぐーんとあがった。");
                        battleInfo.getMe().setAttackRate(new BigDecimal("1.5"));
                        result.setInTheBattle(resultAction);
                    }
                }
                battleInfo.getMe().setTurnCount(battleInfo.getMe().getTurnCount() + 1);

                battleResultRes.getResults().add(result);
            }

        }

        {

            for (BattleInfo battleInfo : battleInfos) {
                BattleResultRes.BattleResult result = new BattleResultRes.BattleResult();
                //
                if (InAction.BEFORE_ATTACK == battleInfo.getWaza().getInAction()) {

                }

                if (InAction.IN_ATTACK == battleInfo.getWaza().getInAction()) {
                    if (ClientAction.ATTACK == battleInfo.getWaza().getClientAction()) {
                        BattleResultRes.ResultAction resultAction = new BattleResultRes.ResultAction();
                        resultAction.setMessage1(battleInfo.getMe().getName() + " の " + battleInfo.getWaza().getName() + "!");

                        if (battleInfo.isHit()) {
                            resultAction.setAction(ClientAction.ATTACK);

                            Long opHp = battleInfo.attack();
                            battleInfo.getOp().setHp(opHp);

                            CharacterStatusEntity copyMe = CharacterStatusEntity.of(battleInfo.getMe());
                            CharacterStatusEntity copyOp = CharacterStatusEntity.of(battleInfo.getOp());

                            resultAction.setCharacterStatus1(copyMe);
                            resultAction.setCharacterStatus2(copyOp);

                        } else {
                            // 当たらなかった
                            resultAction.setAction(ClientAction.NOT_HIT);
                            resultAction.setMessage2(battleInfo.getOp().getName() + " には当たらなかった。");
                        }

                        result.setInAttack(resultAction);
                        battleResultRes.getResults().add(result);

                    } else if (ClientAction.EFFECT == battleInfo.getWaza().getClientAction()) {

                    } else if (ClientAction.HEALING == battleInfo.getWaza().getClientAction()) {

                    }
                }

                if (InAction.AFTER_ATTACK == battleInfo.getWaza().getInAction()) {

                }
            }

        }

        {

            for (BattleInfo battleInfo : battleInfos) {
                BattleResultRes.BattleResult result = new BattleResultRes.BattleResult();
                if (InAction.END_THE_BATTLE == battleInfo.getWaza().getInAction()) {


                }

                if (SpecialAbility.TORU == battleInfo.getCharacter().getSpecialAbility() &&
                        battleInfo.getOp().getHp() <= 0) {

                    BattleResultRes.ResultAction action = new BattleResultRes.ResultAction();
                    action.setMessage1(battleInfo.getCharacter().getName() + " は敵を倒してテンションが上っている!");
                    battleInfo.getMe().setSpeedRate( battleInfo.getMe().getSpeedRate().add(new BigDecimal("0.2")));
                    action.setMessage2(battleInfo.getCharacter().getName() + " のすばやさが上がった!");

                    result.setEndTheBattle(action);
                }
                battleResultRes.getResults().add(result);
            }

        }


        roomRepository.save(room);
        battleResult.setStatus(room.getRoomId(), BattleResultStatus.FINISHED);
        battleResult.putBattleResult(room.getRoomId(), battleResultRes);


        BattleRes res = new BattleRes();
        res.setReady(true);
        return res;
    }

    public BattleResultRes getBattleResult(String roomId, String userId) {
        BattleResultRes res = battleResult.getBattleResult(roomId, userId);
        res.setBattleResultStatus(battleResult.getBattleResultStatus(roomId));
        if (res.getBattleResultStatus() == BattleResultStatus.COMMAND_WAITING) {
            RoomEntity room = roomRepository.findById(roomId).get();
            room.setWazaUser1(null);
            room.setWazaUser2(null);
            roomRepository.save(room);
        }
        return res;
    }
}
