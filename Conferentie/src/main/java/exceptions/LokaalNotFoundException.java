package exceptions;

public class LokaalNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LokaalNotFoundException(Integer id) {
	    super("Could not find room %s".formatted(id));
	  }
}