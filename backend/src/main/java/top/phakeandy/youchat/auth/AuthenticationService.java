package top.phakeandy.youchat.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import top.phakeandy.youchat.auth.request.LoginRequest;
import top.phakeandy.youchat.auth.response.LoginResponse;

@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface AuthenticationService {

  LoginResponse authenticate(
      LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);
}
