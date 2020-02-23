
$(document).ready(function(){
		
	var spinnerOptions = {
			  lines: 10, // The number of lines to draw
			  length: 22, // The length of each line
			  width: 29, // The line thickness
			  radius: 77, // The radius of the inner circle
			  scale: 0.35, // Scales overall size of the spinner
			  corners: 1, // Corner roundness (0..1)
			  color: '#eab02b', // CSS color or array of colors
			  fadeColor: 'transparent', // CSS color or array of colors
			  speed: 0.8, // Rounds per second
			  rotate: 36, // The rotation offset
			  animation: 'spinner-line-fade-more', // The CSS animation
													// name for the lines
			  direction: 1, // 1: clockwise, -1: counterclockwise
			  zIndex: 2e9, // The z-index (defaults to 2000000000)
			  className: 'spinner', // The CSS class to assign to the
									// spinner
			  top: '45%', // Top position relative to parent
			  left: '52%', // Left position relative to parent
			  shadow: '0 0 1px transparent', // Box-shadow for the lines
			  position: 'absolute' // Element positioning
			};
	
	const { pipelines } = window.mediaStreamLibrary

	// force auth
	const authorize = async host => {
	  // Force a login by fetching usergroup
	  const fetchOptions = {
	    credentials: 'include',
	    headers: {
	      'Axis-Orig-Sw': true,
	      'X-Requested-With': 'XMLHttpRequest',
	    },
	    mode: 'no-cors',
	  }
	  try {
	    await window.fetch(`http://${host}/axis-cgi/usergroup.cgi`, fetchOptions);
	  } catch (err) {
	    console.error(err);
	  }
	}

	const play = (host, encoding, videoId) => {
	   
	  let Pipeline = pipelines.Html5VideoPipeline;
	  let mediaElement = document.getElementById(videoId);
	 
	  var target = document.getElementById('videoContainer');
	  var spinner = new Spinner(spinnerOptions).spin(target);
	  
	  // Setup a new pipeline
	  const pipeline = new Pipeline({
	     ws: { uri: `ws://${host}/rtsp-over-websocket` },
	    rtsp: { uri: `rtsp://${host}/axis-media/media.amp?videocodec=${encoding}` },
	    mediaElement,
	  })
	  pipeline.ready.then(() => {
		  pipeline.rtsp.play();
		  window.setInterval(e=>{spinner.stop();},2000);
	  })

	  return pipeline;
	}

	let pipeline;
	let cameraId;
	
		
		  
	$('#collapse1').on('hide.bs.collapse', async e=> { 
		pipeline && pipeline.rtsp.stop(); 
	});
		 
	$('#showEntryVideo').click( async e=> {
		cameraId='entry';
		pipeline && pipeline.close();
		await authorize('10.100.200.171');
		pipeline = play('10.100.200.171','h264','video');
	});
	
	$('#showCorridorVideo').click( async e=> {
		cameraId='corridor';
		pipeline && pipeline.close();
		await authorize('10.100.200.170');
		pipeline = play('10.100.200.170','h264','video');
	});
	
	$('#makeSnapshot').on('click', e=> {
		makeScreenshot(cameraId);
	});
	
	
	
	const makeScreenshot = (cameraId) =>
	{   
		$.get({
			url: '/homeautomation/snapshot',
			data:{deviceName:cameraId},
			success: function ( data ) {
				// window.setInterval(e=>{spinner.stop();},1000);
				// $('#message').toast('show');
		    }
		});
	}
});