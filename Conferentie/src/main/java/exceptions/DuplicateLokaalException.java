package exceptions;

public class DuplicateLokaalException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateLokaalException(Long id) {
	    super("Duplicate room %s".formatted(id));
	}
}
