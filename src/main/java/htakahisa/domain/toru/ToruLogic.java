package htakahisa.domain.toru;

import htakahisa.controller.dto.*;
import htakahisa.domain.toru.dto.BattleDto;
import htakahisa.domain.toru.dto.BattleInfo;
import htakahisa.domain.toru.dto.BattleResult;
import htakahisa.domain.toru.dto.MeAndOp;
import htakahisa.domain.toru.entity.CharacterStatusEntity;
import htakahisa.domain.toru.entity.CharactersEntity;
import htakahisa.domain.toru.entity.RoomEntity;
import htakahisa.domain.toru.entity.WazaEntity;
import htakahisa.domain.toru.enums.*;
import htakahisa.domain.toru.repository.CharacterStatusRepository;
import htakahisa.domain.toru.repository.CharactersRepository;
import htakahisa.domain.toru.repository.RoomRepository;
import htakahisa.domain.toru.repository.WazaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    /**
     * status map
     */
    private Map<String, GetBattleResultStatusRes> battleStatusMap = new HashMap<>();

    public RoomEntity saveRoom(CreateRoomReq req) {

        List<RoomEntity> rooms = roomRepository.findByUserId1(req.getUserId());

        if (CollectionUtils.isEmpty(rooms) ||
                !rooms.stream().anyMatch(r -> r.getWinner() == 0)) {

            // userId2 にあるなら戦闘中なのでその roomId を返す
            List<RoomEntity> user2Rooms = roomRepository.findByUserId2(req.getUserId());
            if (user2Rooms.stream().filter(r -> r.getWinner() == 0)
                    .collect(Collectors.toList()).size() > 0) {
                return user2Rooms.stream().filter(r -> r.getWinner() == 0)
                        .collect(Collectors.toList()).get(0);
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
                room.setWinner(0L);
                return roomRepository.save(room);
            } else {
                RoomEntity room = undefineds.get(0);
                room.setUserId2(req.getUserId());
                room.setCharacterId2(req.getCharacterId1());
                room.setUser2Char1(req.getCharacterId1());
                room.setUser2Char2(req.getCharacterId2());
                room.setUser2Char3(req.getCharacterId3());
                room.setWinner(0L);
                return roomRepository.save(room);
            }
        } else {
            return rooms.stream().filter(r -> r.getWinner() == 0).collect(Collectors.toList()).get(0);
        }
    }


    public void setBattleStatusForCreateRoom(BattleResultStatus battleResultStatus, CreateRoomReq req, RoomEntity room) {
        GetBattleResultStatusRes res = battleStatusMap.get(room.getRoomId());

        if (res == null) {
            res = new GetBattleResultStatusRes();
        }

        res.update(battleResultStatus, req, room);

        battleStatusMap.put(room.getRoomId(), res);
    }

    public void setCharacter(SetCharacterReq req) {
        RoomEntity room = roomRepository.findById(req.getRoomId()).get();
        if (room.getUserId1().equals(req.getUserId())) {
            room.setCharacterId1(req.getCharacterId());
        } else if (room.getUserId2().equals(req.getUserId())) {
            room.setCharacterId2(req.getCharacterId());
        }

        roomRepository.save(room);
    }

    public void initBattleStatus(String roomId) {
        battleResult.initBattleStatus(roomId);
    }

    public void setUserId(String roomId, String userId) {
        battleResult.setUserId(roomId, userId);
    }

    public boolean updateBattleStatus(BattleReq req) {
        GetBattleResultStatusRes battleStatus = battleStatusMap.get(req.getRoomId());
        GetBattleResultStatusRes.UserStatus userStatus = battleStatus.getUserStatus(req.getUserId());

        if (userStatus.getBattleResultStatus() == BattleResultStatus.WAIT ||
                userStatus.getBattleResultStatus() == BattleResultStatus.WIN ||
                userStatus.getBattleResultStatus() == BattleResultStatus.LOSE ||
                userStatus.getBattleResultStatus() == BattleResultStatus.GET_RESULT ) {
            return battleStatus.isAllUserWait();
        }

        if (req.getWaza() == Waza.INIT_CHANGE) {
            if (userStatus.getBattleResultStatus() == BattleResultStatus.INIT_CHANGE) {
                userStatus.setBattleResultStatus(BattleResultStatus.WAIT);
                userStatus.setBattleResultStatusOld(BattleResultStatus.INIT_CHANGE);
            }
        } else if (req.getWaza() == Waza.CHANGE) {
            if (userStatus.getBattleResultStatus() == BattleResultStatus.COMMAND_INPUT) {
                userStatus.setBattleResultStatus(BattleResultStatus.WAIT);
                userStatus.setBattleResultStatusOld(BattleResultStatus.COMMAND_INPUT);
            }
        } else if (req.getWaza() == Waza.GIVE_UP) {
            if (userStatus.getBattleResultStatus() == BattleResultStatus.COMMAND_INPUT) {
                userStatus.setBattleResultStatus(BattleResultStatus.WAIT);
                userStatus.setBattleResultStatusOld(BattleResultStatus.COMMAND_INPUT);
            }
        } else {
            // その他の通常攻撃など
            if (userStatus.getBattleResultStatus() == BattleResultStatus.COMMAND_INPUT) {
                userStatus.setBattleResultStatus(BattleResultStatus.WAIT);
                userStatus.setBattleResultStatusOld(BattleResultStatus.COMMAND_INPUT);
            }
        }

        RoomEntity room = roomRepository.findById(req.getRoomId()).get();
        room.setWaza(req.getUserId(), req.getWaza());
        room.setChangeCharacterId(req.getUserId(), req.getChangeCharacterId());
        roomRepository.save(room);
        return battleStatus.isAllUserWait();
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

    public GetCharacterRes getCharacter(GetCharacterReq req) {
        CharactersEntity c = characterRepository.findById(req.getCharacterId()).get();
        WazaEntity w1 = wazaRepository.findById(c.getWaza1()).get();
        WazaEntity w2 = wazaRepository.findById(c.getWaza2()).get();
        WazaEntity w3 = wazaRepository.findById(c.getWaza3()).get();
        WazaEntity w4 = wazaRepository.findById(c.getWaza4()).get();
        return GetCharacterRes.of(c, w1, w2, w3, w4);
    }

//    public boolean setReadyBattle(BattleReq req) {
//        // room 取得
//        RoomEntity room = roomRepository.findById(req.getRoomId()).get();
//        room.setWaza(req.getUserId(), req.getWaza());
//        if (room.getUserId1().equals(req.getUserId())) {
//            room.setChangeCharacterId1(req.getChangeCharacterId());
//        } else if (room.getUserId2().equals(req.getUserId())) {
//            room.setChangeCharacterId2(req.getChangeCharacterId());
//        }
//        roomRepository.save(room);
//
//        if (battleResult.getBattleResultStatus(req.getRoomId()) == BattleResultStatus.CHARACTER_SELECT) {
//            if (room.getChangeCharacterId1() != null && room.getChangeCharacterId2() != null){
//                battleResult.setStatus(req.getRoomId(), BattleResultStatus.COMMAND_WAITING);
//            }
//        }
//
//        boolean commandReady = room.commandReady() && battleResult.getBattleResultStatus(req.getRoomId()) == BattleResultStatus.COMMAND_WAITING;
//        return commandReady;
//    }

    public GetBattleResultStatusRes getBattleResultStatus(String roomId) {
        GetBattleResultStatusRes battleStatus = battleStatusMap.get(roomId);

        if (battleStatus.isAllUserWait()) {
            RoomEntity room = roomRepository.findById(roomId).get();
            battleStatus.update(BattleResultStatus.COMMAND_INPUT, room.getUserId1());
            battleStatus.update(BattleResultStatus.COMMAND_INPUT, room.getUserId2());
        }

        return battleStatus;
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

        BattleInfo b1 = BattleInfo.of(char1, room, waza1, room.getUserId1(),
                req.getChangeCharacterId());
        BattleInfo b2 = BattleInfo.of(char2, room, waza2, room.getUserId2(),
                req.getChangeCharacterId());

        MeAndOp meAndOp = MeAndOp.of(characterStatus1, characterStatus2);

        // 順番を決める
        List<BattleInfo> battleInfos = List.of(b1, b2).stream()

                .filter(b -> b.getWaza().getWaza() != Waza.NONE)
                .sorted((c1, c2) -> {
                    if (c1.getWaza().getPriority().compareTo(c2.getWaza().getPriority()) > 0) {
                        return -1;
                    } else if (c1.getWaza().getPriority().compareTo(c2.getWaza().getPriority()) < 0) {
                        return 1;
                    } else {
                        return -meAndOp.getMe(c1.getUserId()).getSpeed()
                                .compareTo(meAndOp.getMe(c2.getUserId()).getSpeed());
                    }
                })
                .collect(Collectors.toList());

        return this.getBattleRes(room, battleInfos, meAndOp);
    }

    /**
     * バトルを実施します
     * @param room
     * @param battleInfos
     * @return
     */
    private BattleRes getBattleRes(RoomEntity room, List<BattleInfo> battleInfos, MeAndOp meAndOp) {
        // バトル
        BattleResultRes battleResultRes = new BattleResultRes();

        BattleDto dto = new BattleDto();

        if (!this.isGiveUp(room, battleInfos, meAndOp, battleResultRes)) {
            this.change(room, battleInfos, meAndOp, battleResultRes);
            if (!battleInfos.stream().anyMatch(b -> b.getWaza().getWaza() == Waza.INIT_CHANGE)) {
                this.inTheBattle(battleInfos, meAndOp, battleResultRes);
                dto = this.attack(battleInfos, meAndOp, battleResultRes);
                if (!dto.isEndBattle()) {
                    this.endTheBattle(battleInfos, meAndOp, battleResultRes);
                }
            }
//            battleResult.setStatus(room.getRoomId(), BattleResultStatus.COMMAND_INPUT);

        }

        battleResult.putBattleResult(room.getRoomId(), battleResultRes, meAndOp.isSomeoneDead());
//        BattleRes res = BattleRes.of(battleResult.getBattleResultStatus(room.getRoomId()));

        return new BattleRes();
    }


    private boolean isGiveUp(RoomEntity room, List<BattleInfo> battleInfos, MeAndOp meAndOp, BattleResultRes battleResultRes) {

        List<BattleInfo> giveUps = battleInfos.stream()
                .filter(b -> b.getWaza().getWaza() == Waza.GIVE_UP)
                .collect(Collectors.toList());

        if (giveUps.size() > 0) {

            BattleResultRes.BattleResult result = new BattleResultRes.BattleResult();
            BattleResultRes.ResultAction resultAction = new BattleResultRes.ResultAction();

            resultAction.setMessage1("降参が選ばれました。");
            resultAction.setAction(ClientAction.GIVE_UP);

//            if (giveUps.get(0).getUserId().equals(room.getUserId1())) {
//                resultAction.setCharacterStatus1(meAndOp.getMe(room.getUserId1()));
//                battleResult.setStatus(room.getRoomId(), BattleResultStatus.GIVE_UP1);
//            } else if (giveUps.get(0).getUserId().equals(room.getUserId2())) {
//                resultAction.setCharacterStatus1(meAndOp.getMe(room.getUserId2()));
//                battleResult.setStatus(room.getRoomId(), BattleResultStatus.GIVE_UP2);
//            }

            result.setInTheBattle(resultAction);
            battleResultRes.getResults().add(result);

            return true;
        } else {
            return false;
        }
    }

    private void change(RoomEntity room, List<BattleInfo> battleInfos, MeAndOp meAndOp, BattleResultRes battleResultRes) {
        // 交代
        for (BattleInfo battleInfo : battleInfos) {
            BattleResultRes.BattleResult result = new BattleResultRes.BattleResult();

            if (battleInfo.getWaza().getWaza() == Waza.CHANGE ||
                    battleInfo.getWaza().getWaza() == Waza.INIT_CHANGE) {

                Long changeCharacterId = null;
                if (battleInfo.getUserId().equals(room.getUserId1())) {
                    changeCharacterId = room.getChangeCharacterId1();
//                    room.setChangeCharacterId1(null);
                    room.setCharacterId1(changeCharacterId);
                    roomRepository.save(room);
                    GetBattleResultStatusRes battleStatus = battleStatusMap.get(room.getRoomId());
                    battleStatus.setSelectedCharacterId(battleInfo.getUserId(), room.getChangeCharacterId1());
                } else if (battleInfo.getUserId().equals(room.getUserId2())) {
                    changeCharacterId = room.getChangeCharacterId2();
//                    room.setChangeCharacterId1(null);
                    room.setCharacterId2(changeCharacterId);
                    roomRepository.save(room);
                    GetBattleResultStatusRes battleStatus = battleStatusMap.get(room.getRoomId());
                    battleStatus.setSelectedCharacterId(battleInfo.getUserId(), room.getChangeCharacterId2());
                }

                CharacterStatusEntity change = characterStatusRepository.findByRoomIdAndUserIdAndCharacterId(
                        battleInfo.getRoom().getRoomId(),
                        battleInfo.getUserId(),
                        changeCharacterId);
                CharacterStatusEntity old = meAndOp.getMe(battleInfo.getUserId());
                old.initData();
                characterStatusRepository.save(old);

                meAndOp.setMe(battleInfo.getUserId(), change);


                BattleResultRes.ResultAction resultAction = new BattleResultRes.ResultAction();
                if (battleInfo.getWaza().getWaza() == Waza.CHANGE) {
                    resultAction.setMessage1("よくやったぞ " + old.getName() + "! 戻っておいで!");
                    resultAction.setAction(ClientAction.CHANGE);
                    resultAction.setMessage2("いけ! " + meAndOp.getMe(battleInfo.getUserId()).getName() + "!");
                } else if (battleInfo.getWaza().getWaza() == Waza.INIT_CHANGE) {
                    resultAction.setMessage1("いけ! " + meAndOp.getMe(battleInfo.getUserId()).getName() + "!");
                    resultAction.setAction(ClientAction.INIT_CHANGE);
                }
                //status change
                GetBattleResultStatusRes.UserStatus userStatus = battleStatusMap.get(room.getRoomId()).getUserStatus(battleInfo.getUserId());
                userStatus.setBattleResultStatus(BattleResultStatus.WAIT);

                CharacterStatusEntity copyMe = CharacterStatusEntity.of(meAndOp.getMe(battleInfo.getUserId()));
                resultAction.setCharacterStatus1(copyMe);

                result.setChange(resultAction);
                battleResultRes.getResults().add(result);
            }
        }
    }

    private void inTheBattle(List<BattleInfo> battleInfos, MeAndOp meAndOp, BattleResultRes battleResultRes) {
        for (BattleInfo battleInfo : battleInfos) {


            // 最初に場に出た時
            if (meAndOp.getMe(battleInfo.getUserId()).getTurnCount() == 0) {
                if (InAction.IN_THE_BATTLE == battleInfo.getWaza().getInAction()) {

                }

                if (SpecialAbility.TORU == meAndOp.getMe(battleInfo.getUserId()).getSpecialAbility()) {
                    BattleResultRes.BattleResult result = new BattleResultRes.BattleResult();
                    BattleResultRes.ResultAction resultAction = new BattleResultRes.ResultAction();
                    resultAction.setAction(ClientAction.EFFECT);
                    resultAction.setMessage1("とうやまは登場した瞬間に宇宙人からのスーパービームを受け、攻撃力がぐーんとあがった。");
                    meAndOp.getMe(battleInfo.getUserId()).addAttackRate(new BigDecimal("0.5"));
                    result.setInTheBattle(resultAction);
                    battleResultRes.getResults().add(result);
                }

                }if (SpecialAbility.OMOKO == meAndOp.getMe(battleInfo.getUserId()).getSpecialAbility()) {
                    BattleResultRes.BattleResult result = new BattleResultRes.BattleResult();
                    BattleResultRes.ResultAction resultAction = new BattleResultRes.ResultAction();
                    resultAction.setAction(ClientAction.EFFECT);
                    resultAction.setMessage1("おもこはちょうど通りかかった飛行機につかまって、ぐぐーんと上がった！");
                    meAndOp.getMe(battleInfo.getUserId()).addSpeedRate(new BigDecimal("2"));
                    result.setInTheBattle(resultAction);
                    battleResultRes.getResults().add(result);
            }
                 if (SpecialAbility.VALIOM == meAndOp.getMe(battleInfo.getUserId()).getSpecialAbility()) {
                    BattleResultRes.BattleResult result = new BattleResultRes.BattleResult();
                    BattleResultRes.ResultAction resultAction = new BattleResultRes.ResultAction();
                    resultAction.setAction(ClientAction.EFFECT);
                     resultAction.setMessage1("ヴァリオン");
                      meAndOp.getMe(battleInfo.getUserId()).addSpeedRate(new BigDecimal("2"));
                     result.setInTheBattle(resultAction);
                     battleResultRes.getResults().add(result);
            }
            meAndOp.getMe(battleInfo.getUserId())
                    .setTurnCount(meAndOp.getMe(battleInfo.getUserId()).getTurnCount() + 1);


        }
    }

    private BattleDto attack(List<BattleInfo> battleInfos, MeAndOp meAndOp, BattleResultRes battleResultRes) {



        for (BattleInfo battleInfo : battleInfos) {
            if (meAndOp.getMe(battleInfo.getUserId()).isDead()) {
                continue;
            }

            boolean isEffectHit = false;

            //
            if (InAction.BEFORE_ATTACK == battleInfo.getWaza().getInAction()) {

            }

            if (InAction.IN_ATTACK == battleInfo.getWaza().getInAction()) {
                BattleResultRes.BattleResult result = new BattleResultRes.BattleResult();

                if (ClientAction.ATTACK == battleInfo.getWaza().getClientAction() ||
                        ClientAction.EFFECT == battleInfo.getWaza().getClientAction()) {
                    BattleResultRes.ResultAction resultAction = new BattleResultRes.ResultAction();
                    resultAction.setMessage1(meAndOp.getMe(battleInfo.getUserId()).getName() + " の "
                            + battleInfo.getWaza().getName() + "!");

                    if (meAndOp.isHit(battleInfo.getWaza(), battleInfo.getUserId())) {
                        resultAction.setAction(battleInfo.getWaza().getClientAction());

                        Long opHp = meAndOp.attack(battleInfo.getWaza(), battleInfo.getUserId());
                        meAndOp.getOp(battleInfo.getUserId()).setHp(opHp);

                        CharacterStatusEntity copyMe = CharacterStatusEntity.of(meAndOp.getMe(battleInfo.getUserId()));
                        CharacterStatusEntity copyOp = CharacterStatusEntity.of(meAndOp.getOp(battleInfo.getUserId()));

                        resultAction.setCharacterStatus1(copyMe);
                        resultAction.setCharacterStatus2(copyOp);

                        // EFFECT があたったら true (ここはあたった場合なので EFFECT なら true にする)
                        isEffectHit = ClientAction.EFFECT == battleInfo.getWaza().getClientAction();

                    } else {
                        // 当たらなかった
                        resultAction.setAction(ClientAction.NOT_HIT);
                        resultAction.setMessage2(meAndOp.getOp(battleInfo.getUserId()).getName() + " には当たらなかった。");

                        CharacterStatusEntity copyMe = CharacterStatusEntity.of(meAndOp.getMe(battleInfo.getUserId()));
                        resultAction.setCharacterStatus1(copyMe);
                    }

                    result.setInAttack(resultAction);
                    battleResultRes.getResults().add(result);

                } else if (ClientAction.HEALING == battleInfo.getWaza().getClientAction()) {
                    BattleResultRes.ResultAction resultAction = new BattleResultRes.ResultAction();
                    meAndOp.getMe(battleInfo.getUserId())
                            .healing(battleInfo.getWaza().getPower(),
                                    meAndOp.getMe(battleInfo.getUserId()).getOrgHp());

                    CharacterStatusEntity copyMe = CharacterStatusEntity.of(meAndOp.getMe(battleInfo.getUserId()));
                    CharacterStatusEntity copyOp = CharacterStatusEntity.of(meAndOp.getOp(battleInfo.getUserId()));

                    resultAction.setCharacterStatus1(copyMe);
                    resultAction.setCharacterStatus2(copyOp);

                    resultAction.setMessage1(meAndOp.getMe(battleInfo.getUserId()).getName() + " の " +
                            battleInfo.getWaza().getName() +"!");
                    resultAction.setMessage2("傷が回復した!");
                    resultAction.setAction(ClientAction.HEALING);

                    result.setInAttack(resultAction);
                    battleResultRes.getResults().add(result);
                }


            }

            if (isEffectHit && battleInfo.getWaza().getAppendEffect() == AppendEffect.YAKEDO) {
                if (meAndOp.isAppendEffect(battleInfo.getWaza())) {
                    meAndOp.getOp(battleInfo.getUserId()).setAppendEffect(AppendEffect.YAKEDO);

                    BattleResultRes.BattleResult result = new BattleResultRes.BattleResult();
                    BattleResultRes.ResultAction resultAction = new BattleResultRes.ResultAction();
                    resultAction.setMessage1(meAndOp.getOp(battleInfo.getUserId()).getName() + " はやけどを負った!");
                    resultAction.setCharacterStatus1(meAndOp.getOp(battleInfo.getUserId()));
                    resultAction.setAction(ClientAction.EFFECT);
                    result.setInAttack(resultAction);
                    battleResultRes.getResults().add(result);
                }
            }

            if (InAction.AFTER_ATTACK == battleInfo.getWaza().getInAction()) {

            }

            BattleDto dto = this.isDead(meAndOp, battleResultRes, battleInfo);
            if (dto != null) {
                return dto;
            }
        }
        return new BattleDto();
    }


    private void endTheBattle(List<BattleInfo> battleInfos, MeAndOp meAndOp, BattleResultRes battleResultRes) {
        for (BattleInfo battleInfo : battleInfos) {

            if (InAction.END_THE_BATTLE == battleInfo.getWaza().getInAction()) {

            }

            if (meAndOp.getMe(battleInfo.getUserId()).getAppendEffect() == AppendEffect.YAKEDO) {
                BattleResultRes.BattleResult result = new BattleResultRes.BattleResult();
                BattleResultRes.ResultAction resultAction = new BattleResultRes.ResultAction();

                meAndOp.getMe(battleInfo.getUserId()).appendEffect();

                CharacterStatusEntity copyMe = CharacterStatusEntity.of(meAndOp.getMe(battleInfo.getUserId()));
                resultAction.setCharacterStatus1(copyMe);
                resultAction.setMessage1(meAndOp.getMe(battleInfo.getUserId()).getName() + " はやけどのダメージを受けた!");
                resultAction.setAction(ClientAction.ATTACK);
                result.setAfterAttack(resultAction);
                battleResultRes.getResults().add(result);

                this.isDead(meAndOp, battleResultRes, battleInfo);
                BattleDto dto = this.isDead(meAndOp, battleResultRes, battleInfo);
                if (dto != null) {
                    return;
                }
            }

            if (SpecialAbility.TORU == meAndOp.getMe(battleInfo.getUserId()).getSpecialAbility() &&
                    meAndOp.getOp(battleInfo.getUserId()).isDead() &&
                    !meAndOp.getMe(battleInfo.getUserId()).isDead()
            ) {
                BattleResultRes.BattleResult result = new BattleResultRes.BattleResult();
                BattleResultRes.ResultAction resultAction = new BattleResultRes.ResultAction();
                resultAction.setMessage1(meAndOp.getMe(battleInfo.getUserId()).getName() + " のいななき!\n敵を倒してテンション爆上がりだ!");
                meAndOp.getMe(battleInfo.getUserId())
                        .addSpeedRate(new BigDecimal("0.25"));
                resultAction.setMessage2(meAndOp.getMe(battleInfo.getUserId()).getName() + " のすばやさがぐんと上がった!");
                resultAction.setAction(ClientAction.EFFECT);
                CharacterStatusEntity copyMe = CharacterStatusEntity.of(meAndOp.getMe(battleInfo.getUserId()));
                resultAction.setCharacterStatus1(copyMe);

                result.setEndTheBattle(resultAction);
                battleResultRes.getResults().add(result);
            }

        }
    }


    private BattleDto isDead(MeAndOp meAndOp, BattleResultRes battleResultRes, BattleInfo battleInfo) {
        // 倒れたら終了
        if (meAndOp.getMe(battleInfo.getUserId()).isDead()) {
            BattleResultRes.BattleResult result = new BattleResultRes.BattleResult();
            BattleResultRes.ResultAction resultAction = new BattleResultRes.ResultAction();
            resultAction.setMessage1(meAndOp.getMe(battleInfo.getUserId()).getName() + " は倒された!");
            resultAction.setAction(ClientAction.DEAD);
            CharacterStatusEntity copyMe = CharacterStatusEntity.of(meAndOp.getMe(battleInfo.getUserId()));

            resultAction.setCharacterStatus1(copyMe);

            result.setAfterAttack(resultAction);
            battleResultRes.getResults().add(result);

            GetBattleResultStatusRes battleStatus = battleStatusMap.get(battleInfo.getRoom().getRoomId());
            battleStatus.setDead(meAndOp.getOp(battleInfo.getUserId()).getUserId());

            BattleDto dto = new BattleDto();
            dto.setSomeoneDead(true);
            dto.setEndBattle(this.finishedBattle(battleInfo.getRoom().getRoomId()));
            return dto;
        }

        if (meAndOp.getOp(battleInfo.getUserId()).isDead()) {
            BattleResultRes.BattleResult result = new BattleResultRes.BattleResult();
            BattleResultRes.ResultAction resultAction = new BattleResultRes.ResultAction();
            resultAction.setMessage1(meAndOp.getOp(battleInfo.getUserId()).getName() + " は倒された!");
            resultAction.setAction(ClientAction.DEAD);

            CharacterStatusEntity copyOp = CharacterStatusEntity.of(meAndOp.getOp(battleInfo.getUserId()));
            resultAction.setCharacterStatus2(copyOp);

            result.setAfterAttack(resultAction);
            battleResultRes.getResults().add(result);

            GetBattleResultStatusRes battleStatus = battleStatusMap.get(battleInfo.getRoom().getRoomId());
            battleStatus.setDead(meAndOp.getOp(battleInfo.getUserId()).getUserId());

            BattleDto dto = new BattleDto();
            dto.setSomeoneDead(true);
            dto.setEndBattle(this.finishedBattle(battleInfo.getRoom().getRoomId()));
            return dto;
        }
        return null;
    }




    public BattleResultRes getBattleResult(String roomId, String userId) {


        BattleResultRes res = battleResult.getBattleResult(roomId, userId);
        if (battleStatusMap.get(roomId).getUserStatus(userId).getBattleResultStatus() != BattleResultStatus.GET_RESULT) {
            return res;
        }

//        // 全員倒された場合は終了
//        List<CharacterStatusEntity> cs = characterStatusRepository.findByRoomId(roomId);
//        RoomEntity room = roomRepository.findById(roomId).get();
//        if (cs.stream().filter(c -> c.getUserId().equals(room.getUserId1()))
//                        .allMatch(c -> c.getHp() <= 0) ) {
//            res.setBattleResultStatus(BattleResultStatus.BATTLE_FINISED);
//            room.setWinner(1L);
//        } else if (cs.stream().filter(c -> c.getUserId().equals(room.getUserId2()))
//                .allMatch(c -> c.getHp() <= 0) ) {
//            res.setBattleResultStatus(BattleResultStatus.BATTLE_FINISED);
//            room.setWinner(2L);
//        } else if (battleResult.getBattleResultStatus(roomId) == BattleResultStatus.GIVE_UP1) {
//            res.setBattleResultStatus(BattleResultStatus.BATTLE_FINISED);
//            room.setWinner(2L);
//        } else if (battleResult.getBattleResultStatus(roomId) == BattleResultStatus.GIVE_UP2) {
//            res.setBattleResultStatus(BattleResultStatus.BATTLE_FINISED);
//            room.setWinner(1L);
//        } else {
//            res.setBattleResultStatus(battleResult.getBattleResultStatus(roomId));
//        }
//
//        if (res.getBattleResultStatus() == BattleResultStatus.COMMAND_WAITING) {
//            room.setWazaUser1(null);
//            room.setWazaUser2(null);
//            roomRepository.save(room);
//        }




        GetBattleResultStatusRes battleStatus = battleStatusMap.get(roomId);





        if (battleStatus.isAllUserWait()) {

            RoomEntity room = roomRepository.findById(roomId).get();

            // 降参が選ばれた
            if (room.getWazaUser1() == Waza.GIVE_UP) {
                this.setBattleStatus(BattleResultStatus.LOSE, roomId, room.getUserId1());
                this.setBattleStatus(BattleResultStatus.WIN, roomId, room.getUserId2());

            } else if (room.getWazaUser2() == Waza.GIVE_UP) {
                this.setBattleStatus(BattleResultStatus.WIN, roomId, room.getUserId1());
                this.setBattleStatus(BattleResultStatus.LOSE, roomId, room.getUserId2());

            // どちらかのキャラがすべて倒された
            } else if (battleStatus.isAllCharacterDead(room.getUserId1())) {
                this.setBattleStatus(BattleResultStatus.LOSE, roomId, room.getUserId1());
                this.setBattleStatus(BattleResultStatus.WIN, roomId, room.getUserId2());
            } else if (battleStatus.isAllCharacterDead(room.getUserId2())) {
                this.setBattleStatus(BattleResultStatus.WIN, roomId, room.getUserId1());
                this.setBattleStatus(BattleResultStatus.LOSE, roomId, room.getUserId2());
            } else {
                battleStatus.update(BattleResultStatus.COMMAND_INPUT, roomId);
            }
        } else {
            if (battleStatus.isCurrentChracterDead(userId)) {
                battleStatus.update(BattleResultStatus.INIT_CHANGE, userId);
            } else {
                battleStatus.update(BattleResultStatus.WAIT, userId);
            }
        }



        return res;
    }

    private boolean finishedBattle(String roomId) {
        List<CharacterStatusEntity> cs = characterStatusRepository.findByRoomId(roomId);
        RoomEntity room = roomRepository.findById(roomId).get();

        if (cs.stream().filter(c -> c.getUserId().equals(room.getUserId1()))
                .allMatch(c -> c.getHp() <= 0) ) {
            return true;
        } else if (cs.stream().filter(c -> c.getUserId().equals(room.getUserId2()))
                .allMatch(c -> c.getHp() <= 0) ) {
            return true;
        }
        return false;
    }

    public void setBattleStatus(BattleResultStatus battleResultStatus, String roomId, String userId) {
        battleStatusMap.get(roomId).update(battleResultStatus, userId);
    }

    public void setBattleStatus(BattleResultStatus battleResultStatus, String roomId) {
        RoomEntity room = roomRepository.findById(roomId).get();
        battleStatusMap.get(roomId).update(battleResultStatus, room.getUserId1());
        battleStatusMap.get(roomId).update(battleResultStatus, room.getUserId2());
    }
}
