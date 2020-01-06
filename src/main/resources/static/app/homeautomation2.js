var buttonControl = angular.module("ButtonControl", []);
buttonControl.controller("ButtonController",[ function($scope, $http) {
	
	$scope.setBoiler = function(state) {

		$http({
			method : "GET",
			url : '/homeautomation/boiler/'+state
		}).then(function mySuccess(response) {
			$scope.myWelcome = response.data;
		}, function myError(response) {
			$scope.myWelcome = response.statusText;
		});

	};
	$scope.setAllLights = function(state) {
		$http({
			method : "GET",
			url : '/homeautomation/lightAll/' + state
		}).then(function mySuccess(response) {
			$scope.myWelcome = response.data;
		}, function myError(response) {
			$scope.myWelcome = response.statusText;
		});
	};

	$scope.setHeating = function(type) {

		$http({
			method : "GET",
			url : '/homeautomation/heating/' + type
		}).then(function mySuccess(response) {
			$scope.myWelcome = response.data;
		}, function myError(response) {
			$scope.myWelcome = response.statusText;
		});
	};
});
