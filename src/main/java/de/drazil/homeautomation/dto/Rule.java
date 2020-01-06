package de.drazil.homeautomation.dto;

import java.util.List;

import lombok.Data;

@Data
public class Rule {
	String name;
	private List<RuleMatcher> ruleMatcherList;
}
