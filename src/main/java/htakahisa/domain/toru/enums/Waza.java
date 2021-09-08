package htakahisa.domain.toru.enums;

import lombok.Getter;
import lombok.Setter;

public enum Waza {

    PUNCH(1), // パンチ
    CHANGE(900), // 交代
    GIVE_UP(901), // 降参
    ;

    @Getter
    @Setter
    private int id;

    Waza(int id) {
        this.id = id;
    }
}
