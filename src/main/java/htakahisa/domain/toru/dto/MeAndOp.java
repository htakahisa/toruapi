package htakahisa.domain.toru.dto;

import htakahisa.domain.toru.entity.CharacterStatusEntity;
import htakahisa.domain.toru.entity.WazaEntity;
import htakahisa.domain.toru.enums.Waza;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Random;

@Getter
@Setter
public class MeAndOp {

    private CharacterStatusEntity characterStatus1;
    private CharacterStatusEntity characterStatus2;

    public static MeAndOp of(CharacterStatusEntity c1, CharacterStatusEntity c2) {
        MeAndOp m = new MeAndOp();
        m.setCharacterStatus1(c1);
        m.setCharacterStatus2(c2);
        return m;
    }

    public Long attack(WazaEntity waza, String userId) {
        Long hp = getOp(userId).getHp() - (waza.getPower() + getMe(userId).getAttack());
        if (hp < 0) {
            hp = 0L;
        }
        return hp;
    }

    public boolean isHit(WazaEntity waza, String userId) {
        BigDecimal hitRate = waza.getHitRate().add(getMe(userId).getDodgeRate());
        int i = new Random().nextInt(100) + 1;

        return hitRate.multiply(new BigDecimal("100")).intValue() >= i;
    }

    public boolean isAppendEffect(WazaEntity waza) {
        BigDecimal appendEffectRate = waza.getAppendEffectRate();
        int i = new Random().nextInt(100) + 1;

        return appendEffectRate.multiply(new BigDecimal("100")).intValue() >= i;
    }

    public boolean isSomeoneDead() {
        return characterStatus1.getHp() <= 0 || characterStatus2.getHp() <= 0;
    }


    public CharacterStatusEntity getMe(String userId) {
        if (userId.equals(characterStatus1.getUserId())) {
            return characterStatus1;
        } else if (userId.equals(characterStatus2.getUserId())) {
            return characterStatus2;
        }
        return null;
    }
    public void setMe(String userId, CharacterStatusEntity c) {
        if (userId.equals(characterStatus1.getUserId())) {
            characterStatus1 = c;
        } else if (userId.equals(characterStatus2.getUserId())) {
            characterStatus2 = c;
        }
    }

    public CharacterStatusEntity getOp(String userId) {
        if (userId.equals(characterStatus1.getUserId())) {
            return characterStatus2;
        } else if (userId.equals(characterStatus2.getUserId())) {
            return characterStatus1;
        }
        return null;
    }
}
