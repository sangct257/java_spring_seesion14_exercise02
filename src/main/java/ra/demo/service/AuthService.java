package ra.demo.service;

import ra.demo.dto.request.LoginRequest;
import ra.demo.dto.request.RegisterRequest;
import ra.demo.dto.response.JwtResponse;
import ra.demo.entity.User;

public interface AuthService {
    User register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
}