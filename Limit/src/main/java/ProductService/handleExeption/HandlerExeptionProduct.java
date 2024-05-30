package ProductService.handleExeption;

public class HandlerExeptionProduct extends RuntimeException {
    public HandlerExeptionProduct(String messException, String anyValue) {
        super(String.join(" ", messException,  anyValue));
    }
}
