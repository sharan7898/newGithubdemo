var base_url = "[[@{/}]]";
		
function getDistrict(input) {
	
	var stateId = input.value;
	if(stateId==''){
		$('#district').empty();
				$('#district')
					.append(
					'<option value="">Select</option>'
					);
	}else{

	var url = base_url + "district"
		+ "/list_by_states/" + stateId;

	$
		.get(
			url,

			function(responseJson) {

				console.log(responseJson);

				$('#district').empty();
				$('#district')
					.append(
					'<option value="">Select</option>'
					);

				$.each(responseJson, function(index, district) {
					
		$('#district').append(
						'<option value="' + district.id + '">'
						+ district.name
						+ '</option>');

				});


			}).fail(function() {
				showErrorModal("Error loading district");
			})
			
			}

}




