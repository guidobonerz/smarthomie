/*
(function poll() {
	$.ajax({
		url : "http://localhost:50080/homeautomation/getSmartEvents",
		success : function(data) {
			// Update your dashboard gauge
			console.log(data.data[0].peerId+' - '+data.data[0].parameterName+' - '+data.data[0].value);
		},
		dataType : "json",
		complete : poll,
		timeout : 30000
	});
})();
 */
(function poll() {
	setTimeout(function() {
		$.ajax({
			url : "http://10.100.200.205:50080/homeautomation/getSmartEvents",
			success : function(data) {
				// Update your dashboard gauge
				if (data.data.length > 0) {
					for (i = 0; i < data.data.length; i++) {
						console.log(data.data[i].peerId + ' - '
								+ data.data[i].parameterName + ' - '
								+ data.data[i].value);
					}
				}
				// Setup the next poll recursively
				poll();
			},
			dataType : "json"
		});
	}, 2000);
})();