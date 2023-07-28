package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.Schedule;
import com.swayaan.nysf.entity.ScheduleTimeSlots;

@Repository
public interface ScheduleTimeSlotsRepository extends CrudRepository<ScheduleTimeSlots, Integer> {
	
	public Long countById(Integer id);

	public List<ScheduleTimeSlots> findAll();
	
	public List<ScheduleTimeSlots> findAllBySchedule(Schedule schedule);
	
	//@Query(value = "SELECT * FROM schedule_time_slots WHERE schedule_id = :scheduleId GROUP BY schedule_days_id ORDER BY time_slot ASC", nativeQuery = true)
	//public List<ScheduleTimeSlots> findAllByScheduleId(Integer scheduleId);
	
	@Query(value = "SELECT * FROM schedule_time_slots WHERE schedule_id = :scheduleId AND schedule_days_id = :scheduleDayId ORDER BY time_slot ASC", nativeQuery = true)
	public List<ScheduleTimeSlots> findAllByScheduleAndScheduleDay(Integer scheduleId, Integer scheduleDayId);
	
}
