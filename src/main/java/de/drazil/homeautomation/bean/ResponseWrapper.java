package de.drazil.homeautomation.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResponseWrapper {

	private boolean successful = false;
	private String message = null;
	private Object data = null;
	private int total = -1;

	public ResponseWrapper(boolean successful, String message) {
		this(successful, message, null, 0);
	}

	public ResponseWrapper(boolean successful, String message, Object data,
			int total) {
		this.successful = successful;
		this.message = message;
		this.data = data;
		this.total = total;
	}
}
