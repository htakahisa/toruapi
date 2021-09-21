package htakahisa.controller.dto;

import htakahisa.domain.toru.entity.RoomEntity;
import htakahisa.domain.toru.enums.BattleResultStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBattleResultStatusRes {

    private UserStatus user1 = new GetBattleResultStatusRes.UserStatus();
    private UserStatus user2 = new GetBattleResultStatusRes.UserStatus();

    public boolean isAllUserWait() {
        return user1 != null &&
                user2 != null &&
                user1.getBattleResultStatus() == BattleResultStatus.WAIT &&
                user2.getBattleResultStatus() == BattleResultStatus.WAIT;
    }

    public boolean isAllCharacterDead(String userId) {
        UserStatus userStatus = this.getUserStatus(userId);
        return !userStatus.isAliveCharacter1 && !userStatus.isAliveCharacter2 && !userStatus.isAliveCharacter3;
    }

    public void setSelectedCharacterId(String userId, Long selectedCharacterId) {
        UserStatus userStatus = this.getUserStatus(userId);
        userStatus.setSelectedCharacterId(selectedCharacterId);
    }

    public void setDead(String userId) {
        UserStatus userStatus = this.getUserStatus(userId);
        if (userStatus.getSelectedCharacterId() == userStatus.getCharacterId1()) {
            userStatus.setAliveCharacter1(false);
        }
        if (userStatus.getSelectedCharacterId() == userStatus.getCharacterId2()) {
            userStatus.setAliveCharacter2(false);
        }
        if (userStatus.getSelectedCharacterId() == userStatus.getCharacterId3()) {
            userStatus.setAliveCharacter3(false);
        }
    }

    public boolean isCurrentChracterDead(String userId) {
        if (userId.equals(user1.getUserId())) {
            if (user1.getSelectedCharacterId() == user1.getCharacterId1() && !user1.isAliveCharacter1) {
                return true;
            }
            if (user1.getSelectedCharacterId() == user1.getCharacterId2() && !user1.isAliveCharacter2) {
                return true;
            }
            if (user1.getSelectedCharacterId() == user1.getCharacterId3() && !user1.isAliveCharacter3) {
                return true;
            }
        }
        if (userId.equals(user2.getUserId())) {
            if (user2.getSelectedCharacterId() == user2.getCharacterId1() && !user2.isAliveCharacter1) {
                return true;
            }
            if (user2.getSelectedCharacterId() == user2.getCharacterId2() && !user2.isAliveCharacter2) {
                return true;
            }
            if (user2.getSelectedCharacterId() == user2.getCharacterId3() && !user2.isAliveCharacter3) {
                return true;
            }
        }


        return false;
    }


    public GetBattleResultStatusRes update(BattleResultStatus battleResultStatus, String userId) {
        if (userId.equals(user1.getUserId())) {
            if (battleResultStatus == user1.getBattleResultStatus()) {
                return this;
            }

            user1.setBattleResultStatusOld(user1.getBattleResultStatus());
            user1.setBattleResultStatus(battleResultStatus);
        } else if (userId.equals(user2.getUserId())) {
            if (battleResultStatus == user2.getBattleResultStatus()) {
                return this;
            }
            user2.setBattleResultStatusOld(user2.getBattleResultStatus());
            user2.setBattleResultStatus(battleResultStatus);
        }
        return null;
    }

    public GetBattleResultStatusRes update(BattleResultStatus battleResultStatus, CreateRoomReq req, RoomEntity room) {
        return update(req.getUserId(), battleResultStatus, req.getCharacterId1(), req.getCharacterId1(),req.getCharacterId2(), req.getCharacterId3(),
                true, true, true);
    }
    public GetBattleResultStatusRes update(String userId,
                                           BattleResultStatus battleResultStatus,
                                           Long selectedCharacterId,
                                           Long characterId1,
                                           Long characterId2,
                                           Long characterId3,
                                           boolean isAliveCharacter1,
                                           boolean isAliveCharacter2,
                                           boolean isAliveCharacter3) {
        UserStatus update = null;
        if (userId.equals(user1.getUserId())) {
            update = user1;
        } else if (userId.equals(user2.getUserId())) {
            update = user2;
        } else {
            if (user1.getUserId() == null) {
                update = user1;
            } else {
                update = user2;
            }
            update.setUserId(userId);
        }

        if (battleResultStatus == update.getBattleResultStatus()) {
            return this;
        }
        update.setBattleResultStatus(battleResultStatus);
        update.setSelectedCharacterId(selectedCharacterId);
        update.setCharacterId1(characterId1);
        update.setCharacterId2(characterId2);
        update.setCharacterId3(characterId3);
        update.setAliveCharacter1(isAliveCharacter1);
        update.setAliveCharacter2(isAliveCharacter2);
        update.setAliveCharacter3(isAliveCharacter3);

        return this;
    }

    public UserStatus getUserStatus(String userId) {
        if (userId.equals(user1.getUserId())) {
            return user1;
        } else if (userId.equals(user2.getUserId())) {
            return user2;
        }
        return null;
    }

    @Getter
    @Setter
    public static class UserStatus {
        private String userId;
        private BattleResultStatus battleResultStatus;
        private BattleResultStatus battleResultStatusOld;
        private Long selectedCharacterId;

        private Long characterId1;
        private Long characterId2;
        private Long characterId3;

        private boolean isAliveCharacter1 = true;
        private boolean isAliveCharacter2 = true;
        private boolean isAliveCharacter3 = true;
    }


}
