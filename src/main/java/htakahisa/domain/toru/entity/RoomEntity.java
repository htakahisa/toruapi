package htakahisa.domain.toru.entity;

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

    public boolean ready() {
        return Strings.isNotEmpty(userId1) && !"UNDEFINED".equals(userId2);
    }
}
