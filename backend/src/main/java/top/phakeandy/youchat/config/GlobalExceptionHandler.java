package top.phakeandy.youchat.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import top.phakeandy.youchat.auth.AuthenticationException;
import top.phakeandy.youchat.auth.InvalidCredentialsException;
import top.phakeandy.youchat.auth.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AuthenticationException.class)
  public ProblemDetail handleAuthenticationException(AuthenticationException ex) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "认证过程中发生错误");
    problemDetail.setTitle("认证失败");
    return problemDetail;
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ProblemDetail handleUserNotFoundException(UserNotFoundException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "用户不存在");
    problemDetail.setTitle("用户未找到");
    return problemDetail;
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ProblemDetail handleInvalidCredentialsException(InvalidCredentialsException ex) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "用户名或密码错误");
    problemDetail.setTitle("凭据无效");
    return problemDetail;
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleGenericException(Exception ex) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "系统发生未知错误");
    problemDetail.setTitle("服务器内部错误");
    return problemDetail;
  }
}
