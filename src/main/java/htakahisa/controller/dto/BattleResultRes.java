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
public class BattleResultRes {

    private BattleResultStatus battleResultStatus;

    private List<BattleResult> results = new ArrayList<>();

    @Getter
    @Setter
    public static class BattleResult {

        private ResultAction change;

        private ResultAction inTheBattle;

        private ResultAction beforeAttack;

        private ResultAction inAttack;

        private ResultAction afterAttack;

        private ResultAction endTheBattle;
    }


    @Getter
    @Setter
    public static class ResultAction {

        private String message1;

        private String message2;

        private ClientAction action;

        private CharacterStatusEntity characterStatus1;

        private CharacterStatusEntity characterStatus2;

    }

}
