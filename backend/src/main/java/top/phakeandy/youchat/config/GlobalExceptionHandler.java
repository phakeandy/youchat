package top.phakeandy.youchat.config;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleGenericException(Exception ex) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "系统发生未知错误");
    problemDetail.setTitle("服务器内部错误");
    return problemDetail;
  }
}
