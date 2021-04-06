var app = angular.module("Homeautomation", []);

var sse = new EventSource('http://10.100.200.4:50080/homeautomation/sse');
sse.onmessage = function (evt) {

	console.log(evt.data);

};

/*
app.factory('Poller', function ($http, $timeout) {
	var pollerData = {
		response: {},
		stop: false
	};

	var isChannelLive = function () {
		$http.get('http://localhost:50080/homeautomation/getMessages').then(
			function (response) {
				$timeout(isChannelLive, 2000);
				pollerData.response = response.data;
				if (response.data.data.length > 0) {
					console.log(response.data.data[0].payload);
				}
			});
	};
	var init = function () {
		pollerData.stop = false;
		isChannelLive();
	};
	var stop = function () {

		pollerData.stop = true;
	};

	return {
		pollerData: pollerData, // this should be private
		init: init,
		stop: stop
	};
});
*/
app.controller("GridController", function ($scope, $http) {
	//Poller.init();
	$http({
		method: "GET",
		url: '/homeautomation/getRemoteWallThermostatList'
	}).then(function mySuccess(response) {
		$scope.remoteWallThermostatList = response.data.data;
	}, function myError(response) {
		// $scope.myWelcome = response.statusText;
	});

	$http({
		method: "GET",
		url: '/homeautomation/getRemoteRadiatorThermostatList'
	}).then(function mySuccess(response) {
		$scope.remoteRadiatorThermostatList = response.data.data;
	}, function myError(response) {
		// $scope.myWelcome = response.statusText;
	});

	$http({
		method: "GET",
		url: '/homeautomation/getRemoteOutdoorWeatherSensorList'
	}).then(function mySuccess(response) {
		$scope.remoteOutdoorWeatherSensorList = response.data.data;
	}, function myError(response) {
		// $scope.myWelcome = response.statusText;
	});

	$http({
		method: "GET",
		url: '/homeautomation/getRemoteValveDriveList'
	}).then(function mySuccess(response) {
		$scope.remoteValveDriveList = response.data.data;
	}, function myError(response) {
		// $scope.myWelcome = response.statusText;
	});

});

app.directive('tooltip', function () {
	return {
		restrict: 'A',
		link: function (scope, element, attrs) {
			$(element).hover(function () {
				$(element).tooltip('show');
			}, function () {
				$(element).tooltip('hide');
			});
		}
	};
});

app.filter("getBatteryStateIconIndex", function () {
	return function (item) {
		var batt;
		if (item.BatteryVoltage <= 2.2) {
			batt = 'empty';
		} else if (item.BatteryVoltage <= 2.4) {
			batt = 'quarter';
		} else if (item.BatteryVoltage <= 2.6) {
			batt = 'half';
		} else if (item.BatteryVoltage <= 2.8) {
			batt = 'three-quarters';
		} else {
			batt = 'full';
		}
		batt += item.LowBattery ? ' red' : '';
		return batt;
	}
});

app.filter("getDewPoint", function () {
	return function (item) {
		return getDewPoint(item.CurrentTemperature, item.Humidity);
	}
});

app.filter("getStickyness", function () {
	return function (item) {
		var dewPoint = getDewPoint(item.CurrentTemperature, item.Humidity);
		if (dewPoint < 16) {
			return 'far fa-smile';
		} else if (dewPoint >= 16) {
			return 'far fa-frown';
		}
	}
});

app.filter("getSignalStrengthIcon", function () {
	return function (item) {
		var signal;
		if (item.SignalStrength <= -80) {
			signal = 'icon-wifi-signal-low-2';
		} else if (item.SignalStrength <= -60) {
			signal = 'icon-wifi-signal-medium-2';
		} else {
			signal = 'icon-wifi-signal-full-2';
		}
		signal += item.Unreachable ? ' red' : '';
		return signal;
	}
});

app.filter("getVentilationRecommendationIcon", function () {
	return function (item) {
		return item ? 'green' : 'red';
	}
});

app.filter("getSliderPosition", function () {
	return function (item) {
		var pos = 50 + (6.25 * item.VentilationDiff) - 2;
		return pos == Number.NaN ? 0 : pos;
	}
});

/* {{item|getLowBatteryColor}} */
app.filter("getLowBatteryColor", function () {
	return function (item) {
		return item.LowBattery ? 'red' : '';
	}
});
/* {{item|getUnreachableColor}} */
app.filter("getUnreachableColor", function () {
	return function (item) {
		return item.Unreachable ? 'red' : '';
	}
});

app.filter("getControlModeIcon", function () {
	return function (item) {
		var iconName = 'far fa-star';
		if (item === 'manual') {
			iconName = 'far fa-hand-paper';
		} else if (item === 'automatic') {
			iconName = 'far fa-clock';
		}
		return iconName;
	}
});

app.directive('linearGraph', function ($rootscope) {
	return function (scope, element, attr) {
		attr.$observe('value')
	};
});
