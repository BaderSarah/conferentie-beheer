package exception;

public class MaxFavouritesReachedException extends RuntimeException {
    public MaxFavouritesReachedException() {
        super("Maximum aantal favorieten bereikt");
    }
}
