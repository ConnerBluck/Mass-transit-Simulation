import java.lang.Exception;

public class NegativeFuelException extends RuntimeException {

    public NegativeFuelException (Bus bus) {
        super("The bus " + bus.getId() +" is out of fuel");
    }
}
