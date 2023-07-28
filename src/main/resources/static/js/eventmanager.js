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
	for (var i = 0; i < 2 && i < arr.length; i++) {
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
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();
}

function showErrorModal(message) {
	showModalDialog("Error", message);
}

function showWarningModal(message) {
	showModalDialog("Warning", message);
}

var filter_main = document.getElementById('filter-accordian');
function filter() {
	filter_main.classList.toggle('d-none');
}

function clearFilter() {
			window.location = moduleURL;
		}
	
	
