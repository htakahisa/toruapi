package htakahisa.domain.toru.entity;

import htakahisa.domain.toru.enums.EnvType;
import htakahisa.domain.toru.enums.Waza;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.apache.tomcat.util.buf.StringUtils;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity(name = "room")
@Getter
@Setter
public class RoomEntity {

    @Id
    private String roomId;

    private String userId1;

    private String userId2;

    private Long characterId1;

    private Long characterId2;

    @Enumerated(EnumType.STRING)
    private Waza wazaUser1;
    @Enumerated(EnumType.STRING)
    private Waza wazaUser2;
    @Enumerated(EnumType.STRING)
    private EnvType envType;

    private Long user1Char1;
    private Long user1Char2;
    private Long user1Char3;

    private Long user2Char1;
    private Long user2Char2;
    private Long user2Char3;

    // userId をいれる
    private Long winner;


    public boolean ready() {
        return Strings.isNotEmpty(userId1) && !"UNDEFINED".equals(userId2);
    }

    public boolean commandReady() {
        return wazaUser1 != null && wazaUser2 != null;
    }

    public void setWaza(String userId, Waza waza) {
        if (userId1.equals(userId)) {
            setWazaUser1(waza);
        } else if (userId2.equals(userId)) {
            setWazaUser2(waza);
        }
    }

    public void clearWaza() {
        setWazaUser1(null);
        setWazaUser2(null);
    }
}
