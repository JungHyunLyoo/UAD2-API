package com.uad2.application.attendance.repository;

import com.uad2.application.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    @Query(value="SELECT * FROM Attendance WHERE member_seq = ?1 and available_date like ?2%",nativeQuery = true)
    List<Attendance> findByMemberSeqAndAvailableDate(int memberSeq, String availableDate);
}
