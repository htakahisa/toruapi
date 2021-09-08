package htakahisa.domain.toru.entity;

import htakahisa.domain.toru.enums.AppendEffect;
import htakahisa.domain.toru.enums.Waza;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "waza")
@Getter
@Setter
public class WazaEntity {

    @Id
    private Waza waza;

    // 技名
    private String name;

    private Long cp;

    private Long priority;

    // 威力
    private Long power;

    // 追加効果
    private AppendEffect appendEffect;

    // いつ発動する技か
    private InAction inAction;
}
