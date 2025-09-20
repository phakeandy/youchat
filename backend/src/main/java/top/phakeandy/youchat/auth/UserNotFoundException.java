package top.phakeandy.youchat.auth;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class UserNotFoundException extends ErrorResponseException {

  public UserNotFoundException(UUID userId) {
    super(
        HttpStatus.NOT_FOUND,
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "用户 " + userId + " 不存在。"),
        null
    );
    getBody().setTitle("用户未找到");
  }

  public UserNotFoundException(String username) {
    super(
        HttpStatus.NOT_FOUND,
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "用户 " + username + " 不存在。"),
        null
    );
    getBody().setTitle("用户未找到");
  }

  public UserNotFoundException() {
    super(
        HttpStatus.NOT_FOUND,
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "用户不存在"),
        null
    );
    getBody().setTitle("用户未找到");
  }
}
