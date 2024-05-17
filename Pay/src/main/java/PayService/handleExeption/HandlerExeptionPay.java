package PayService.handleExeption;

public class HandlerExeptionPay extends RuntimeException{
    public HandlerExeptionPay(String messException, String anyValue){
        super(String.join(" ", messException,  anyValue));
    }
}
