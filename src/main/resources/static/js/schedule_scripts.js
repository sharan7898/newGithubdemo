
var ddScheduleDay;
$(document).ready(function () {
			
			ddScheduleDay = $("#scheduleDaysFop");
			ddScheduleDay.on("change", function() {
				getTimeSlotsForSelectedDay();
			});	
			
			
			// Add Schedule days operation
			$("#scheduleDaysSection").on("click", ".btnAddScheduleDay", function (e) {
				e.preventDefault();
				$('#date').val('');
				$('#day').val('');
				$('#sequenceNumber').val('');
				$('#scheduleDayId').val('');
				$("#span-error-scheduleDay").text("");
				$("#span-error-scheduleDate").text("");
				$('#scheduleDayModalLabel').text("Add Schedule Day");
			});
		
			// Edit Schedule days operation
			$("#daysTable").on("click", ".editlink", function (event) {
				event.preventDefault();
				var href = $(this).attr('href');
				var text = $(this).text();
				var type = $(this).attr('type');
				// clear all data
				$('#date').val('');
				$('#day').val('');
				$('#sequenceNumber').val('');
				$('#scheduleDayId').val('');
				$("#span-error-scheduleDay").text("");
				$("#span-error-scheduleDate").text("");
				if (type == "edit") {
					$.get(href, function (scheduleDayDTO, status) {
						$('#day').val(scheduleDayDTO.day);
						$('#date').val(convertDate(new Date(scheduleDayDTO.date).toLocaleString()));
						$('#sequenceNumber').val(scheduleDayDTO.sequenceNumber);
						$('#scheduleDayId').val(scheduleDayDTO.id);
						$('#scheduleDayModalLabel').text("Edit Schedule Day");
					});
					$('#addScheduleDaysModalDialog').modal('show');
				}
			});
			
			
			// Add Schedule time slots operation
			$("#scheduleTimeSlotSection").on("click", ".btnAddScheduleTimeSlots", function (e) {
				e.preventDefault();
				$('#scheduleDays').val('');
				$('#scheduleDays').removeAttr('disabled');
				$('#scheduleDaysHidden').val('');
				$('#scheduleDaysHidden').attr('disabled','disabled');
				$('#time_slot').val('');
				$('#start_time').val('');
				$('#end_time').val('');
				$('#scheduleTimeSlotId').val('');
				
				$(".span-error").text("");
				
				/*$("#span-error-scheduleDays").text("");
				$("#span-error-time-slot").text("");
				$("#span-error-start-time").text("");
				$("#span-error-end-time").text("");*/
				$('#scheduleTimeSlotsModalLabel').text("Add Schedule Time Slots");
			});
			
			
			// Edit Schedule time slots operation
			$("#timeSlotsTable").on("click", ".editSlotLink", function (event) {
				event.preventDefault();
				var href = $(this).attr('href');
				var text = $(this).text();
				var type = $(this).attr('type');
				// clear all data
				$('#scheduleDays').val('');
				$('#scheduleDays').removeAttr('disabled');
				$('#scheduleDaysHidden').val('');
				$('#scheduleDaysHidden').attr('disabled','disabled');
				$('#time_slot').val('');
				$('#start_time').val('');
				$('#end_time').val('');
				$('#scheduleTimeSlotId').val('');
				$(".span-error").text("");
				if (type == "edit") {
					$.get(href, function (scheduleTimeSlotsDTO, status) {
						$('#scheduleDays').val(scheduleTimeSlotsDTO.schedule_days_id);
						$('#scheduleDaysHidden').val(scheduleTimeSlotsDTO.schedule_days_id);
						$('#scheduleDaysHidden').removeAttr('disabled');
						$('#scheduleDays').attr('disabled','disabled');
						$('#time_slot').val(scheduleTimeSlotsDTO.time_slot);
						$('#start_time').val(scheduleTimeSlotsDTO.start_time);
						$('#end_time').val(scheduleTimeSlotsDTO.end_time);
						$('#scheduleTimeSlotId').val(scheduleTimeSlotsDTO.id);
						$('#scheduleTimeSlotsModalLabel').text("Edit Schedule Time Slots");
					});
					$('#addScheduleTimeSlotsModalDialog').modal('show');
				}
			});
			
			
			// Add schedule Fop operation
			$("#scheduleFopSection").on("click", ".btnAddScheduleFop", function (e) {
				e.preventDefault();
				//$('#scheduleDaysFop').val('');
				$('#scheduleTimeSlotsFop').val('');
				$('#fop').val('');
				$('#descriptionFop').val('');
				$('#category_name').val('');
				$('#category_detail_id').val('');
				$('#event_id').val('');
				$('#event_description').val('');
				$('#scheduleFopId').val('');
		
				/*$("#span-error-scheduleDay").text("");
				$("#span-error-scheduleDate").text("");
				$("#span-error-scheduleDate").text("");
				$("#span-error-scheduleDate").text("");
				$("#span-error-scheduleDate").text("");
				$("#span-error-scheduleDate").text("");
				$("#span-error-scheduleDate").text("");
				$("#span-error-scheduleDate").text("");
				$("#span-error-scheduleDate").text("");*/
				$(".span-error").text("");
				
				$('#scheduleFopModalLabel').text("Add Schedule FOP");
			});
			
			// Edit Schedule FOP operation
			$("#fopTable").on("click", ".editFopLink", function (event) {
				event.preventDefault();
				var href = $(this).attr('href');
				var text = $(this).text();
				var type = $(this).attr('type');
				// clear all data
				//$('#scheduleDaysFop').val('');
				$('#scheduleTimeSlotsFop').val('');
				$('#fop').val('');
				$('#descriptionFop').val('');
				$('#category_name').val('');
				$('#category_detail_id').val('');
				$('#event_id').val('');
				$('#event_description').val('');
				$('#scheduleFopId').val('');
				
				$(".span-error").text("");
				if (type == "edit") {
					$.get(href, function (scheduleFopDTO, status) {
						//$('#scheduleDaysFop').val(scheduleFopDTO.schedule_days_id);
						
						$('#scheduleTimeSlotsFop').val(scheduleFopDTO.schedule_time_slots_id);
						$('#fop').val(scheduleFopDTO.fop);
						$('#descriptionFop').val(scheduleFopDTO.description);
						$('#category_name').val(scheduleFopDTO.category_name);
						$('#category_detail_id').val(scheduleFopDTO.category_detail_id);
						$('#event_id').val(scheduleFopDTO.event_id);
						$('#event_description').val(scheduleFopDTO.event_description);
						$('#scheduleFopId').val('');
						$('#scheduleFopId').val(scheduleFopDTO.id);
						$('#scheduleFopModalLabel').text("Edit Schedule Time Slots");
					});
					$('#addScheduleFopModalDialog').modal('show');
					//getTimeSlotsForSelectedDay();
				}
			});
			
			
			
		});
		
		function convertDate(str) {
		  var date = new Date(str),
		  mnth = ("0" + (date.getMonth() + 1)).slice(-2),
		  day = ("0" + date.getDate()).slice(-2);
		  return [date.getFullYear(), mnth, day].join("-");
		}

		var regSportId = /^\(?([0-9]{0,6})\)?([0-9]{1,3})$/;

		function validateForm(form) {
        

			if ((document.getElementById('championship').value) == "") {
				var flagChampionship = false;
				$("#span-error-championship").text("Select the championship");
			} else {
				flagChampionship = true;
				$("#span-error-championship").text("");
			}

			if ((document.getElementById('sport_id').value) == "") {
				$("#span-error-sportid").text("Enter the sport id");
				var flagSportId = false;
			} else {
				$("#span-error-sportid").text("");
				flagSportId = true;
			}
			
			if (!regSportId.exec(document.getElementById('sport_id').value)) {
				$("#span-error-sportid").text("Enter the valid sport Id");
			var	flagSportId = false;
			}
			else {
				$("#span-error-sportid").text("");
				flagSportId = true;
			}

			if ((document.getElementById('sport_name').value) == "") {
				$("#span-error-sportname").text("Enter the sport name");
				var flagSportName = false;
			} else {
				$("#span-error-sportname").text("");
				flagSportName = true;
			}

			if ((document.getElementById('description').value) == "") {
				$("#span-error-description").text("Enter the description");
				var flagDescription = false;
			} else {

				$("#span-error-description").text("");
				flagDescription = true;
			}

			return (flagChampionship && flagSportId && flagSportName && flagDescription)
		}
		
		function validateScheduleDaysForm(form){
			if ((document.getElementById('day').value) == "") {
				var flagDay = false;
				$("#span-error-scheduleDay").text("Enter the Day");
			} else {
				flagDay = true;
				$("#span-error-scheduleDay").text("");
			}
			
			if ((document.getElementById('date').value) == "") {
				var flagDate = false;
				$("#span-error-scheduleDate").text("Enter the Date");
			} else {
				flagDate = true;
				$("#span-error-scheduleDate").text("");
			}
				if ((document.getElementById('sequenceNumber').value) == "") {
				var flagSequenceNumber = false;
				$("#span-error-sequenceNumber").text("Enter the Sequence Number");
			} else {
				flagSequenceNumber = true;
				$("#span-error-scheduleNumber").text("");
			}
			return (flagDay && flagDate && flagSequenceNumber)
		}
		
		function validateScheduleTimeSlotsForm(form){
			
			if ((document.getElementById('scheduleDays').value) == "") {
				var flagDay = false;
				$("#span-error-scheduleDays").text("Select the Day");
			} else {
				flagDay = true;
				$("#span-error-scheduleDays").text("");
			}
			
			if ((document.getElementById('time_slot').value) == "") {
				var flagTimeSlot = false;
				$("#span-error-time-slot").text("Enter the Time Slot");
			} else {
				flagTimeSlot = true;
				$("#span-error-time-slot").text("");
			}
			
			if ((document.getElementById('start_time').value) == "") {
				var flagStartTime = false;
				$("#span-error-start-time").text("Enter the Start Time");
			} else {
				flagStartTime = true;
				$("#span-error-start-time").text("");
			}
			
			if ((document.getElementById('end_time').value) == "") {
				var flagEndTime = false;
				$("#span-error-end-time").text("Enter the End Time");
			} else {
				flagEndTime = true;
				$("#span-error-end-time").text("");
			}
			return (flagDay && flagTimeSlot && flagStartTime && flagEndTime)
					
		}
				
		function validateScheduleFopForm(form){
			
			/*if ((document.getElementById('scheduleDaysFop').value) == "") {
				var flagDay = false;
				$("#span-error-scheduleDaysFop").text("Select the Day");
			} else {
				flagDay = true;
				$("#span-error-scheduleDaysFop").text("");
			}*/
			
			if ((document.getElementById('scheduleTimeSlotsFop').value) == "") {
				var flagTimeSlot = false;
				$("#span-error-scheduleTimeSlotsFop").text("Enter the Time Slot");
			} else {
				flagTimeSlot = true;
				$("#span-error-scheduleTimeSlotsFop").text("");
			}
			
			if ((document.getElementById('descriptionFop').value) == "") {
				var flagDesc = false;
				$("#span-error-desc").text("Enter the description");
			} else {
				flagDesc = true;
				$("#span-error-desc").text("");
			}
			
			if ((document.getElementById('fop').value) == "") {
				var flagFop = false;
				$("#span-error-fop").text("Select the Fop");
			} else {
				flagFop = true;
				$("#span-error-fop").text("");
			}
			
			if((document.getElementById('fop').value) == "Common" || (document.getElementById('fop').value) == ""){
				return (flagTimeSlot && flagFop && flagDesc)
			} else if ((document.getElementById('fop').value) != "Common") {
				if ((document.getElementById('category_detail_id').value) == "") {
					var flagCategoryDetail = false;
					$("#span-error-category-detail-id").text("Enter the Category detail ID");
				} else {
					flagCategoryDetail = true;
					$("#span-error-category-detail-id").text("");
				}
				
				if ((document.getElementById('category_name').value) == "") {
					var flagCategoryName = false;
					$("#span-error-category-name").text("Enter the Category Name");
				} else {
					flagCategoryName = true;
					$("#span-error-category-name").text("");
				}
				
				if ((document.getElementById('event_id').value) == "") {
					var flagEvent = false;
					$("#span-error-event-id").text("Enter the event id");
				} else {
					flagEvent = true;
					$("#span-error-event-id").text("");
				}
				
				if ((document.getElementById('event_description').value) == "") {
					var flagEventDesc = false;
					$("#span-error-event-desc").text("Enter the event Description");
				} else {
					flagEventDesc = true;
					$("#span-error-event-desc").text("");
				}
				
				return (flagTimeSlot && flagFop && flagDesc && flagCategoryDetail && flagCategoryName && flagEvent && flagEventDesc)
			} 
			
		}
		
		
		function getTimeSlotsForSelectedDay() {
			selectedScheduleDay = $("#scheduleDaysFop option:selected");
			scheduleDayId = selectedScheduleDay.val();
			if(scheduleDayId != ""){
				url = base_url + "scheduleDays/" + scheduleDayId + "/getTimeSlots";
				$.get(url,
						function(responseJson) {
							console.log(responseJson);
							$('#scheduleTimeSlotsFop').empty();
							$('#scheduleTimeSlotsFop').append(
									'<option value="">-- Select --</option>');

							$.each(responseJson,
									function(index, timeSlot) {

										$('#scheduleTimeSlotsFop').append(
												'<option value="' + timeSlot.id + '">'
														+ timeSlot.time_slot + ' - (' + timeSlot.start_time + ' to ' + timeSlot.end_time + ')'
														+ '</option>');

									});
						}).fail(function() {
					showErrorModal("Error loading Time Slots.");
				})
			}
			
		}