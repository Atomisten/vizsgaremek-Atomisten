package vizsgaremek.exceptionhandling;


public class DeletedEpisodeNotFoundException extends RuntimeException {
    private int idNotFound;

    public DeletedEpisodeNotFoundException(int idNotFound) {
        this.idNotFound = idNotFound;
    }

    public int getIdNotFound() {
        return idNotFound;
    }
}
