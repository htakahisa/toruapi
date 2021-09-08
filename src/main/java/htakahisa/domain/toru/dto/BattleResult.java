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

    public void putBattleResult(String roomId, BattleResultRes res) {

        ResultInfo resultInfo = resultMap.get(roomId);
        resultInfo.setRes(res);
        resultMap.put(roomId, resultInfo);
    }

    public BattleResultRes getBattleResult(String roomId, String userId) {
        ResultInfo resultInfo = resultMap.get(roomId);

        resultInfo.getUserIds().remove(userId);

        if (resultInfo.getUserIds().size() == 0) {
            resultInfo.setBattleResultStatus(BattleResultStatus.COMMAND_WAITING);
        }

        return resultInfo.getRes();
    }

    public BattleResultStatus getBattleResultStatus(String roomId) {
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

    }

}
