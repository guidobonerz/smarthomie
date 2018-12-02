package de.drazil.homeautomation.dto;

import java.util.List;

import lombok.Data;

@Data
public class RuleMatcher {
	private List<RuleMatcher> ruleMatcherList;
	private String attribute;
	private String comparator;
	private String type;
	private String[] value;
}
