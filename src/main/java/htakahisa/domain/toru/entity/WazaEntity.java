package htakahisa.domain.toru.entity;

import htakahisa.domain.toru.enums.AppendEffect;
import htakahisa.domain.toru.enums.ClientAction;
import htakahisa.domain.toru.enums.InAction;
import htakahisa.domain.toru.enums.Waza;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity(name = "waza")
@Getter
@Setter
public class WazaEntity {

    @Id
    @Enumerated(EnumType.STRING)
    private Waza waza;

    // 技名
    private String name;

    @Enumerated(EnumType.STRING)
    private ClientAction clientAction;

    private Long cp;

    private Long priority;

    // 威力
    private Long power;

    // 追加効果
    @Enumerated(EnumType.STRING)
    private AppendEffect appendEffect;

    private BigDecimal hitRate;

    // いつ発動する技か
    @Enumerated(EnumType.STRING)
    private InAction inAction;
}
