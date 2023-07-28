
$(document).ready(function () {
   	$("#confirmPassword").keyup(checkPasswordMatch);
   	
   	$(document).on("change", "#participantImage", function() {
	console.log("entered on change");
		if (!checkFileSize(this)) {
		console.log("file size error");
			$('#span-error-participant-image').text(
				"Please choose an image less than " + MAX_FILE_SIZE + "Bytes");
			return;
		}
	
	});

	$(document).on("change", "#birthCertificateFile", function() {
		if (!checkFileSize(this)) {
			$('#span-error-birth-certificate').text(
				"Please choose an image less than " + MAX_FILE_SIZE + "Bytes");

			return;
		}
		
	});

	$(document).on("change", "#medicalCertificateFile", function() {
		if (!checkFileSize(this)) {
			$("#span-error-medical-certificate").text(
				"Please choose an image less than " + MAX_FILE_SIZE + "Bytes");

			return;
		}
			});

	$(document).on("change", "#paymentReceiptFile", function() {
		if (!checkFileSize(this)) {
			$('#span-error-payment-reciept').text(
				"Please choose an image less than " + MAX_FILE_SIZE + "Bytes");

			return;
		}
		
	});

 	
});

// Toggle password visibility
const togglePassword = document.querySelector('#togglePassword');
const password = document.querySelector('#password');
togglePassword.addEventListener('click', function (e) {
    const passwordType = password.getAttribute('type') === 'password' ? 'text' : 'password';
    password.setAttribute('type', passwordType);
    // toggle the eye slash icon
    this.classList.toggle('fa-eye-slash');
});

// Toggle confirm password visibility
const toggleConfirmPassword = document.querySelector('#toggleConfirmPassword');
const confirmPassword = document.querySelector('#confirmPassword');
toggleConfirmPassword.addEventListener('click', function (e) {
    const confirmPasswordtype = confirmPassword.getAttribute('type') === 'password' ? 'text' : 'password';
    confirmPassword.setAttribute('type', confirmPasswordtype);
    // toggle the eye slash icon
    this.classList.toggle('fa-eye-slash');
});
		
		
function checkPasswordMatch() {
    var password = $("#password").val();
    var confirmPassword = $("#confirmPassword").val();
    if (password != confirmPassword){
        $("#divCheckPasswordMatch").html("<span class='red-font'>Passwords do not match!<span>");
        $("#span-error-confirm-password").text(
							"");
  }  else{
        $("#divCheckPasswordMatch").html("<span class='green-font'>Passwords match.<span>");
        $("#span-error-confirm-password").text(
							"");
}

}
	


function checkFileSize(fileInput) {
	fileSize = fileInput.files[0].size;
	if (fileSize > MAX_FILE_SIZE) {
		//	fileInput.setCustomValidity("You must choose an images less than " + MAX_FILE_SIZE + "Bytes");
		//	fileInput.reportValidity();
		return false;
	} else {
		fileInput.setCustomValidity("");
		return true;
	}
}


