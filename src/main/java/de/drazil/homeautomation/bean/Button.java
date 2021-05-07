package de.drazil.homeautomation.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Button {
    private String text;
    private String callback;
}
