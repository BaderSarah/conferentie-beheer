package exceptions;

public class SprekerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SprekerNotFoundException(Integer id) {
	    super("Could not find speaker %s".formatted(id));
	  }
}