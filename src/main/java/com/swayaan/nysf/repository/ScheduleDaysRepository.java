package com.swayaan.nysf.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.swayaan.nysf.entity.ScheduleDays;

@Repository
public interface ScheduleDaysRepository extends CrudRepository<ScheduleDays, Integer> {
	
	public Long countById(Integer id);

	public List<ScheduleDays> findAll();
	
	@Query(value = "SELECT * FROM schedule_days WHERE schedule_id = :scheduleId ORDER BY DATE_FORMAT(date, '%Y-%m-%d') ASC", nativeQuery = true)
	public List<ScheduleDays> getAllByScheduleOrderByDateAsc(Integer scheduleId);

	@Query(value = "SELECT * FROM schedule_days WHERE schedule_id = :scheduleId ORDER BY sequence_number ASC", nativeQuery = true)
	public List<ScheduleDays> getAllByScheduleOrderBySequenceNumber(Integer scheduleId);

	@Query(value = "SELECT * FROM schedule_days WHERE schedule_id = :scheduleId ", nativeQuery = true)
	public List<ScheduleDays> findBySchedule(Integer scheduleId);
	
}
