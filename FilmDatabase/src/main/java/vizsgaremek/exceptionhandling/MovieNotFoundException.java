package vizsgaremek.exceptionhandling;

public class MovieNotFoundException extends RuntimeException {
    private int idNotFound;

    public MovieNotFoundException(int idNotFound) {
        this.idNotFound = idNotFound;
    }

    public int getIdNotFound() {
        return idNotFound;
    }
}
