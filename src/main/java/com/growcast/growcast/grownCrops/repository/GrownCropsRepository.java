package com.growcast.growcast.grownCrops.repository;

import com.growcast.growcast.grownCrops.dto.GrownCropsDropdownDTO;
import com.growcast.growcast.grownCrops.entity.GrownCrops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrownCropsRepository extends JpaRepository<GrownCrops, Long> {
    //드롭다운용 작물 이름 가져오기 위에서 user_id로 조회
    @Query("SELECT gc.gc_id, gc.name FROM GrownCrops gc WHERE gc.user.userId = :user_id")
    List<GrownCropsDropdownDTO> findByUserId(@Param("user_id") Long user_id);

    //대시보드 작성시 사용자가 등록한 작물이 있는지 조회
    @Query("SELECT gc.gc_id FROM GrownCrops gc WHERE gc.user.userId = :user_id AND gc.name = :cropName")
    Optional<Long> findByGrownCropsNameAndUserId(@Param("user_id") Long user_id,@Param("cropName") String cropName);
}