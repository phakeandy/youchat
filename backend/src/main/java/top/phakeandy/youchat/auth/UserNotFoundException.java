package top.phakeandy.youchat.auth;

public class UserNotFoundException extends AuthenticationException {

  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
