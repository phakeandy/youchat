package top.phakeandy.youchat.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class InvalidCredentialsException extends ErrorResponseException {

  private static final long serialVersionUID = 1L;

  public InvalidCredentialsException(String message) {
    super(
        HttpStatus.UNAUTHORIZED,
        ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, message),
        null);
    getBody().setTitle("凭据无效");
  }

  public InvalidCredentialsException(String message, Throwable cause) {
    super(
        HttpStatus.UNAUTHORIZED,
        ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, message),
        cause);
    getBody().setTitle("凭据无效");
  }
}
