package vizsgaremek.exceptionhandling;

public class EpisodeNotFoundException extends RuntimeException {
    private int idNotFound;

    public EpisodeNotFoundException(int idNotFound) {
        this.idNotFound = idNotFound;
    }

    public int getIdNotFound() {
        return idNotFound;
    }
}
