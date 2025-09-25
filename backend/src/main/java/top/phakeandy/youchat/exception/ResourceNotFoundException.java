package top.phakeandy.youchat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class ResourceNotFoundException extends ErrorResponseException {

  private static final long serialVersionUID = 1L;

  public ResourceNotFoundException(String message) {
    super(
        HttpStatus.NOT_FOUND,
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message),
        null);
    getBody().setTitle("资源不存在");
  }
}
