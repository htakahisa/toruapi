package htakahisa.domain.toru.dto;

import htakahisa.controller.dto.BattleResultRes;
import htakahisa.domain.toru.enums.BattleResultStatus;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.util.*;

public class BattleResult {

    private Map<String/*roomId*/, ResultInfo> resultMap = new HashMap<>();

    public void setStatus(String roomId, BattleResultStatus battleResultStatus) {

        ResultInfo resultInfo = resultMap.get(roomId);
        if (resultInfo == null) {
            resultInfo = new ResultInfo();
            resultMap.put(roomId, resultInfo);
        }

        resultMap.get(roomId).setBattleResultStatus(battleResultStatus);
    }

    public void putBattleResult(String roomId, BattleResultRes res, boolean isSomeoneDead) {

        ResultInfo resultInfo = resultMap.get(roomId);
        resultInfo.setRes(res);
        resultInfo.setSomeoneDead(isSomeoneDead);
        resultMap.put(roomId, resultInfo);
    }

    public BattleResultRes getBattleResult(String roomId, String userId) {
        ResultInfo resultInfo = resultMap.get(roomId);

        resultInfo.getUserIds().remove(userId);

        if (resultInfo.getUserIds().size() == 0) {

            if (resultInfo.isSomeoneDead()) {
                resultInfo.setBattleResultStatus(BattleResultStatus.CHARACTER_SELECT);
            } else {
                resultInfo.setBattleResultStatus(BattleResultStatus.COMMAND_WAITING);
            }
        }

        return resultInfo.getRes();
    }

    public BattleResultStatus getBattleResultStatus(String roomId) {
        if (resultMap.get(roomId) == null) {
            return null;
        }
        return resultMap.get(roomId).getBattleResultStatus();
    }

    public void setUserId(String roomId, String userId) {
        resultMap.get(roomId).getUserIds().add(userId);
    }

    @Getter
    @Setter
    public static class ResultInfo {

        private BattleResultStatus battleResultStatus;

        private BattleResultRes res;

        private Set<String> userIds = new HashSet<>();

        private boolean isSomeoneDead;
    }

}
