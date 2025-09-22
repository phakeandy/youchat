package top.phakeandy.youchat.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class UsernameAlreadyExistsException extends ErrorResponseException {

  private static final long serialVersionUID = 1L;

  public UsernameAlreadyExistsException(String username) {
    super(
        HttpStatus.CONFLICT,
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "用户名 " + username + " 已存在。"),
        null);
    getBody().setTitle("用户名已存在");
  }
}
