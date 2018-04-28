var buttonControl = angular.module("ButtonControl", []);
buttonControl.controller("ButtonController", function($scope, $http) {
	$scope.setBoilerOn = function() {

		$http({
			method : "GET",
			url : '/homeautomation/boiler/true'
		}).then(function mySuccess(response) {
			$scope.myWelcome = response.data;
		}, function myError(response) {
			$scope.myWelcome = response.statusText;
		});

	};
	$scope.setBoilerOff = function() {

		$http({
			method : "GET",
			url : '/homeautomation/boiler/false'
		}).then(function mySuccess(response) {
			$scope.myWelcome = response.data;
		}, function myError(response) {
			$scope.myWelcome = response.statusText;
		});
	};
	$scope.setAllLightsOn = function() {

		$http({
			method : "GET",
			url : '/homeautomation/lightAll/true'
		}).then(function mySuccess(response) {
			$scope.myWelcome = response.data;
		}, function myError(response) {
			$scope.myWelcome = response.statusText;
		});
	};
	$scope.setAllLightsOff = function() {

		$http({
			method : "GET",
			url : '/homeautomation/lightAll/false'
		}).then(function mySuccess(response) {
			$scope.myWelcome = response.data;
		}, function myError(response) {
			$scope.myWelcome = response.statusText;
		});
	};
});
