package com.growcast.growcast.grownCrops.repository;

import com.growcast.growcast.grownCrops.dto.GrownCropsDropdownDTO;
import com.growcast.growcast.grownCrops.dto.GrownCropsResponse;
import com.growcast.growcast.grownCrops.entity.GrownCrops;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrownCropsRepository extends JpaRepository<GrownCrops, Long> {

    @Query("SELECT new com.growcast.growcast.grownCrops.dto.GrownCropsDropdownDTO(gc.gcid, gc.name) " +
            "FROM GrownCrops gc WHERE gc.user.userId = :user_id")
    List<GrownCropsDropdownDTO> findByUserId(@Param("user_id") Long user_id);

    @Query("SELECT gc.gcid FROM GrownCrops gc WHERE gc.user.userId = :userId AND gc.name = :cropName")
    Optional<Long> findGcIdByUserIdAndCropName(@Param("userId") Long userId, @Param("cropName") String cropName);

    @Query("SELECT new com.growcast.growcast.grownCrops.dto.GrownCropsResponse(" +
            "gc.gcid, gc.name, gc.nickname, gc.picture, gc.plantingDate, gc.expectedHarvestDate) " +
            "FROM GrownCrops gc WHERE gc.user.userId = :userId ORDER BY gc.createdAt DESC")
    List<GrownCropsResponse> findAllByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    Optional<GrownCrops> findByGcidAndUserUserId(Long gcid, Long userId);
}