package htakahisa.domain.toru.entity;

import htakahisa.domain.toru.enums.AppendEffect;
import htakahisa.domain.toru.enums.SpecialAbility;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity(name = "character_status")
@Getter
@Setter
public class CharacterStatusEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long characterId;

    private String roomId;

    private String userId;

    private long turnCount;

    private String name;

    private Long hp;

    private Long orgHp;

    // 攻撃
    private Long attack;

    // すばやさ
    private Long speed;

    // 回避率
    @Column(nullable= false, precision=20, scale=2)
    private BigDecimal dodgeRate;

    @Enumerated(EnumType.STRING)
    private SpecialAbility specialAbility;
    @Enumerated(EnumType.STRING)
    private AppendEffect appendEffect;

    private BigDecimal attackRate = BigDecimal.ONE;
    private BigDecimal speedRate = BigDecimal.ONE;

    public Long getAttack() {
        return attackRate.multiply(BigDecimal.valueOf(attack)).longValue();
    }

    public Long getSpeed() {
        return speedRate.multiply(BigDecimal.valueOf(speed)).longValue();
    }

    public boolean isDead() {
        return getHp() <= 0;
    }

    public void healing(Long heal, Long originalHp) {
        BigDecimal healingRate = BigDecimal.valueOf(heal).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN);

        Long newHp = getHp() + BigDecimal.valueOf(originalHp).multiply(healingRate).longValue();
        if (newHp > this.getOrgHp()) {
            newHp = this.getOrgHp();
        }

        this.setHp(newHp);
    }

    public static CharacterStatusEntity of(String roomId, String userId, CharactersEntity character) {
        CharacterStatusEntity c = new CharacterStatusEntity();
        c.setRoomId(roomId);
        c.setCharacterId(character.getId());
        c.setUserId(userId);
        c.setAttack(character.getAttack());
        c.setDodgeRate(character.getDodgeRate());
        c.setHp(character.getHp());
        c.setOrgHp(character.getHp());
        c.setName(character.getName());
        c.setSpeed(character.getSpeed());
        c.setSpecialAbility(character.getSpecialAbility());
        c.setAppendEffect(AppendEffect.NONE);
        return c;
    }

    public static CharacterStatusEntity of(CharacterStatusEntity from) {
        CharacterStatusEntity to = new CharacterStatusEntity();
        to.setTurnCount(from.getTurnCount());
        to.setUserId(from.getUserId());
        to.setAttackRate(from.getAttackRate());
        to.setCharacterId(from.getCharacterId());
        to.setAttack(from.getAttack());
        to.setSpeed(from.getSpeed());
        to.setName(from.getName());
        to.setHp(from.getHp());
        to.setOrgHp(from.getOrgHp());
        to.setDodgeRate(from.getDodgeRate());
        to.setRoomId(from.getRoomId());
        to.setId(from.getId());
        to.setSpeedRate(from.getSpeedRate());
        to.setSpecialAbility(from.getSpecialAbility());
        to.setAppendEffect(from.getAppendEffect());
        return to;
    }

    public void initData() {
        this.setTurnCount(0);
        this.setSpeedRate(BigDecimal.ONE);
        this.setAttackRate(BigDecimal.ONE);
    }
}
