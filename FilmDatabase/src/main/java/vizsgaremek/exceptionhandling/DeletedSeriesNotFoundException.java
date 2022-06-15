package vizsgaremek.exceptionhandling;

public class DeletedSeriesNotFoundException extends RuntimeException {
    private int idNotFound;

    public DeletedSeriesNotFoundException(int idNotFound) {
        this.idNotFound = idNotFound;
    }

    public int getIdNotFound() {
        return idNotFound;
    }
}
