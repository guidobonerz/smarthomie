var req = new Pollymer.Request({
	recurring : true,
	maxDelay : 5000
});
req
		.on(
				'finished',
				function(code, result, headers) {
					if (result != null) {
						var scope = angular
								.element(
										$('[ng-controller=RemoteWallThermostatController]'))
								.scope();

						for (i = 0; i < result.length; i++) {
							if (scope.remoteWallThermostatList != null) {
								for (j = 0; j < scope.remoteWallThermostatList.length; j++) {
									if (scope.remoteWallThermostatList[j].SerialNo === result[i].SerialNo) {
										value = result[i].Value;
										switch (result[i].Attribute) {
										case 'CONTROL_MODE': {
											scope.remoteWallThermostatList[j].ControlMode = (value == 0 ? 'automatic'
													: 'manual');
											break;
										}
										case 'ACTUAL_HUMIDITY': {
											scope.remoteWallThermostatList[j].Humidity = value;
											break;
										}
										case 'ACTUAL_TEMPERATURE': {
											scope.remoteWallThermostatList[j].CurrentTemperature = value;
											break;
										}
										}
									}
								}
							}
						}
						scope.$apply();
						console.log(result);
					}
				});
req.on('error', function(reason) {
});
var headers = {};
var body = 'some data';
// req.maxTries = 2;
// req.start('POST', '/homeautomation/poll', headers, body);

var app = angular.module("Homeautomation", []);

app.controller("RemoteWallThermostatController", function($scope, $http) {
	$http.get('/homeautomation/getRemoteWallThermostatList').success(
			function(data, status, headers, config) {
				$scope.remoteWallThermostatList = data.data;
			}).error(function(data, status, headers, config) {
		// log error
	});
});

app.controller("RemoteRadiatorThermostatController", function($scope, $http) {
	$http.get('/homeautomation/getRemoteRadiatorThermostatList').success(
			function(data, status, headers, config) {
				$scope.remoteRadiatorThermostatList = data.data;
			}).error(function(data, status, headers, config) {
		// log error
	});
});

app.controller("RemoteOutdoorWeatherSensorController", function($scope, $http) {
	$http.get('/homeautomation/getRemoteOutdoorWeatherSensorList').success(
			function(data, status, headers, config) {
				$scope.remoteOutdoorWeatherSensorList = data.data;
			}).error(function(data, status, headers, config) {
		// log error
	});
});

app.controller("RemoteValveDriveController", function($scope, $http) {
	$http.get('/homeautomation/getRemoteValveDriveList').success(
			function(data, status, headers, config) {
				$scope.remoteValveDriveList = data.data;
			}).error(function(data, status, headers, config) {
		// log error
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

/*
 * <canvas linearGraph id="{{item.SerialNo}}" width="100" height="15"
 * style="border: 1px solid #000000;"> var c =
 * document.getElementById("{{item.SerialNo}}"); var ctx = c.getContext("2d");
 * var grd = ctx.createLinearGradient(0, 0, 100, 0); grd.addColorStop(0,
 * "green"); grd.addColorStop(1, "red");
 * 
 * ctx.fillStyle="#dddddd";
 * ctx.fillRect(50+(6.25*$scope.item.VentilationDiff)-2,0,4,15); ctx.fillStyle =
 * grd; ctx.fillRect(0, 0, 100, 15);
 * 
 * 
 */
