package htakahisa.domain.toru.repository;

import htakahisa.domain.toru.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, String> {

    List<RoomEntity> findByUserId1(String userId);

    List<RoomEntity> findByUserId2(String userId);

}
