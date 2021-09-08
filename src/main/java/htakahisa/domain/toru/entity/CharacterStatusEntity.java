package htakahisa.domain.toru.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

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

    // 攻撃
    private Long attack;

    // すばやさ
    private Long speed;

    // 回避率
    private Long dodgeRate;


    private BigDecimal attackRate = BigDecimal.ONE;
    private BigDecimal speedRate = BigDecimal.ONE;

    public Long getAttack() {
        return attackRate.multiply(BigDecimal.valueOf(attack)).longValue();
    }

    public Long getSpeed() {
        return speedRate.multiply(BigDecimal.valueOf(speed)).longValue();
    }

    public static CharacterStatusEntity of(String roomId, String userId, CharactersEntity character) {
        CharacterStatusEntity c = new CharacterStatusEntity();
        c.setRoomId(roomId);
        c.setCharacterId(character.getId());
        c.setUserId(userId);
        c.setAttack(character.getAttack());
        c.setDodgeRate(character.getDodgeRate());
        c.setHp(character.getHp());
        c.setName(character.getName());
        c.setSpeed(character.getSpeed());


        return c;
    }

    public static CharacterStatusEntity of(CharacterStatusEntity from) {
        CharacterStatusEntity to = new CharacterStatusEntity();
        to.setTurnCount(from.getTurnCount());
        to.setAttackRate(from.getAttackRate());
        to.setCharacterId(from.getCharacterId());
        to.setAttack(from.getAttack());
        to.setSpeed(from.getSpeed());
        to.setName(from.getName());
        to.setHp(from.getHp());
        to.setDodgeRate(from.getDodgeRate());
        to.setRoomId(from.getRoomId());
        to.setId(from.getId());
        to.setSpeedRate(from.getSpeedRate());

        return to;
    }
}
