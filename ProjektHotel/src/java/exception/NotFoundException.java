package exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(Long id)
    {
         super("nie znaleziono rezerwacji o podanym id" + id);
    }
}
