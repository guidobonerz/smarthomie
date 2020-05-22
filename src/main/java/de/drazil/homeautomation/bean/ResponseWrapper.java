package de.drazil.homeautomation.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseWrapper {

	private boolean successful = false;
	private String message = null;
	private Object data = null;
	private int total = -1;

	public ResponseWrapper(final boolean successful, final String message) {
		this(successful, message, null, 0);
	}
}
