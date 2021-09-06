package htakahisa.domain.toru;

import htakahisa.controller.dto.CreateRoomReq;
import htakahisa.domain.toru.entity.RoomEntity;
import htakahisa.domain.toru.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

@Component
public class ToruLogic {

    @Autowired
    private RoomRepository roomRepository;

    public RoomEntity saveRoom(CreateRoomReq req) {

        List<RoomEntity> rooms = roomRepository.findByUserId1(req.getUserId());

        if (CollectionUtils.isEmpty(rooms)) {

            // userId2 にあるなら戦闘中なのでその roomId を返す
            List<RoomEntity> user2Rooms = roomRepository.findByUserId2(req.getUserId());
            if (user2Rooms.size() > 0) {
                return user2Rooms.get(0);
            }

            List<RoomEntity> undefineds =
                    roomRepository.findByUserId2("UNDEFINED");
            if (undefineds.size() == 0) {
                RoomEntity room = new RoomEntity();
                room.setRoomId(UUID.randomUUID().toString());
                room.setUserId1(req.getUserId());
                room.setUserId2("UNDEFINED");
                return roomRepository.save(room);
            } else {

                RoomEntity room = undefineds.get(0);
                room.setUserId2(req.getUserId());
                return roomRepository.save(room);
            }
        } else {
            return rooms.get(0);
        }
    }
}
