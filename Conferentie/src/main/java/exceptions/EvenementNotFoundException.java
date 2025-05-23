package exceptions;

public class EvenementNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EvenementNotFoundException(Integer id) {
	    super("Could not find event %s".formatted(id));
	  }
}