package top.phakeandy.youchat.auth;

public class InvalidCredentialsException extends AuthenticationException {

  public InvalidCredentialsException(String message) {
    super(message);
  }

  public InvalidCredentialsException(String message, Throwable cause) {
    super(message, cause);
  }
}
