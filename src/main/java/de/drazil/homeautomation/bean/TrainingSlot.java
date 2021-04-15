package de.drazil.homeautomation.bean;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingSlot {
    private String location;
    private String line;
    private String timeslot;
    private LocalDate date;
    private String ownerName;
    private long ownerId;
    private boolean booked;
    private boolean supervision;
}
