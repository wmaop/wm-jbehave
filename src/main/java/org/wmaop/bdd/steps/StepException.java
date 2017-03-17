package org.wmaop.bdd.steps;

@SuppressWarnings("serial")
public class StepException extends RuntimeException {

	public StepException(String message) {
		super(message);
	}

	public StepException(Throwable cause) {
		super(cause);
	}

	public StepException(String message, Throwable cause) {
		super(message, cause);
	}
}
