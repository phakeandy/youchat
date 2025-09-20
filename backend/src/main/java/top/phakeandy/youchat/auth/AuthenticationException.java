package top.phakeandy.youchat.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class AuthenticationException extends ErrorResponseException {

  public AuthenticationException(String message) {
    super(
        HttpStatus.UNAUTHORIZED,
        ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, message),
        null);
    getBody().setTitle("认证失败");
  }

  public AuthenticationException(String message, Throwable cause) {
    super(
        HttpStatus.UNAUTHORIZED,
        ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, message),
        cause);
    getBody().setTitle("认证失败");
  }
}
