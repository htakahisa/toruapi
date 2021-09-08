package htakahisa.domain.toru.entity;

import htakahisa.domain.toru.enums.EnvType;
import htakahisa.domain.toru.enums.Waza;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.apache.tomcat.util.buf.StringUtils;

import javax.persistence.Entity;
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

    private Waza wazaUser1;
    private Waza wazaUser2;

    private EnvType envType;


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
