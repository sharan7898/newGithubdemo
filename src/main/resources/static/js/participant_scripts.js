$(document).ready(function() {
	//	$("#buttonCancel").on("click", function() {
	//		window.location = moduleURL;



	$(document).on("change", "#participantImage", function() {
		if (!checkFileSize(this)) {
			$('#span-error-participant-image').text(
				"Please choose an image less than " + MAX_FILE_SIZE + "Bytes");
			return;
		}
		console.log(this);
		showParticipantImageThumbnail(this);
	});

	$(document).on("change", "#birthCertificateFile", function() {
		if (!checkFileSize(this)) {
			$('#span-error-birth-certificate').text(
				"Please choose an image less than " + MAX_FILE_SIZE + "Bytes");

			return;
		}
		console.log(this);
		showBirthCertificateFileImageThumbnail(this);
	});

	$(document).on("change", "#medicalCertificateFile", function() {
		if (!checkFileSize(this)) {
			$("#span-error-medical-certificate").text(
				"Please choose an image less than " + MAX_FILE_SIZE + "Bytes");

			return;
		}
		console.log(this);
		showMedicalCertificateFileImageThumbnail(this);
	});

	$(document).on("change", "#paymentReceiptFile", function() {
		if (!checkFileSize(this)) {
			$('#span-error-payment-reciept').text(
				"Please choose an image less than " + MAX_FILE_SIZE + "Bytes");

			return;
		}
		console.log(this);
		showPaymentReceiptFileImageThumbnail(this);
	});

});





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



function showParticipantImageThumbnail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#participantImageThumbnail").attr("src", e.target.result);
	};
	reader.readAsDataURL(file);
}

function showBirthCertificateFileImageThumbnail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#birthCertificateFileThumbnail").attr("src", e.target.result);
	};
	reader.readAsDataURL(file);
}

function showMedicalCertificateFileImageThumbnail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#medicalFitnessCertificateThumbnail").attr("src", e.target.result);
	};
	reader.readAsDataURL(file);
}

function showPaymentReceiptFileImageThumbnail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#paymentReceiptFileThumbnail").attr("src", e.target.result);
	};
	reader.readAsDataURL(file);
}
