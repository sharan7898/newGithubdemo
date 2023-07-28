(function($) {
    "use strict";

    // Add active state to sidbar nav links
    var path = window.location.href; // because the 'href' property of the DOM element is the absolute path
        $("#layoutSidenav_nav .sb-sidenav a.nav-link").each(function() {
            if (this.href === path) {
                $(this).addClass("active");
            }
        });
        

    // Toggle the side navigation
    $("#sidebarToggle").on("click", function(e) {
        e.preventDefault();
        $("body").toggleClass("sb-sidenav-toggled");
    });
    
})(jQuery);


$(document).ready(function() {
	var pathname = window.location.pathname;
	var arr = pathname.split("/");
	var parentpath = "";
	for (var i = 0; i < 3 && i < arr.length; i++) {
		parentpath += "/" + arr[i + 1];
	}
	var activeLink = document.querySelector('a.nav-link[href="' + parentpath + '"]');
	activeLink.classList.add("active");

});


$(document).ready(function() {
	$("#logoutLink").on("click", function(e) {
		e.preventDefault();
		document.logoutForm.submit();
	});
 
});

function showModalDialog(title, message) {
	//$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();
}

function showModalDialogForEmail(title, message) {
//	$("#modalTitle").text(title);
	$("#modalBody").text("This Email Id is already taken by another user.");
	$("#modalDialog").modal();
}



function showErrorModal(message) {
	showModalDialog("Error", message);
}

function showSuccessModal(message) {
	showModalDialog("Success", message);
}

function showWarningModal(message) {
	showModalDialog("Warning", message);
}

function showEmailWarningModal(message) {
	showModalDialogForEmail("Email Warning", message);
}

var filter_main = document.getElementById('filter-accordian');
function filter() {
	filter_main.classList.toggle('d-none');
}

function clearFilter() {
			window.location = moduleURL;
		}
	
$(document).ready(
	function() {
		getNotifications();
	});

function getNotifications() {
	url = base_url + "admin/notification";
	$.get(url, function(result) {
	console.log(result);
		if (result) {
			var count = result.notificationCount;
			
$("#totalNotificationCount").text(count);
}
})
}


$(document).ready(
	function() {
		getNotificationsForEventManager();
	});

function getNotificationsForEventManager() {
	url = base_url + "event-manager/notification";
	$.get(url, function(result) {
	console.log(result);
		if (result) {
			var count = result.notificationCount;
			
$("#totalNotificationCountForEventManager").text(count);
}
})
}