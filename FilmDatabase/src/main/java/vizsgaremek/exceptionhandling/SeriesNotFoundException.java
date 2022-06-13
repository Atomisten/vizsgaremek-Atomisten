package vizsgaremek.exceptionhandling;

public class SeriesNotFoundException extends RuntimeException {
    private int idNotFound;

    public SeriesNotFoundException(int idNotFound) {
        this.idNotFound = idNotFound;
    }

    public int getIdNotFound() {
        return idNotFound;
    }
}
