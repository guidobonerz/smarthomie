package de.drazil.homeautomation.bean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ActivityState {
    private Module module;
    private Stage mainStage;
    private Stage bookingStage;
    private long userId;
    private long chatId;
    private String userName;
    private int selectedLocation;
    private int selectedDate;
    private int selectedLine;
    private int selectedTimeslot;
    private List<String> locationList = new ArrayList<>();
    private List<LocalDate> dateList = new ArrayList<>();
    private List<String> lineList = new ArrayList<>();
    private List<TrainingSlot> timeslotList = new ArrayList<>();

}
