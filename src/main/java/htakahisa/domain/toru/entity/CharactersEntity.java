package htakahisa.domain.toru.entity;

import htakahisa.domain.toru.enums.SpecialAbility;
import htakahisa.domain.toru.enums.Waza;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "characters")
@Getter
@Setter
public class CharactersEntity {

    @Id
    private Long id;

    private String name;

    private Long hp;

    // 攻撃
    private Long attack;

    // すばやさ
    private Long speed;

    // 回避率
    private Long dodgeRate;

    private Waza waza1;
    private Waza waza2;
    private Waza waza3;
    private Waza waza4;

    private SpecialAbility specialAbility;
}

