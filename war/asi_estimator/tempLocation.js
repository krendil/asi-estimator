function getCoords() {

	if( navigator.geolocation ) {
		
		navigator.geolocation.getCurrentPosition ( 
			
			// successful callback...
			function (position) {
				document.getElementById("long").value = position.coords.longitude;
				document.getElementById("lat").value = position.coords.latitude;
			}, 
			
			// next function is the error callback
			function (error)
			{
				switch(error.code) 
				{
					case error.TIMEOUT:
						alert ('Timeout');
						break;
					case error.POSITION_UNAVAILABLE:
						alert ('Position unavailable');
						break;
					case error.PERMISSION_DENIED:
						alert ('Permission denied');
						break;
					case error.UNKNOWN_ERROR:
						alert ('Unknown error');
						break;
				}
			}
			);
	}
  }