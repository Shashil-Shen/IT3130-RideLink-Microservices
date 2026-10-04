package lk.ac.sliit.ridelink.account.domain.exception;

public class InactiveAccountException extends RuntimeException 
{
    public InactiveAccountException() {
        super("Account is not active");
    }
}
