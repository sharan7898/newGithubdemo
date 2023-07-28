var base_url = "[[@{/}]]";
var MAX_FILE_SIZE = 1000000; // 1MB
$(document).ready(function() {

	$(document).on("change", "#scoreBoardImage1", function() {
		if (!checkFileSize(this)) {
			$('#span-error-image1').text(
				"Please choose an image less than " + MAX_FILE_SIZE + "Bytes");
			return;
		}
		console.log(this);
		showImage1Thumbnail(this);
	});

	$(document).on("change", "#scoreBoardImage2", function() {
		if (!checkFileSize(this)) {
			$('#span-error-image2').text(
				"Please choose an image less than " + MAX_FILE_SIZE + "Bytes");
			return;
		}
		console.log(this);
		showImage2Thumbnail(this);
	});

	$(document).on("change", "#scoreBoardImage3", function() {
		if (!checkFileSize(this)) {
			$('#span-error-image3').text(
				"Please choose an image less than " + MAX_FILE_SIZE + "Bytes");
			return;
		}
		console.log(this);
		showImage3Thumbnail(this);
	});
	$(document).on("change", "#scoreBoardImage4", function() {
		if (!checkFileSize(this)) {
			$('#span-error-image4').text(
				"Please choose an image less than " + MAX_FILE_SIZE + "Bytes");
			return;
		}
		console.log(this);
		showImage4Thumbnail(this);
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



function showImage1Thumbnail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail1").attr("src", e.target.result);
		$("#thumbnail1Preview").attr("src", e.target.result);
		var elethumbnail1Preview = $('#thumbnail1Preview');
		if (elethumbnail1Preview.length == 0) {
			$(".thumbnail1Div").prepend(
				"<th:block th:unless='${#strings.contains(championship.ImagePath1,"
				+ "'null'" +
				")}'>" +
				"<img id='thumbnail1Preview' alt='Image1' class='img-fluid'	style='width: 80px; height: 50px;'/></th: block >"
			);
			$("#thumbnail1Preview").attr("src", e.target.result);

		} else {
			$("#thumbnail1Preview").attr("src", e.target.result);
		}
	};
	reader.readAsDataURL(file);
}

function showImage2Thumbnail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail2").attr("src", e.target.result);
		$("#thumbnail2Preview").attr("src", e.target.result);
		var elethumbnail2Preview = $('#thumbnail2Preview');
		if (elethumbnail2Preview.length == 0) {
			$(".thumbnail2Div").append(
				"<th:block th:unless='${#strings.contains(championship.ImagePath2,"
				+ "'null'" +
				")}'>" +
				"<img id='thumbnail2Preview' alt='Image3' class='img-fluid'	style='width: 80px; height: 50px;'/></th: block >"
			);
			$("#thumbnail2Preview").attr("src", e.target.result);

		} else {
			$("#thumbnail2Preview").attr("src", e.target.result);
		}
	};
	reader.readAsDataURL(file);
}

function showImage3Thumbnail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail3").attr("src", e.target.result);
		$("#thumbnail3Preview").attr("src", e.target.result);
		var elethumbnail3Preview = $('#thumbnail3Preview');
		if (elethumbnail3Preview.length == 0) {
			$(".thumbnail1Div").append(
				"<th:block th:unless='${#strings.contains(championship.ImagePath3,"
				+ "'null'" +
				")}'>" +
				"<img id='thumbnail3Preview' alt='Image3' class='img-fluid'	style='width: 80px; height: 50px;'/></th: block >"
			);
			$("#thumbnail3Preview").attr("src", e.target.result);

		} else {
			$("#thumbnail3Preview").attr("src", e.target.result);
		}
	};
	reader.readAsDataURL(file);
}

function showImage4Thumbnail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail4").attr("src", e.target.result);
		$("#thumbnail4Preview").attr("src", e.target.result);
		var elethumbnail4Preview = $('#thumbnail4Preview');
		if (elethumbnail4Preview.length == 0) {
			$(".thumbnail2Div").prepend(
				"<th:block th:unless='${#strings.contains(championship.ImagePath4,"
				+ "'null'" +
				")}'>" +
				"<img id='thumbnail4Preview' alt='Image4' class='img-fluid'	style='width: 80px; height: 50px;'/></th: block >"
			);
			$("#thumbnail4Preview").attr("src", e.target.result);

		} else {
			$("#thumbnail4Preview").attr("src", e.target.result);
		}
	};
	reader.readAsDataURL(file);
}


