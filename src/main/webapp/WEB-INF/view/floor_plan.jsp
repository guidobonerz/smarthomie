<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>Rilkomat</title>
<style type="text/css">
body {
	margin: 0;
	padding: 0;
}

#eg-svgLayer {
	position: absolute;
	top: 0px;
	left: 0px;
}

#ug-svgLayer {
	position: absolute;
	top: 0px;
	left: 0px;
}

#switch {
	position: absolute;
	visibility: hidden;
	border-width: 2px;
	border-style: solid;
	border-color: #ff9955;
	border-radius: 5px;
	width: 40px;
	height: 20px;
	z-index: 10000;
	color: #ffffff;
	width: 40px;
}

#on {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 20px;
	height: 20px;
	border-radius: 3px;
	background-color: #00ff00;
}

#off {
	position: absolute;
	top: 0px;
	left: 20px;
	width: 20px;
	height: 20px;
	border-radius: 3px;
	background-color: #ff0000;
}

ul {
	list-style-type: none;
	padding: 0;
	margin: 0;
}

#eg {
	visibility: hidden;
}

#ug {
	visibility: hidden;
}
</style>
<link rel="stylesheet"
	href='<c:url value="/css/jquery.slider.min.css" />' type="text/css">

<script src='<c:url value="/js/jquery-1.7.1.js" />'></script>
<script src='<c:url value="/js/jquery.slider.min.js" />'></script>

<script type="text/javascript">
	var selectedSerialNo;
	var spinbox;
	/*
	 function showWeatherSensorData(jsonObject) {

	 var slidersDiv = document.getElementById("sliders");
	 var le = document.createElement("ul");
	 slidersDiv.appendChild(le);

	 for (i = 0; i < jsonObject.length; i++) {
	 var serialNo = jsonObject[i].SerialNo;
	 var location = jsonObject[i].Location;
	 var level = location.split('-')[0]
	 var room = $("#" + location);

	 var divElement = document.createElement("div");
	 divElement.id = serialNo;
	 $(divElement).css("color", "#ffffff");
	 $(divElement).css("position", "absolute");
	 $(divElement).css("width", "50");
	 $(divElement).css("height", "50");
	 $(divElement).css("font-family",
	 "'Arial Narrow', Arial, sans-serif");
	 $(divElement).css("font-size", "10");
	 $(divElement).css("font-style", "normal");

	 if (room[0].nodeName === "path") {
	 var pos = room[0].pathSegList[4]
	 $(divElement).css("left", (pos.x + 4));
	 $(divElement).css("top", (pos.y + 4));
	 } else {
	 $(divElement).css("left", parseInt(room.attr("x")) + 4);
	 $(divElement).css("top", parseInt(room.attr("y")) + 4);
	 }

	 var listElement = document.createElement("ul");
	 var temperatureElement = document.createElement("li");
	 temperatureElement.innerHTML = "T:"
	 + jsonObject[i].CurrentTemperature + "&nbsp;&deg;C";
	 var humidityElement = document.createElement("li");
	 humidityElement.innerHTML = "H:" + jsonObject[i].Humidity + "%";
	 listElement.appendChild(temperatureElement);
	 listElement.appendChild(humidityElement);
	 divElement.appendChild(listElement);
	 document.getElementById(level).appendChild(divElement);

	 if (jsonObject[i].Controller == true) {

	 var sliderLiText = document.createElement("li");
	 var sliderText = document.createElement("p");
	 sliderText.innerHTML = location;
	 sliderLiText.appendChild(sliderText);
	 var sliderValue = document.createElement("p");
	 sliderValue.id = "slider_value_" + serialNo;
	 sliderValue.innerHTML = jsonObject[i].DesiredTemperature
	 + "&deg;C";
	 sliderLiText.appendChild(sliderValue);
	 le.appendChild(sliderLiText);

	 var sliderLi = document.createElement("li");
	 var sliderDiv = document.createElement("div");
	 sliderDiv.id = "slider_" + serialNo;
	 sliderLi.appendChild(sliderDiv);

	 le.appendChild(sliderLi);

	 $("#slider_" + serialNo).slider(
	 {
	 range : "min",
	 value : jsonObject[i].DesiredTemperature,
	 min : 0,
	 max : 25,
	 step : 0.5,
	 change : function(event, ui) {

	 var start = ui.handle.parentNode.id
	 .indexOf("LEQ");
	 var sn = ui.handle.parentNode.id.substr(start,
	 10);

	 $.ajax({
	 url : '/homegear/setManualTemperature/'
	 + sn + '/' + (ui.value * 10),
	 type : 'GET',
	 dataType : 'html',
	 success : function(data, textStatus, xhr) {
	 var jsonObject = $.parseJSON(data);
	 },
	 error : function(xhr, textStatus,
	 errorThrown) {
	 alert('ERROR!');
	 }
	 });

	 },
	 slide : function(event, ui) {
	 var start = ui.handle.parentNode.id
	 .indexOf("LEQ");
	 var sn = ui.handle.parentNode.id.substr(start,
	 10);
	 $("#slider_value_" + sn).html(
	 ui.value + "&nbsp;&deg;C");
	 }
	 });
	 }
	 }
	 }
	 */
	function showWeatherSensorData(jsonObject) {

		var slidersDiv = document.getElementById("sliders");
		var list = document.createElement("ul");
		slidersDiv.appendChild(list);

		for (i = 0; i < jsonObject.length; i++) {
			var serialNo = jsonObject[i].SerialNo;
			var location = jsonObject[i].Location;
			var level = location.split('-')[0]
			var room = $("#" + location);

			var svgLayer = document.getElementById(level + "-svgLayer");
			var style = "fill:#ffffff;stroke:none;font-size:10px";

			var temperatureElement = document.createElementNS(
					"http://www.w3.org/2000/svg", "text");
			var humidityElement = document.createElementNS(
					"http://www.w3.org/2000/svg", "text");

			if (room[0].nodeName === "path") {
				var pos = room[0].pathSegList[4]
				temperatureElement.setAttribute("x", (pos.x + 4));
				temperatureElement.setAttribute("y", (pos.y + 4 + 10));
				humidityElement.setAttribute("x", (pos.x + 4));
				humidityElement.setAttribute("y", (pos.y + 4 + 20));

			} else {

				temperatureElement.setAttribute("x",
						parseInt(room.attr("x")) + 4);
				temperatureElement.setAttribute("y",
						parseInt(room.attr("y")) + 4 + 10);
				humidityElement.setAttribute("x", parseInt(room.attr("x")) + 4);
				humidityElement.setAttribute("y",
						parseInt(room.attr("y")) + 4 + 20);
			}

			var temperatureValue = document.createTextNode("T:"
					+ jsonObject[i].CurrentTemperature + "\u00a0\u00b0C");
			var humidityValue = document.createTextNode("H:"
					+ jsonObject[i].Humidity + "%");

			temperatureElement.appendChild(temperatureValue);
			temperatureElement.setAttribute("style", style);
			humidityElement.appendChild(humidityValue);
			humidityElement.setAttribute("style", style);

			svgLayer.appendChild(temperatureElement);
			svgLayer.appendChild(humidityElement);

			if (jsonObject[i].Controller == true) {

				var sliderLiText = document.createElement("li");
				var sliderText = document.createElement("p");
				sliderText.innerHTML = location;
				sliderLiText.appendChild(sliderText);
				list.appendChild(sliderLiText);

				var sliderLi = document.createElement("li");
				var sliderDiv = document.createElement("div");
				var sliderInput = document.createElement("input");
				sliderInput.setAttribute("type", "slider");
				sliderInput.setAttribute("name", "temp");
				sliderInput.setAttribute("value",
						jsonObject[i].DesiredTemperature);
				sliderInput.id = 'slider_' + serialNo;
				sliderLi.appendChild(sliderDiv);
				sliderDiv.appendChild(sliderInput);

				list.appendChild(sliderLi);

				$("#slider_" + serialNo)
						.slider(
								{
									from : 0,
									to : 25,
									step : 0.5,
									skin : 'round_plastic',
									dimension : '&nbsp;&deg;C',
									round : 1,
									format : {
										format : '#0.0',
										locale : 'en'
									},
									callback : function(value) {

										var start = this.inputNode[0].id
												.indexOf("LEQ");
										var sn = this.inputNode[0].id.substr(
												start, 10);

										$
												.ajax({
													url : '/homegear/setManualTemperature/'
															+ sn
															+ '/'
															+ (value * 10),
													type : 'GET',
													dataType : 'html',
													success : function(data,
															textStatus, xhr) {
														var jsonObject = $
																.parseJSON(data);
													},
													error : function(xhr,
															textStatus,
															errorThrown) {
														alert('ERROR!');
													}
												});

									}

								});
			}
		}
	}
	function _doSwitch(serialNo, state) {
		$.ajax({
			url : '/homegear/switch/' + serialNo + '/' + state,
			type : 'GET',
			dataType : 'html',
			success : function(data, textStatus, xhr) {
				var jsonObject = $.parseJSON(data);
			},
			error : function(xhr, textStatus, errorThrown) {
				alert('ERROR!');
			}
		});
	}

	function doSwitch(state) {
		_doSwitch(selectedSerialNo, state);
	}

	function getWeatherSensors() {
		$.ajax({
			url : '/homegear/getWeatherSensors',
			type : 'GET',
			dataType : 'html',
			success : function(data, textStatus, xhr) {
				var jsonObject = $.parseJSON(data);
				showWeatherSensorData(jsonObject);
			},
			error : function(xhr, textStatus, errorThrown) {
				alert('ERROR!');
			}
		});
	}

	function getSwitches() {
		$.ajax({
			url : '/homegear/getSwitches',
			type : 'GET',
			dataType : 'html',
			success : function(data, textStatus, xhr) {
				var jsonObject = $.parseJSON(data);
			},
			error : function(xhr, textStatus, errorThrown) {
				alert('ERROR!');
			}
		});
	}

	function showSwitch(id, serialNo) {
		selectedSerialNo = serialNo;
		var el = $("#" + id);
		var cx = el.attr("cx");
		var cy = el.attr("cy");
		var sw = $("#switch");
		var x = (parseInt(cx) - 8) + "px";
		var y = (parseInt(cy) - 8) + "px";
		sw.css("left", x);
		sw.css("top", y);
		sw.css("visibility", "visible");
	}

	function hideSwitch(id, serialNo) {
		selectedSerialNo = null;
		var sw = $("#switch");
		sw.css("visibility", "hidden");
	}

	function showLevel(level) {
		if (level === 'ug') {
			$("#ug").css("visibility", "visible");
			$("#eg").css("visibility", "hidden");
		} else if (level === 'eg') {
			$("#ug").css("visibility", "hidden");
			$("#eg").css("visibility", "visible");
		}
	}

	$(document).ready(function() {
		getWeatherSensors();
		showLevel('eg');
		//$(document).tooltip();
	});
</script>
</head>
<body style="background-color: #000000">



	<div id="sliders"
		style="position: absolute; top: 0px; left: 400; width: 200px; height: 800px; background: #888888"></div>



	<div id="eg">
		<svg id="eg-svgLayer" width="370" height="600"
			preserveAspectRatio="xMinYMin meet" viewBox="0 0 370 600">
 <defs>
	  <style>
rect, path {
	fill: #222222;
	stroke: #909090;
	stroke-width: 2px;
}

rect:hover, path:hover {
	fill: #444444;
}
</style>
  </defs>
  	<rect id="eg-bathroom1" x="0" y="410" width="45" height="35" />
	<rect id="eg-bathroom2" x="198" y="415" width="45" height="30" />
	<rect id="eg-kitchen" x="243" y="380" width="62" height="65" />
	<rect id="eg-livingroom" x="210" y="140" width="95" height="145" />
	<rect id="eg-garage" x="305" y="50" width="52" height="175" />
	<rect id="eg-bedroom" x="0" y="325" width="75" height="85" />
	<rect id="eg-office1" x="75" y="325" width="50" height="85" />
	<rect id="eg-office2" x="125" y="325" width="50" height="85" />
	<rect id="eg-bathroom3" x="220" y="380" width="23" height="35" />
	<rect id="eg-terasse" x="170" y="140" width="40" height="185" />
	<rect id="eg-outdoor" x="50" y="140" width="70" height="70" />
	<rect id="eg-windfang" x="270" y="285" width="35" height="45" />
				
	<path id="eg-corridor"
				d="M45,410 L175,410 L175,325 L210,325 L210,285 L270,285 L270,330 L305,330 L305,380 L220,380 L220,415 L198,415 L198,445 L45,445 Z" />
		
	<path id="eg-stair"
				d="M175,325 L210,325 L210,345 L195,345 L195,362 L175,362 Z"
				onclick="showLevel('ug');" />
	
	
	<circle id="eg-livingroom_smoke_detector" cx="230" cy="270" r="5"
				style="fill:#2DFFAE" />
	<circle id="eg-bedroom_smoke_detector" cx="60" cy="400" r="5"
				style="fill:#2DFFAE" />
	<circle id="eg-floor_smoke_detector1" cx="145" cy="435" r="5"
				style="fill:#2DFFAE" />
	<circle id="eg-floor_smoke_detector2" cx="265" cy="335" r="5"
				style="fill:#2DFFAE" />
	
	
	
	<circle id="eg-motion_detector1" cx="210" cy="140" r="5"
				style="fill:#f00000" /> 
	<circle id="eg-motion_detector2" cx="270" cy="285" r="5"
				style="fill:#f00000" />
	<circle id="eg-motion_detector3" cx="220" cy="415" r="5"
				style="fill:#f00000" />
	<circle id="eg-corridor-lamp1" cx="180" cy="375" r="5"
				style="fill:#ffe345"
				onmouseover="showSwitch('corridor-lamp1','LEQ0531814');" />
			
			
			
	<rect id="eg-bathroom1-window" x="10" y="444" width="30" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />			
	<rect id="eg-corridor-window1" x="60" y="444" width="30" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	<rect id="eg-corridor-window2" x="110" y="444" width="30" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	<rect id="eg-corridor-window3_l" x="304" y="340" width="3" height="15"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	<rect id="ceg-orridor-window3_r" x="304" y="358" width="3" height="15"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	<rect id="eg-door1" x="304" y="305" width="3" height="20"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	
		
	<rect id="eg-bathroom2-window-l" x="204" y="444" width="14" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	<rect id="eg-bathroom2-window-r" x="220" y="444" width="14" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	
	<rect id="eg-kitchen-window-l" x="257" y="444" width="15" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	<rect id="eg-kitchen-window-r" x="274" y="444" width="15" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	
	
	<rect id="eg-bedroom-window-l" x="25" y="323" width="15" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	<rect id="eg-bedroom-window-r" x="42" y="323" width="15" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	<rect id="eg-office1-window" x="87" y="323" width="26" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	<rect id="eg-office2-window" x="137" y="323" width="26" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	
	<rect id="eg-livingroom-window-l" x="208" y="147" width="3" height="62"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	<rect id="eg-livingroom-window-r" x="208" y="215" width="3" height="62"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	<rect id="eg-door2" x="208" y="288" width="3" height="20"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	
			
	<rect id="eg-bathroom3-window" x="226" y="387" width="10" height="20"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	
	<text x="50" y="50" style="fill:#ffffff;sroke:#ffffff">Rilkomat</text>
	
	</svg>
	</div>


	<div id="ug">
		<svg id="ug-svgLayer" width="370" height="600"
			preserveAspectRatio="xMinYMin meet" viewBox="0 0 370 600">
 <defs>
	  <style>
rect, path {
	fill: #222222;
	stroke: #909090;
	stroke-width: 2px;
}

rect:hover, path:hover {
	fill: #444444;
}
</style>
  </defs>
  	<rect id="ug-garage" x="305" y="50" width="52" height="175"
				style="stroke-dasharray: 4,4;" />
	<rect id="ug-terasse" x="170" y="140" width="40" height="185"
				style="stroke-dasharray: 4,4;" />
  	<rect id="ug-livingroom" x="210" y="140" width="95" height="110" />
  	<rect id="ug-hobby1" x="243" y="285" width="62" height="160" />
	<rect id="ug-hobby2" x="75" y="325" width="100" height="85" />
	<rect id="ug-boilerroom" x="204" y="390" width="39" height="55" />
	<rect id="ug-lumberroom" x="270" y="250" width="35" height="35" />
	
	
		
	
	<path id="ug-corridor"
				d="M175,362 L195,362 L195,345 L210,345 L210,250 L270,250 L270,285 L243,285 L243,390 L175,390 Z" />

	<path id="ug-laundry"
				d="M0,325 L75,325 L75,410 L175,410 L175,390 L204,390 L204,445 L0,445 Z" />
		
	<path id="ug-stair"
				d="M175,325 L210,325 L210,345 L195,345 L195,362 L175,362 Z"
				onclick="showLevel('eg');" />
	
	<circle id="ug-motion1" cx="210" cy="140" r="5" style="fill:#f00000" /> 
	
	<circle id="ug-floor_smoke_detector" cx="235" cy="325" r="5"
				style="fill:#2DFFAE" />
					
	<rect id="ug-livingroom-window" x="242" y="139" width="30" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	
	
	
	<rect id="ug-lumberroom-window" x="304" y="270" width="3" height="10"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
		
		
		
	<rect id="ug-boilerroom-window" x="213" y="444" width="20" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
		
	<rect id="ug-hobby1-window1" x="264" y="444" width="20" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	<rect id="ug-hobby1-window2" x="304" y="360" width="3" height="20"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	
	<rect id="ug-laundry-door" x="48" y="323" width="20" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	<rect id="ug-laundry-window1" x="23" y="323" width="20" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	<rect id="ug-laundry-window2" x="10" y="444" width="20" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	<rect id="ug-laundry-window3" x="110" y="444" width="20" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	
	<rect id="ug-hobby2-window1" x="87" y="323" width="20" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	<rect id="ug-hobby2-window2" x="117" y="323" width="20" height="3"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
		
	<rect id="ug-corridor-window" x="208" y="255" width="3" height="20"
				style="fill:#6060ff;stroke:#000000;stroke-width:0px" />
	
	
	<text x="50" y="50" style="fill:#ffffff;sroke:#ffffff">Rilkomat</text>
	
	
	
	
	
	
	
	</svg>
	</div>





	<div id="switch">
		<div id="on" onclick="doSwitch(true);"></div>
		<div id="off" onclick="doSwitch(false);"></div>
	</div>

</body>
</html>