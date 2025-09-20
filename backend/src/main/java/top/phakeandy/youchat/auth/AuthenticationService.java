package top.phakeandy.youchat.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {

  LoginResponse authenticate(
      LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response);
}
