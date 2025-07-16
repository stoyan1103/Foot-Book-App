package com.example.reservation.repository;

import com.example.field.model.Field;
import com.example.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByFieldAndStartTime(Field field, LocalDateTime startTime);

    List<Reservation> findByEndTimeBeforeAndIsExpiredFalse(LocalDateTime dateTime);

    @Query("""
            SELECT CASE
                WHEN COUNT(r) > 0 THEN true
                ELSE false
            END FROM Reservation r
            WHERE r.field = :field 
            AND r.isExpired = false
            AND ((:startTime < r.endTime) AND (:endTime > r.startTime))
            """)
    boolean existsByFieldAndTimeOverlapAndIsExpiredFalse(@Param("field") Field field,
                                                         @Param("startTime") LocalDateTime startTime,
                                                         @Param("endTime") LocalDateTime endTime);
}