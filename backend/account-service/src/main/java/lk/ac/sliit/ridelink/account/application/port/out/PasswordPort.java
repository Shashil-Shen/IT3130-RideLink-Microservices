package lk.ac.sliit.ridelink.account.application.port.out;

public interface PasswordPort 
{
    String hash(String rawPassword);
    boolean matches(String rawPassword, String passwordHash);
}
