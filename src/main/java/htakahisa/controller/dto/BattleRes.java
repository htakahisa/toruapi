package htakahisa.controller.dto;

import htakahisa.domain.toru.entity.CharacterStatusEntity;
import htakahisa.domain.toru.enums.BattleResultStatus;
import htakahisa.domain.toru.enums.ClientAction;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BattleRes {

    private BattleResultStatus battleResultStatus;


    public static BattleRes of(BattleResultStatus status) {
        BattleRes res = new BattleRes();
        res.setBattleResultStatus(status);
        return res;
    }
}
