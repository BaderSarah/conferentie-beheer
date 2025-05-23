package exceptions;

public class DuplicateLokaalException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateLokaalException(Integer id) {
	    super("Duplicate room %s".formatted(id));
	}
}
