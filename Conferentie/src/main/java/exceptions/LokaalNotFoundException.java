package exceptions;

public class LokaalNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LokaalNotFoundException(Long id) {
	    super("Could not find room %s".formatted(id));
	  }
}