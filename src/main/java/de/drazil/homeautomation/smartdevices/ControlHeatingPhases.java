package de.drazil.homeautomation.smartdevices;

import java.util.ArrayList;
import java.util.List;

import de.drazil.homeautomation.bean.HeatingPhase;
import de.drazil.homeautomation.bean.HeatingProfile;

public class ControlHeatingPhases {

	public static void main(String[] args) throws Exception {
		// -------------------------------------------------------------------------------------------------------------------------------------
		// flur profile
		/*
		 * System.out.println("Setup Heatingphase for CORRIDOR");
		 * List<HeatingPhase> floorHeatingPhases = new ArrayList<>();
		 * floorHeatingPhases.add(new HeatingPhase("00:00", 17.0));
		 * floorHeatingPhases.add(new HeatingPhase("07:00", 18.5));
		 * floorHeatingPhases.add(new HeatingPhase("21:00", 17.0));
		 * 
		 * HomegearDeviceFactory.getRemoteWallThermostatBySerialNo("LEQ1215197")
		 * .setHeatingProfile( new HeatingProfile(Day.ALL_DAYS,
		 * floorHeatingPhases), WeekProgram.WEEK_PROGRAM_1, false);
		 */

		// -------------------------------------------------------------------------------------------------------------------------------------
		// badezimmer rosa NEQ0937684
		/*
		 * System.out.println("Setup Heatingphase for PINK BATHROOM");
		 * List<HeatingPhase> bathroomPinkHeatingPhasesWorkDays = new
		 * ArrayList<>(); bathroomPinkHeatingPhasesWorkDays.add(new
		 * HeatingPhase("00:00", 17.0));
		 * bathroomPinkHeatingPhasesWorkDays.add(new HeatingPhase("05:00",
		 * 19.5)); bathroomPinkHeatingPhasesWorkDays.add(new
		 * HeatingPhase("08:00", 17.0));
		 * 
		 * List<HeatingPhase> bathroomPinkHeatingPhasesWeekend = new
		 * ArrayList<>(); bathroomPinkHeatingPhasesWeekend.add(new
		 * HeatingPhase("00:00", 17.0));
		 * bathroomPinkHeatingPhasesWeekend.add(new HeatingPhase("07:00",
		 * 19.5)); bathroomPinkHeatingPhasesWeekend.add(new
		 * HeatingPhase("09:00", 17.0));
		 * 
		 * HomegearDeviceFactory.getRemoteWallThermostatBySerialNo("NEQ0937684")
		 * .setHeatingProfile( WeekProgram.WEEK_PROGRAM_1, false, new
		 * HeatingProfile(Day.WORK_DAYS, bathroomPinkHeatingPhasesWorkDays), new
		 * HeatingProfile(Day.WEEKEND_DAYS, bathroomPinkHeatingPhasesWeekend));
		 */
		// -------------------------------------------------------------------------------------------------------------------------------------
		// wohnzimmer
		/*
		 * System.out.println("Setup Heatingphase for LIVINGROOM");
		 * List<HeatingPhase> livingroomWorkdayHeatingPhases = new
		 * ArrayList<>(); livingroomWorkdayHeatingPhases.add(new
		 * HeatingPhase("00:00", 17.0)); livingroomWorkdayHeatingPhases.add(new
		 * HeatingPhase("19:00", 20.0)); livingroomWorkdayHeatingPhases.add(new
		 * HeatingPhase("22:00", 17.0));
		 * 
		 * HomegearDeviceFactory.getRemoteWallThermostatBySerialNo("LEQ0992980")
		 * .setHeatingProfile( new HeatingProfile(Day.WORK_DAYS,
		 * livingroomWorkdayHeatingPhases), WeekProgram.WEEK_PROGRAM_1, false);
		 * 
		 * List<HeatingPhase> livingroomWeekendHeatingPhases = new
		 * ArrayList<>(); livingroomWeekendHeatingPhases.add(new
		 * HeatingPhase("00:00", 17.0)); livingroomWeekendHeatingPhases.add(new
		 * HeatingPhase("11:00", 20.0)); livingroomWeekendHeatingPhases.add(new
		 * HeatingPhase("22:00", 17.0));
		 * 
		 * HomegearDeviceFactory.getRemoteWallThermostatBySerialNo("LEQ0992980")
		 * .setHeatingProfile( new HeatingProfile(Day.WEEKEND_DAYS,
		 * livingroomWeekendHeatingPhases), WeekProgram.WEEK_PROGRAM_1, false);
		 */
		// -------------------------------------------------------------------------------------------------------------------------------------
		// kitchen
		/*
		 * System.out.println("Setup Heatingphase for KITCHEN");
		 * List<HeatingPhase> kitchenWorkdayPhases = new ArrayList<>();
		 * kitchenWorkdayPhases.add(new HeatingPhase("00:00", 17.0));
		 * kitchenWorkdayPhases.add(new HeatingPhase("06:00", 18.0));
		 * kitchenWorkdayPhases.add(new HeatingPhase("08:00", 17.0));
		 * kitchenWorkdayPhases.add(new HeatingPhase("17:00", 19.0));
		 * kitchenWorkdayPhases.add(new HeatingPhase("19:00", 17.0));
		 * 
		 * HomegearDeviceFactory.getRemoteWallThermostatBySerialNo("LEQ1213742")
		 * .setHeatingProfile( new HeatingProfile(Day.ALL_DAYS,
		 * kitchenWorkdayPhases), WeekProgram.WEEK_PROGRAM_1, false);
		 */
		// -------------------------------------------------------------------------------------------------------------------------------------
		// schlafzimmer
		/*
		 * System.out.println("Setup Heatingphase for BEDROOM");
		 * List<HeatingPhase> bedroomHeatingPhases = new ArrayList<>();
		 * bedroomHeatingPhases.add(new HeatingPhase("00:00", 17.0));
		 * bedroomHeatingPhases.add(new HeatingPhase("20:00", 19.0));
		 * bedroomHeatingPhases.add(new HeatingPhase("22:00", 17.0));
		 * 
		 * HomegearDeviceFactory.getRemoteWallThermostatBySerialNo("LEQ1213732")
		 * .setHeatingProfile( new HeatingProfile(Day.ALL_DAYS,
		 * bedroomHeatingPhases), WeekProgram.WEEK_PROGRAM_1, false);
		 */
		// -------------------------------------------------------------------------------------------------------------------------------------
		// b�ro manu
		/*
		 * List<HeatingPhase> wkphases = new ArrayList<>(); wkphases.add(new
		 * HeatingPhase("00:00", 16.0)); wkphases.add(new HeatingPhase("07:00",
		 * 21.0)); wkphases.add(new HeatingPhase("19:00", 16.0));
		 * 
		 * HomegearDeviceFactory.getRemoteWallThermostatBySerialNo("LEQ1214104")
		 * .setHeatingProfile(new HeatingProfile(Day.WORK_DAYS, wkphases),
		 * WeekProgram.WEEK_PROGRAM_1, true);
		 * 
		 * List<HeatingPhase> wephases = new ArrayList<>(); wephases.add(new
		 * HeatingPhase("00:00", 16.0));
		 * HomegearDeviceFactory.getRemoteWallThermostatBySerialNo("LEQ1214104")
		 * .setHeatingProfile(new HeatingProfile(Day.WEEKEND, wephases),
		 * WeekProgram.WEEK_PROGRAM_1, true);
		 */

		// -------------------------------------------------------------------------------------------------------------------------------------
		// büro guido
/*
		System.out.println("Setup Heatingphase for OFFICE GUIDO");
		List<HeatingPhase> office1WorkdayHeatingPhases = new ArrayList<>();
		office1WorkdayHeatingPhases.add(new HeatingPhase("00:00", 17.0));
		office1WorkdayHeatingPhases.add(new HeatingPhase("17:00", 19.5));
		office1WorkdayHeatingPhases.add(new HeatingPhase("22:00", 17.0));

		List<HeatingPhase> office1WeekendHeatingPhases = new ArrayList<>();
		office1WeekendHeatingPhases.add(new HeatingPhase("00:00", 17.0));
		office1WeekendHeatingPhases.add(new HeatingPhase("08:00", 19.5));
		office1WeekendHeatingPhases.add(new HeatingPhase("22:00", 17.0));

		HomegearDeviceFactory.getRemoteWallThermostatBySerialNo("LEQ0993127").setHeatingProfile(
				WeekProgram.WEEK_PROGRAM_1, false, new HeatingProfile(Day.WORK_DAYS, office1WorkdayHeatingPhases),
				new HeatingProfile(Day.WEEKEND_DAYS, office1WeekendHeatingPhases));
*/
		// -------------------------------------------------------------------------------------------------------------------------------------
		// keller

		/*
		 * System.out.println("Setup Heatingphase for GUESTROOM");
		 * List<HeatingPhase> cellarHeatingPhases = new ArrayList<>();
		 * cellarHeatingPhases.add(new HeatingPhase("00:00", 17.0));
		 * 
		 * HomegearDeviceFactory.getRemoteWallThermostatBySerialNo("LEQ1214100")
		 * .setHeatingProfile( WeekProgram.WEEK_PROGRAM_1, false, new
		 * HeatingProfile(Day.ALL_DAYS, cellarHeatingPhases));
		 */
		// -------------------------------------------------------------------------------------------------------------------------------------
		// Hobby2
		/*
		 * System.out.println("Setup Heatingphase for Hobby 2");
		 * List<HeatingPhase> cellar2HeatingPhases = new ArrayList<>();
		 * cellar2HeatingPhases.add(new HeatingPhase("00:00", 17.0));
		 * 
		 * HomegearDeviceFactory.getRemoteWallThermostatBySerialNo("NEQ0937677")
		 * .setHeatingProfile( WeekProgram.WEEK_PROGRAM_1, false, new
		 * HeatingProfile(Day.ALL_DAYS, cellar2HeatingPhases));
		 */
	}

}
