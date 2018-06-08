package de.drazil.homeautomation.smartdevices;

public class Control {

	public static void main(String args[]) throws Exception {

		// Object retVal =
		// HomegearXmlRpcController.init("http://10.100.200.111:8080/homeautomation/jsonrpc",
		// "RpcClient", 0x11);
		// Object retVal =
		// HomegearXmlRpcController.init("http://10.100.200.111:8080/homeautomation/xmlrpc",
		// "RpcClient", 1);
		// System.out.println("rpc init:" + retVal);

		// -------------------------------------------------------------------------------------------------------------------------------------
		// flur profile

		// -------------------------------------------------------------------------------------------------------------------------------------
		// badezimmer rosa LEQ0786143
		// HomegearDeviceFactory.getRemoteRadiatorThermostatBySerialNo("LEQ0786143").setControlMode(HeatingMode.AUTO);
		// HomegearDeviceFactory.getRemoteRadiatorThermostatBySerialNo("LEQ0786143").setLocked(false,false);

		// HomegearDeviceFactory.getRemoteRadiatorThermostatBySerialNo("LEQ0786143").setControlMode(HeatingMode.MANUAL,
		// 0.0);

		// -------------------------------------------------------------------------------------------------------------------------------------
		// wohnzimmer

		// HomegearDeviceFactory.getRemoteWallThermostatBySerialNo("LEQ0992980").setControlMode(HeatingMode.AUTO);

		// HomegearDeviceFactory.getRemoteSwitchBySerialNo("LEQ0531814").setState(true);
		// -------------------------------------------------------------------------------------------------------------------------------------
		// kitchen

		// -------------------------------------------------------------------------------------------------------------------------------------
		// schlafzimmer

		// -------------------------------------------------------------------------------------------------------------------------------------
		// büro manu

		// -------------------------------------------------------------------------------------------------------------------------------------
		// büro guido

		// -------------------------------------------------------------------------------------------------------------------------------------
		// keller

		/*
		 * List<HomematicRemoteRadiatorThermostat> list = HomegearDeviceFactory
		 * .getSmartDeviceList(HomematicRemoteRadiatorThermostat.class);
		 * 
		 * for (HomematicRemoteRadiatorThermostat rt : list) {
		 * rt.setBacklightDisabled(true); }
		 */
		/*
		 * List<IHeatingDevice> list = HomegearDeviceFactory
		 * .getSmartDeviceList(IHeatingDevice.class);
		 * 
		 * for (IHeatingDevice rt : list) {
		 * System.out.println(rt.getLocation());
		 * rt.setWakeOnRadioEnabled(false); }
		 */
		// boiler
		// HomegearDeviceFactory.getRemoteValveDriveBySerialNo("HEQ0134004").setValveState(0);
		// HomegearDeviceFactory.getRemoteValveDriveBySerialNo("HEQ0134004").setErrorValvePosition(0);

		// System.out.println(HomegearDeviceFactory.getRemoteValveDriveBySerialNo("HEQ0134004").getValveState());
		// .setValveState(30);

		// alle heizkoerper tasten sperren
		/*
		 * List<RemoteRadiatorThermostat> list =
		 * HomegearDeviceFactory.getSmartDeviceList(RemoteRadiatorThermostat.
		 * class);
		 * 
		 * for (RemoteRadiatorThermostat rt : list) {
		 * System.out.println(rt.getLocation()); rt.setLocked(new Boolean(true
		 * ));
		 * 
		 * // rt.setControlMode(HeatingMode.MANUAL, new Double(100)); // //
		 * rt.setControlMode(HeatingMode.AUTO); }
		 */

	}
}
