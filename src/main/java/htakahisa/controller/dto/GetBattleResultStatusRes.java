package htakahisa.controller.dto;

import htakahisa.domain.toru.enums.BattleResultStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBattleResultStatusRes {

    private BattleResultStatus battleResultStatus;
}
