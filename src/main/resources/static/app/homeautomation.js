var app = angular.module("Homeautomation", []);

app.controller("RemoteWallThermostatController", function($scope, $http) {

	$http({
		method : "GET",
		url : '/homeautomation/getRemoteWallThermostatList'
	}).then(function mySuccess(response) {
		$scope.remoteWallThermostatList = response.data.data;
	}, function myError(response) {
		// $scope.myWelcome = response.statusText;
	});
});

app.controller("RemoteRadiatorThermostatController", function($scope, $http) {

	$http({
		method : "GET",
		url : '/homeautomation/getRemoteRadiatorThermostatList'
	}).then(function mySuccess(response) {
		$scope.remoteRadiatorThermostatList = response.data.data;
	}, function myError(response) {
		// $scope.myWelcome = response.statusText;
	});
});

app.controller("RemoteOutdoorWeatherSensorController", function($scope, $http) {

	$http({
		method : "GET",
		url : '/homeautomation/getRemoteOutdoorWeatherSensorList'
	}).then(function mySuccess(response) {
		$scope.remoteOutdoorWeatherSensorList = response.data.data;
	}, function myError(response) {
		// $scope.myWelcome = response.statusText;
	});
});

app.controller("RemoteValveDriveController", function($scope, $http) {

	$http({
		method : "GET",
		url : '/homeautomation/getRemoteValveDriveList'
	}).then(function mySuccess(response) {
		$scope.remoteValveDriveList = response.data.data;
	}, function myError(response) {
		// $scope.myWelcome = response.statusText;
	});
});

app.directive('tooltip', function() {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			$(element).hover(function() {
				$(element).tooltip('show');
			}, function() {
				$(element).tooltip('hide');
			});
		}
	};
});

app.filter("getBatteryStateIconIndex", function() {
	return function(item) {
		if (item.BatteryVoltage <= 2.2) {
			return 0;
		} else if (item.BatteryVoltage <= 2.4) {
			return 1;
		} else if (item.BatteryVoltage <= 2.6) {
			return 2;
		} else if (item.BatteryVoltage <= 2.8) {
			return 3;
		} else {
			return 4;
		}
	}
});

app.filter("getDewPoint", function() {
	return function(item) {
		return getDewPoint(item.CurrentTemperature, item.Humidity);
	}
});

app.filter("getStickyness", function() {
	return function(item) {
		var dewPoint = getDewPoint(item.CurrentTemperature, item.Humidity);
		if (dewPoint < 16) {
			return "fa fa-sort-desc";
		} else if (dewPoint >= 16) {
			return "fa fa-sort-up";
		}
	}
});

app.filter("getSignalStrengthIcon", function() {
	return function(item) {
		if (item.SignalStrength <= -80) {
			return "icon-wifi-signal-low-2";
		} else if (item.SignalStrength <= -60) {
			return "icon-wifi-signal-medium-2";
		} else {
			return "icon-wifi-signal-full-2";
		}
	}
});

app.filter("getVentilationRecommendationIcon", function() {
	return function(item) {
		return item ? "green" : "red";
	}
});

app.filter("getSliderPosition", function() {
	return function(item) {
		var pos = 50 + (6.25 * item.VentilationDiff) - 2;
		return pos == Number.NaN ? 0 : pos;
	}
});

app.filter("getLowBatteryColor", function() {
	return function(item) {
		return item.LowBattery ? "red" : "";
	}
});

app.filter("getUnreachableColor", function() {
	return function(item) {
		return item.Unreachable ? "red" : "";
	}
});

app.filter("getControlModeIcon", function() {
	return function(item) {
		var iconName = "fa fa-star-o";
		if (item === "manual") {
			iconName = "fa fa-hand-paper-o";
		} else if (item === "automatic") {
			iconName = "fa fa-clock-o";
		}
		return iconName;
	}
});

app.directive('linearGraph', function($rootscope) {
	return function(scope, element, attr) {
		attr.$observe('value')
	};
});
