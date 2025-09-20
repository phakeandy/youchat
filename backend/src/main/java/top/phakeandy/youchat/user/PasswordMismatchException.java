package top.phakeandy.youchat.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class PasswordMismatchException extends ErrorResponseException {

  private static final long serialVersionUID = 1L;

  public PasswordMismatchException() {
    super(
        HttpStatus.BAD_REQUEST,
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "密码和确认密码不一致。"),
        null);
    getBody().setTitle("密码不匹配");
  }
}
