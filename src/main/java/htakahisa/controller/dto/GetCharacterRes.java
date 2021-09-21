package htakahisa.controller.dto;

import htakahisa.domain.toru.entity.CharactersEntity;
import htakahisa.domain.toru.entity.WazaEntity;
import htakahisa.domain.toru.enums.SpecialAbility;
import htakahisa.domain.toru.enums.Waza;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Getter
@Setter
public class GetCharacterRes {

    private Long id;

    private String name;

    private Long hp;

    // 攻撃
    private Long attack;

    // すばやさ
    private Long speed;

    // 回避率
    private BigDecimal dodgeRate;

    private Waza waza1;
    private Waza waza2;
    private Waza waza3;
    private Waza waza4;
    private SpecialAbility specialAbility;
    private String wazaName1;
    private String wazaName2;
    private String wazaName3;
    private String wazaName4;


    public static GetCharacterRes of(CharactersEntity c, WazaEntity w1, WazaEntity w2, WazaEntity w3, WazaEntity w4) {
        GetCharacterRes res = new GetCharacterRes();
        BeanUtils.copyProperties(c, res);

        res.setWazaName1(w1.getName());
        res.setWazaName2(w2.getName());
        res.setWazaName3(w3.getName());
        res.setWazaName4(w4.getName());

        return res;
    }
}
