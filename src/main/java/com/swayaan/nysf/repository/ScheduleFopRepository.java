package com.swayaan.nysf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.swayaan.nysf.entity.Schedule;
import com.swayaan.nysf.entity.ScheduleDays;
import com.swayaan.nysf.entity.ScheduleFop;
import com.swayaan.nysf.entity.ScheduleTimeSlots;

@Repository
public interface ScheduleFopRepository extends CrudRepository<ScheduleFop, Integer> {
	
	public Long countById(Integer id);

	public List<ScheduleFop> findAll();

	public List<ScheduleFop> findAllBySchedule(Schedule schedule);
	
	public List<ScheduleFop> findAllByScheduleDays(ScheduleDays scheduleDays);
	
	public List<ScheduleFop> findAllByScheduleTimeSlots(ScheduleTimeSlots scheduleTimeSlots);
	
	//@Query(value = "SELECT * FROM schedule_fop WHERE scheduleTimeSlotsId = :scheduleTimeSlotsId ORDER BY date ASC", nativeQuery = true)
	
	/*@Query(value = "SELECT *, time_slot FROM schedule_time_slots"
			+ " FROM schedule_fop"
			+ " INNER JOIN schedule_time_slots on schedule_fop.schedule_time_slots_id = schedule_time_slots.id"
			+ " WHERE schedule_fop.schedule_time_slots_id = :scheduleTimeSlotsId"
			+ " ORDER BY time_slot ASC", nativeQuery = true)*/
	@Query(value = "SELECT schedule_fop.* , schedule_time_slots.time_slot FROM schedule_fop LEFT JOIN schedule_time_slots on schedule_fop.schedule_time_slots_id = schedule_time_slots.id  WHERE schedule_fop.schedule_time_slots_id = :scheduleTimeSlotsId ORDER BY time_slot ASC", nativeQuery = true)
	public List<ScheduleFop> findAllByScheduleTimeSlotsOrderBySlot(Integer scheduleTimeSlotsId);

	/*@Query(value = "SELECT * FROM schedule_days WHERE id = :scheduleId ORDER BY date ASC", nativeQuery = true)
	public List<ScheduleFop> findAllByScheduleAndScheduleDay(Integer scheduleId, Integer scheduleDayId);*/
	
}
