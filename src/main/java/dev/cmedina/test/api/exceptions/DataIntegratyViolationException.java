package dev.cmedina.test.api.exceptions;

public class DataIntegratyViolationException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 4712454392809124929L;

	public DataIntegratyViolationException(String message) {
        super(message);
    }
}