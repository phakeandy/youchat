package top.phakeandy.youchat.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class InvalidPasswordException extends ErrorResponseException {

  private static final long serialVersionUID = 1L;

  public InvalidPasswordException(String message) {
    super(
        HttpStatus.BAD_REQUEST,
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message),
        null);
    getBody().setTitle("密码格式错误");
  }
}
