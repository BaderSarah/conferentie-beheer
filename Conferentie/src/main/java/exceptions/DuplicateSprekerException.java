package exceptions;

public class DuplicateSprekerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateSprekerException(Long id) {
	    super("Duplicate speaker %s".formatted(id));
	}
}