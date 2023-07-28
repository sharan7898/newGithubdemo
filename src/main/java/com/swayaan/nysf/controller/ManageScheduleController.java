package com.swayaan.nysf.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.swayaan.nysf.entity.Championship;
import com.swayaan.nysf.entity.ChampionshipStatusEnum;
import com.swayaan.nysf.entity.Participant;
import com.swayaan.nysf.entity.Schedule;
import com.swayaan.nysf.entity.ScheduleDays;
import com.swayaan.nysf.entity.ScheduleFop;
import com.swayaan.nysf.entity.ScheduleTimeSlots;
import com.swayaan.nysf.entity.User;
import com.swayaan.nysf.exception.EntityNotFoundException;
import com.swayaan.nysf.service.ChampionshipService;
import com.swayaan.nysf.service.ScheduleDaysService;
import com.swayaan.nysf.service.ScheduleFopService;
import com.swayaan.nysf.service.ScheduleService;
import com.swayaan.nysf.service.ScheduleTimeSlotsService;
import com.swayaan.nysf.service.UserService;
import com.swayaan.nysf.utils.CommonUtil;

@Controller
@RequestMapping("/admin")
public class ManageScheduleController {

	@Autowired
	ScheduleService service;
	@Autowired
	private UserService userService;
	@Autowired
	ChampionshipService championshipService;
	@Autowired
	ScheduleTimeSlotsService scheduleTimeSlotsService;
	@Autowired
	ScheduleDaysService scheduleDaysService;
	@Autowired
	ScheduleFopService scheduleFopService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ManageScheduleController.class);

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-schedule")
	public String listFirstPageSchedule(Model model,HttpServletRequest request) {
		LOGGER.info("Entered listFirstPageSchedule method -ManageScheduleController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);
		LOGGER.info("Exit listFirstPageSchedule method -ManageScheduleController");

		return listAllScheduleByPage(1, model, "description", "asc", "", "", request);
		
	}
	
	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-schedule/page/{pageNum}")
	public String listAllScheduleByPage(@PathVariable(name = "pageNum") int pageNum,Model model,@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword1", required = false) String description,
			@RequestParam(name = "keyword2", required = false) String championshipName,HttpServletRequest request) {
		LOGGER.info("Entered listAllScheduleByPage method -ManageScheduleController");
		//For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl,currentUser);

		Page<Schedule> page=service.listByPage(pageNum, sortField, sortDir, description, championshipName);
		List<Schedule> listSchedules = page.getContent();
			
		long startCount = (pageNum - 1) * service.RECORDS_PER_PAGE + 1;
		long endCount = startCount + service.RECORDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("moduleURL", "/admin/manage-schedule");
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword1", description);
		model.addAttribute("keyword2", championshipName);
		Schedule schedule = new Schedule();
		model.addAttribute("listSchedules", listSchedules);
		model.addAttribute("pageTitle", "Manage Schedule");
		model.addAttribute("Schedule", schedule);
		LOGGER.info("Exit listAllScheduleByPage method -ManageScheduleController");

		return "administration/manage_schedule";
	}

//	@GetMapping("/manage-schedule")
//	public String listAllSchedules(Model model, HttpServletRequest request) {
//
//		LOGGER.info("Entered listAllSchedules ManageScheduleController");
//		// For Navigation history
//		String mappedUrl = request.getRequestURI();
//		User currentUser = CommonUtil.getCurrentUser();
//		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
//
//		List<Schedule> listSchedules = service.listAllSchedules();
//		model.addAttribute("listSchedules", listSchedules);
//		model.addAttribute("pageTitle", "Manage Schedule");
//		return "administration/manage_schedule";
//	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-schedule/new")
	public String newSchedule(Model model, HttpServletRequest request) {
		LOGGER.info("Entered newSchedule method -ManageScheduleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		Schedule schedule = new Schedule();
		schedule.setEnabled(true);

		ScheduleDays scheduleDays = new ScheduleDays();
		model.addAttribute("scheduleDays", scheduleDays);

		ScheduleTimeSlots scheduleTimeSlots = new ScheduleTimeSlots();
		model.addAttribute("scheduleTimeSlots", scheduleTimeSlots);

		ScheduleFop scheduleFop = new ScheduleFop();
		model.addAttribute("scheduleFop", scheduleFop);

		List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();

		model.addAttribute("listChampionships", listChampionships);
		model.addAttribute("schedule", schedule);
		model.addAttribute("pageTitle", "Add Schedule");
		LOGGER.info("Exit newSchedule method -ManageScheduleController");

		return "administration/schedule_form";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-schedule/save")
	public String saveSchedule(Schedule schedule, RedirectAttributes redirectAttributes, HttpServletRequest request)
			throws IOException {
		LOGGER.info("Entered saveSchedule method -ManageScheduleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		String message = "";
		String errorMessage = "";
		
		message = "The Schedule "+schedule.getSport_name() + " has been saved successfully!";
		errorMessage = "This Schedule cannot be edited beacause championship is ongoing/completed!";
		if(schedule.getId()!=null) {
			if(!schedule.getChampionship().getStatus().equals(ChampionshipStatusEnum.SCHEDULED)) {
			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
			return "redirect:/admin/manage-schedule/edit/" + schedule.getId();
			}
		}
		
		service.save(schedule);
		redirectAttributes.addFlashAttribute("message", message);
		LOGGER.info("Exit saveSchedule method -ManageScheduleController");

		return "redirect:/admin/manage-schedule/edit/" + schedule.getId();
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-schedule/{id}/enabled/{status}")
	public String updateScheduleEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws EntityNotFoundException {
		LOGGER.info("Entered updateScheduleEnabledStatus method -ManageScheduleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		Schedule schedule = service.get(id);

		service.updateScheduleEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Schedule " + schedule.getId() + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);
		LOGGER.info("Exit updateScheduleEnabledStatus method -ManageScheduleController");

		return "redirect:/admin/manage-schedule";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-schedule/edit/{id}")
	public String editSchedule(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws EntityNotFoundException {
		LOGGER.info("Entered editSchedule method -ManageScheduleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			Schedule schedule = service.get(id);
			model.addAttribute("schedule", schedule);

			ScheduleDays scheduleDays = new ScheduleDays();
			model.addAttribute("scheduleDays", scheduleDays);

			ScheduleTimeSlots scheduleTimeSlots = new ScheduleTimeSlots();
			model.addAttribute("scheduleTimeSlots", scheduleTimeSlots);

			ScheduleFop scheduleFop = new ScheduleFop();
			model.addAttribute("scheduleFop", scheduleFop);

			// get the days in schedule days if available
			List<ScheduleDays> listScheduleDays = new ArrayList<ScheduleDays>();
			listScheduleDays = scheduleDaysService.getScheduleDaysOrderBySequenceNumber(id);
			if (listScheduleDays.size() != 0) {
				List<ScheduleDays> listSortedScheduleDays = listScheduleDays.stream()
						.sorted(Comparator.comparing(ScheduleDays::getSequenceNumber)).collect(Collectors.toList());

				model.addAttribute("listScheduleDays", listSortedScheduleDays);
			} else {
				model.addAttribute("listScheduleDays", listScheduleDays);
			}

			List<ScheduleTimeSlots> listScheduleTimeSlots = new ArrayList<ScheduleTimeSlots>();
			listScheduleTimeSlots = scheduleTimeSlotsService.getTimeSlotsForSchedule(schedule);
			model.addAttribute("listScheduleTimeSlots", listScheduleTimeSlots);

			List<ScheduleFop> listScheduleFops = new ArrayList<ScheduleFop>();
			listScheduleFops = scheduleFopService.getScheduleFopForSchedule(schedule);
			model.addAttribute("listScheduleFops", listScheduleFops);

			List<Championship> listChampionships = championshipService.listAllChampionshipsByNotDeleted();
			model.addAttribute("listChampionships", listChampionships);

			model.addAttribute("pageTitle", "Edit Schedule");
			return "administration/schedule_form";
		} catch (EntityNotFoundException ex) {
			LOGGER.error("Schedule with id" + id + "not found" + ex.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", "Schedule with id" + id + "not found");
			LOGGER.info("Exit editSchedule method -ManageScheduleController");

			return "redirect:/admin/manage-schedule";
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-schedule/view/{id}")
	public String viewSchedule(@PathVariable(name = "id") Integer scheduleId, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws EntityNotFoundException {
		LOGGER.info("Entered viewSchedule method -ManageScheduleController");

		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		Schedule schedule = service.get(scheduleId);
		// List<ScheduleDays> listScheduleDays =
		// scheduleDaysService.getScheduleDaysOrderByDate(scheduleId);
		List<ScheduleDays> listScheduleDays = scheduleDaysService.getScheduleDaysOrderBySequenceNumber(scheduleId);
		LOGGER.info("listScheduleDays count:" + listScheduleDays.size());
		LOGGER.info("listScheduleDays :" + Arrays.toString(listScheduleDays.toArray()));

		// get all time slots for the schedule days
		LinkedHashMap<ScheduleDays, LinkedHashMap<ScheduleTimeSlots, List<ScheduleFop>>> mapScheduleTimeSlots = new LinkedHashMap<>();

		for (ScheduleDays scheduleDay : listScheduleDays) {
			List<ScheduleTimeSlots> listTimeSlotsByScheduleDay = new ArrayList<ScheduleTimeSlots>();
			listTimeSlotsByScheduleDay = scheduleTimeSlotsService.getAllByScheduleAndScheduleDays(scheduleId,
					scheduleDay.getId());
			LinkedHashMap<ScheduleTimeSlots, List<ScheduleFop>> mapScheduleFops = new LinkedHashMap<>();
			// System.out.println("listTimeSlotsByScheduleDay" + :
			// Arrays.toString(listScheduleDays.toArray()));

			if (listTimeSlotsByScheduleDay.size() != 0) {
				for (ScheduleTimeSlots timeSlot : listTimeSlotsByScheduleDay) {
					List<ScheduleFop> listFopByScheduleTimeSlots = new ArrayList<ScheduleFop>();
					listFopByScheduleTimeSlots = scheduleFopService
							.getAllByScheduleTimeSlotsOrderBySlot(timeSlot.getId());
					if (listFopByScheduleTimeSlots.size() != 0) {
						mapScheduleFops.put(timeSlot, listFopByScheduleTimeSlots);
					}

				}
				if (mapScheduleFops.size() != 0) {
					mapScheduleTimeSlots.put(scheduleDay, mapScheduleFops);
				}

				// Collection<List<ScheduleFop>> values1 = mapScheduleFops.values();
				// System.out.println("Inner hashmap values :" + values1 );
			}
		}

		/*
		 * Set<ScheduleDays> allKeys = mapScheduleTimeSlots.keySet();
		 * System.out.println("mapScheduleTimeSlots keys :" + allKeys);
		 * Collection<LinkedHashMap<ScheduleTimeSlots,List<ScheduleFop>>> values =
		 * mapScheduleTimeSlots.values(); System.out.println("Outer hashmap values :" +
		 * values );
		 */

		model.addAttribute("mapScheduleTimeSlots", mapScheduleTimeSlots);

		List<ScheduleFop> listScheduleFops = new ArrayList<ScheduleFop>();
		listScheduleFops = scheduleFopService.getScheduleFopForSchedule(schedule);
		model.addAttribute("listScheduleFops", listScheduleFops);

		model.addAttribute("schedule", schedule);
		model.addAttribute("pageTitle", "View Schedule");
		LOGGER.info("Exit viewSchedule method -ManageScheduleController");

		return "administration/view_schedule";
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-schedule/schedule-days/assign/{id}")
	public String saveScheduleDays(@PathVariable(name = "id") Integer id,
			@RequestParam(name = "scheduleDayId", required = false) Integer scheduleDayId,
			@ModelAttribute("scheduleDays") ScheduleDays scheduleDays, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
		LOGGER.info("Entered saveScheduleDays method -ManageScheduleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			Schedule schedule = service.get(id);
			if(schedule.getId()!=null) {
				if(!schedule.getChampionship().getStatus().equals(ChampionshipStatusEnum.SCHEDULED)) {
				redirectAttributes.addFlashAttribute("errorMessage", "This Schedule cannot be edited beacause championship is ongoing/completed!");
				return "redirect:/admin/manage-schedule/edit/" + schedule.getId();
				}
			}
			
			List<ScheduleDays> listScheduleDays = schedule.getScheduleDays();
			if (scheduleDayId == null) {
				ScheduleDays ScheduleDaysNew = new ScheduleDays();

				ScheduleDaysNew.setDay(scheduleDays.getDay());
				ScheduleDaysNew.setDate(scheduleDays.getDate());
				ScheduleDaysNew.setSequenceNumber(scheduleDays.getSequenceNumber());
				listScheduleDays.add(ScheduleDaysNew);

				schedule.setScheduleDays(listScheduleDays);
				Schedule schedule1 = service.save(schedule);
			} else {
				ScheduleDays existingScheduleDays = scheduleDaysService.get(scheduleDayId);
				existingScheduleDays.setDate(scheduleDays.getDate());
				existingScheduleDays.setDay(scheduleDays.getDay());
				existingScheduleDays.setSequenceNumber(scheduleDays.getSequenceNumber());
				scheduleDaysService.save(existingScheduleDays);
			}
			LOGGER.info("Exit saveScheduleDays method -ManageScheduleController");

			return "redirect:/admin/manage-schedule/edit/" + schedule.getId();

		} catch (Exception e) {
			LOGGER.error("Schedule not found!" + e.getMessage());
			throw e;
		}

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-schedule/schedule-time-slots/assign/{id}")
	public String saveScheduleTimeSlots(@PathVariable(name = "id") Integer id,
			@RequestParam(name = "scheduleDays") Integer scheduleDaysId,
			@RequestParam(name = "scheduleTimeSlotId", required = false) Integer scheduleTimeSlotId,
			@ModelAttribute("scheduleTimeSlots") ScheduleTimeSlots scheduleTimeSlots, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
		LOGGER.info("Entered saveScheduleTimeSlots method -ManageScheduleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			Schedule schedule = service.get(id);
			if(schedule.getId()!=null) {
				if(!schedule.getChampionship().getStatus().equals(ChampionshipStatusEnum.SCHEDULED)) {
				redirectAttributes.addFlashAttribute("errorMessage", "This Schedule cannot be edited beacause championship is ongoing/completed!");
				return "redirect:/admin/manage-schedule/edit/" + schedule.getId();
				}
			}
			ScheduleDays scheduleDays = scheduleDaysService.get(scheduleDaysId);

			List<ScheduleTimeSlots> listScheduleTimeSlots = scheduleDays.getScheduleTimeSlots();

			if (scheduleTimeSlotId == null) {

				ScheduleTimeSlots ScheduleTimeSlotsNew = new ScheduleTimeSlots();
				ScheduleTimeSlotsNew.setScheduleDays(scheduleTimeSlots.getScheduleDays());
				ScheduleTimeSlotsNew.setTime_slot(scheduleTimeSlots.getTime_slot());
				ScheduleTimeSlotsNew.setStart_time(scheduleTimeSlots.getStart_time());
				ScheduleTimeSlotsNew.setEnd_time(scheduleTimeSlots.getEnd_time());
				ScheduleTimeSlotsNew.setSchedule(schedule);

				ScheduleTimeSlots scheduleTimeSlots1 = scheduleTimeSlotsService.save(ScheduleTimeSlotsNew);
			} else {

				ScheduleTimeSlots existingScheduleTimeSlots = scheduleTimeSlotsService.get(scheduleTimeSlotId);
				// existingScheduleTimeSlots.setScheduleDays(scheduleTimeSlots.getScheduleDays());
				existingScheduleTimeSlots.setTime_slot(scheduleTimeSlots.getTime_slot());
				existingScheduleTimeSlots.setStart_time(scheduleTimeSlots.getStart_time());
				existingScheduleTimeSlots.setEnd_time(scheduleTimeSlots.getEnd_time());
				existingScheduleTimeSlots.setSchedule(schedule);

				scheduleTimeSlotsService.save(existingScheduleTimeSlots);
			}
			LOGGER.info("Exit saveScheduleTimeSlots method -ManageScheduleController");

			return "redirect:/admin/manage-schedule/edit/" + schedule.getId();

		} catch (Exception e) {
			LOGGER.error("Schedule not found!" + e.getMessage());
			throw e;
		}

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@PostMapping("/manage-schedule/schedule-fop/assign/{id}")
	public String saveScheduleFop(@PathVariable(name = "id") Integer id,
			@RequestParam(name = "scheduleFopId", required = false) Integer scheduleFopId,
			@ModelAttribute("schedulefop") ScheduleFop scheduleFop, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws Exception {
		LOGGER.info("Entered saveScheduleFop method -ManageScheduleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			Schedule schedule = service.get(id);
			if(schedule.getId()!=null) {
				if(!schedule.getChampionship().getStatus().equals(ChampionshipStatusEnum.SCHEDULED)) {
				redirectAttributes.addFlashAttribute("errorMessage", "This Schedule cannot be edited beacause championship is ongoing/completed!");
				return "redirect:/admin/manage-schedule/edit/" + schedule.getId();
				}
			}

			if (scheduleFopId == null) {

				ScheduleFop ScheduleFopNew = new ScheduleFop();
				ScheduleFopNew.setScheduleDays(scheduleFop.getScheduleTimeSlots().getScheduleDays());
				ScheduleFopNew.setScheduleTimeSlots(scheduleFop.getScheduleTimeSlots());
				ScheduleFopNew.setFop(scheduleFop.getFop());
				ScheduleFopNew.setCategory_detail_id(scheduleFop.getCategory_detail_id());
				ScheduleFopNew.setCategory_name(scheduleFop.getCategory_name());
				ScheduleFopNew.setEvent_id(scheduleFop.getEvent_id());
				ScheduleFopNew.setEvent_description(scheduleFop.getEvent_description());
				ScheduleFopNew.setDescription(scheduleFop.getDescription());
				ScheduleFopNew.setSchedule(schedule);

				ScheduleFop ScheduleFop1 = scheduleFopService.save(ScheduleFopNew);
			} else {

				ScheduleFop existingScheduleFop = scheduleFopService.get(scheduleFopId);

				existingScheduleFop.setScheduleDays(scheduleFop.getScheduleTimeSlots().getScheduleDays());
				existingScheduleFop.setScheduleTimeSlots(scheduleFop.getScheduleTimeSlots());
				existingScheduleFop.setFop(scheduleFop.getFop());
				existingScheduleFop.setCategory_detail_id(scheduleFop.getCategory_detail_id());
				existingScheduleFop.setCategory_name(scheduleFop.getCategory_name());
				existingScheduleFop.setEvent_id(scheduleFop.getEvent_id());
				existingScheduleFop.setEvent_description(scheduleFop.getEvent_description());
				existingScheduleFop.setDescription(scheduleFop.getDescription());
				// existingScheduleFop.setSchedule(schedule);

				scheduleFopService.save(existingScheduleFop);

			}
			LOGGER.info("Exit saveScheduleFop method -ManageScheduleController");

			return "redirect:/admin/manage-schedule/edit/" + schedule.getId();

		} catch (Exception e) {
			LOGGER.error("Schedule not found!" + e.getMessage());
			throw e;
		}
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-schedule/{scheduleId}/schedule-days/delete/{scheduleDaysId}")
	public String deleteScheduleDay(@PathVariable(name = "scheduleId") Integer scheduleId,
			@PathVariable(name = "scheduleDaysId") Integer scheduleDaysId, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		LOGGER.info("Entered deleteScheduleDay method -ManageScheduleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		try {
			ScheduleDays scheduleDays = scheduleDaysService.get(scheduleDaysId);
			List<ScheduleTimeSlots> listScheduleTimeSlots = scheduleDays.getScheduleTimeSlots();
			if (listScheduleTimeSlots.size() == 0 || listScheduleTimeSlots.isEmpty()) {
				scheduleDaysService.delete(scheduleDaysId);
				redirectAttributes.addFlashAttribute("message", "Schedule Day removed Successfully");
				LOGGER.info("Redirected to manage-schedule/edit page");
			} else {
				redirectAttributes.addFlashAttribute("errorMessage",
						"Could not Delete the schedule day. Kindly delete the time slots associated with this day to proceed further.");
				LOGGER.error("Schedule day could not be deleted!");
			}

		} catch (EntityNotFoundException e1) {
			redirectAttributes.addFlashAttribute("errorMessage", "Schedule day could not be found!");
			LOGGER.error("Schedule day could not be found!");
		}
		LOGGER.info("Exit deleteScheduleDay method -ManageScheduleController");

		return "redirect:/admin/manage-schedule/edit/" + scheduleId;
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-schedule/{scheduleId}/schedule-time-slots/delete/{scheduleTimeSlotsId}")
	public String deleteScheduleTimeSlot(@PathVariable(name = "scheduleId") Integer scheduleId,
			@PathVariable(name = "scheduleTimeSlotsId") Integer scheduleTimeSlotsId, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		LOGGER.info("Entered deleteScheduleTimeSlot method -ManageScheduleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);

		try {
			ScheduleTimeSlots scheduleTimeSlots = scheduleTimeSlotsService.get(scheduleTimeSlotsId);
			List<ScheduleFop> listScheduleFops = scheduleTimeSlots.getScheduleFop();
			if (listScheduleFops.size() == 0 || listScheduleFops.isEmpty()) {
				scheduleTimeSlotsService.delete(scheduleTimeSlotsId);
				redirectAttributes.addFlashAttribute("message", "Schedule Time Slot removed Successfully");
				LOGGER.info("Redirected to manage-schedule/edit page");
			} else {
				redirectAttributes.addFlashAttribute("errorMessage",
						"Could not Delete the time slot. Kindly delete the FOPs associated with this time slot to proceed further.");
				LOGGER.error("Schedule time slot could not be deleted!");
			}

		} catch (EntityNotFoundException e1) {
			redirectAttributes.addFlashAttribute("errorMessage", "Schedule Time Slot could not be found!");
			LOGGER.error("Schedule time slot could not be found!");
		}
		LOGGER.info("Exit deleteScheduleTimeSlot method -ManageScheduleController");

		return "redirect:/admin/manage-schedule/edit/" + scheduleId;
	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-schedule/{scheduleId}/schedule-fop/delete/{scheduleFopId}")
	public String deleteScheduleFop(@PathVariable(name = "scheduleId") Integer scheduleId,
			@PathVariable(name = "scheduleFopId") Integer scheduleFopId, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		LOGGER.info("Entered deleteScheduleFop method -ManageScheduleController");
		// For Navigation history
		String mappedUrl = request.getRequestURI();
		User currentUser = CommonUtil.getCurrentUser();
		CommonUtil.setNavigationHistory(mappedUrl, currentUser);
		try {
			scheduleFopService.delete(scheduleFopId);
		} catch (EntityNotFoundException e) {
			LOGGER.error("Schedule FOP could not be found!" + e.getMessage());
		}
		redirectAttributes.addFlashAttribute("message", "Schedule FOP removed Successfully");
		LOGGER.info("Exit deleteScheduleFop method -ManageScheduleController");
		return "redirect:/admin/manage-schedule/edit/" + scheduleId;

	}

	@PreAuthorize("hasAnyAuthority('Administrator','SubAdministrator')")
	@GetMapping("/manage-schedule/delete/{id}")
	public String deleteSchedule(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		LOGGER.info("Entered deleteSchedule method -ManageScheduleController");
		Boolean flag = false;
		try {
			Schedule schedule = service.get(id);
			List<ScheduleFop> ListScheduleFop = scheduleFopService.getScheduleFopForSchedule(schedule);
			if (ListScheduleFop != null) {
				for (ScheduleFop scheduleFop : ListScheduleFop) {

					scheduleFopService.delete(scheduleFop.getId());

				}
			}
			
			List<ScheduleTimeSlots> listScheduleTimeSlots = scheduleTimeSlotsService
					.getTimeSlotsForSchedule(schedule);
			if (listScheduleTimeSlots != null) {
				for (ScheduleTimeSlots scheduleTimeSlots : listScheduleTimeSlots) {

					scheduleTimeSlotsService.delete(scheduleTimeSlots.getId());

				}
			}
			List<ScheduleDays> listScheduleDays = scheduleDaysService.getScheduleDaysBySchedule(schedule.getId());
			if (listScheduleDays != null) {
				for (ScheduleDays scheduleTimeSlots : listScheduleDays) {

					scheduleDaysService.delete(scheduleTimeSlots.getId());

				}
			}	

			// if (flag == true) {
			// redirectAttributes.addFlashAttribute("message1",
			// "The role " + role.getName() + " cannot be deleted. It has been assigned to
			// the user");
			// } else {
			service.delete(id);
			redirectAttributes.addFlashAttribute("message",
					"The Schedule " + schedule.getSport_name() + " has been deleted successfully");
			// }

		} catch (EntityNotFoundException ex) {
			LOGGER.error("Schedule with id" + id + "not found" + ex.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", "Schedule with id" + id + "not found");
		}
		LOGGER.info("Exit deleteSchedule method -ManageScheduleController");

		return "redirect:/admin/manage-schedule";
	}

}
