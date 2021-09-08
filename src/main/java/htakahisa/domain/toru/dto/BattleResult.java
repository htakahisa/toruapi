package htakahisa.domain.toru.dto;

import htakahisa.controller.dto.BattleResultRes;
import htakahisa.domain.toru.enums.BattleResultStatus;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.util.HashMap;
import java.util.Map;

public class BattleResult {

    private Map<String/*roomId*/, ResultInfo> resultMap = new HashMap<>();

    public void setStatus(String roomId, BattleResultStatus battleResultStatus) {
        resultMap.get(roomId).setBattleResultStatus(battleResultStatus);
    }

    public void put(String roomId, BattleResultRes res) {

        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setRes(res);
        resultMap.put(roomId, resultInfo);
    }

    public BattleResultRes getBattleResult(String roomId, String userId) {
        ResultInfo resultInfo = resultMap.get(roomId);

        if (resultInfo.getUserId1().equals(userId)) {
            resultInfo.setUserId1(null);
        } else if (resultInfo.getUserId2().equals(userId)) {
            resultInfo.setUserId2(null);
        }

        if (Strings.isEmpty(resultInfo.getUserId1()) && Strings.isEmpty(resultInfo.getUserId2())) {
            resultInfo.setBattleResultStatus(BattleResultStatus.COMMAND_WAITING);
        }

        return resultInfo.getRes();
    }

    public boolean gotResult(String roomId) {
        ResultInfo resultInfo = resultMap.get(roomId);
        return Strings.isEmpty(resultInfo.getUserId1()) && Strings.isEmpty(resultInfo.getUserId2());
    }



    @Getter
    @Setter
    public static class ResultInfo {

        private BattleResultStatus battleResultStatus;

        private BattleResultRes res;

        private String userId1;

        private String userId2;

    }

}
