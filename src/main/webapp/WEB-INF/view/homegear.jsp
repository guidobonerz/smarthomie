<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="<c:url value="/css/font-awesome.min.css" />" type="text/css">

<link rel="stylesheet" href="<c:url value="/css/bootstrap.min.css"/>"
	type="text/css" />

<link rel="stylesheet"
	href="<c:url value="/css/bootstrap-theme.min.css"/>" type="text/css" />

<script src="<c:url value="/js/bootstrap.min.js"/>" /></script>

<title>Rilkomat</title>
</head>
<body>
	<center>
		<h2>Rilkomat</h2>
	</center>
	<table>
		<tr>
			<th>Location</th>
			<th>Unreachable</th>
			<th>SignalStrength</th>
			<th>LowBattery</th>
			<th>BatteryVoltage</th>
			<th>DesiredTemperature</th>
			<th>CurrentTemperature</th>
			<th>ControlMode</th>
			<th>ValveState</th>
			<th>Humidity</th>
			<th>Ventilation</th>
			<th>VentilationDiff</th>
			<th>Voltage</th>
			<th>Current</th>
			<th>Power</th>
			<th>Frequency</th>
			<th>State</th>
		</tr>

		<c:forEach var="device" items="${remoteRadiatorThermostatList}">
			<tr>
				<td><c:out value="${device.Location}" /></td>
				<td><c:out value="${device.Unreachable}" /></td>
				<td><c:out value="${device.SignalStrength}" />&nbsp;dBm</td>
				<td><c:out value="${device.LowBattery}" /></td>
				<td><c:out value="${device.BatteryVoltage}" />V</td>
				<td><c:out value="${device.DesiredTemperature}" />&nbsp;&deg;C</td>
				<td><c:out value="${device.CurrentTemperature}" />&nbsp;&deg;C</td>
				<td><c:out value="${device.ControlMode}" /></td>
				<td><c:out value="${device.ValveState}" />&nbsp;%</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</c:forEach>

		<c:forEach var="device" items="${remoteWallThermostatList}">
			<tr>
				<td><c:out value="${device.Location}" /></td>
				<td><c:out value="${device.Unreachable}" /></td>
				<td><c:out value="${device.SignalStrength}" />&nbsp;dBm</td>
				<td><c:out value="${device.LowBattery}" /></td>
				<td><c:out value="${device.BatteryVoltage}" />V</td>
				<td><c:out value="${device.DesiredTemperature}" />&nbsp;&deg;C</td>
				<td><c:out value="${device.CurrentTemperature}" />&nbsp;&deg;C</td>
				<td><c:out value="${device.ControlMode}" /></td>
				<td>&nbsp;</td>
				<td><c:out value="${device.Humidity}" />&nbsp;%</td>
				<td><c:out value="${device.Ventilation}" /></td>
				<td><c:out value="${device.VentilationDiff}" /></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</c:forEach>
		<c:forEach var="device" items="${remoteOutdoorWeatherSensorList}">
			<tr>
				<td><c:out value="${device.Location}" /></td>
				<td><c:out value="${device.Unreachable}" /></td>
				<td><c:out value="${device.SignalStrength}" />&nbsp;dBm</td>
				<td><c:out value="${device.LowBattery}" /></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td><c:out value="${device.CurrentTemperature}" />&nbsp;&deg;C</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td><c:out value="${device.Humidity}" />&nbsp;%</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</c:forEach>

		<c:forEach var="device" items="${remoteSwitchList}">
			<tr>
				<td><c:out value="${device.Location}" /></td>
				<td><c:out value="${device.Unreachable}" /></td>
				<td><c:out value="${device.SignalStrength}" />&nbsp;dBm</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td><c:out value="${device.Voltage}" />&nbsp;V</td>
				<td><c:out value="${device.Current}" />&nbsp;mA</td>
				<td><c:out value="${device.Power}" />&nbsp;W</td>
				<td><c:out value="${device.Frequency}" />&nbsp;Hz</td>
				<td><c:out value="${device.State}" /></td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>