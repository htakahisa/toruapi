package htakahisa.domain.toru.entity;

import htakahisa.domain.toru.enums.SpecialAbility;
import htakahisa.domain.toru.enums.Waza;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

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
    @Column(nullable= false, precision=20, scale=2)
    private BigDecimal dodgeRate;

    @Enumerated(EnumType.STRING)
    private Waza waza1;
    @Enumerated(EnumType.STRING)
    private Waza waza2;
    @Enumerated(EnumType.STRING)
    private Waza waza3;
    @Enumerated(EnumType.STRING)
    private Waza waza4;
    @Enumerated(EnumType.STRING)
    private SpecialAbility specialAbility;
}

