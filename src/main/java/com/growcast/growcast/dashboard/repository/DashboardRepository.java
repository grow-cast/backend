package com.growcast.growcast.dashboard.repository;

import com.growcast.growcast.dashboard.entity.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    //@Query("SELECT new com.growcast.growcast.dashboard.dto.DashboardMonthlyListDTO(d.dashboardId, d.dashboardTitle, d.createdAt) " +"FROM Dashboard d WHERE d.user.userId = :user_id AND d.createdAt BETWEEN :startDate AND :endDate")
    //List<DashboardMonthlyListDTO> findAllDashboardByUserIdAndCreatedAtBetween(Long user_id, LocalDateTime startDate, LocalDateTime endDate);

    List<Dashboard> findAllByUser_UserIdAndCreatedAtBetween(Long user_id, LocalDateTime startDate, LocalDateTime endDate);
}