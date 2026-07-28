package ra.demo.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.demo.dto.request.LoginRequest;
import ra.demo.dto.request.RegisterRequest;
import ra.demo.dto.response.ApiResponse;
import ra.demo.dto.response.JwtResponse;
import ra.demo.entity.User;
import ra.demo.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/test")
    public String test() {
        return "Đăng nhập thành công";
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(
            @Valid @RequestBody RegisterRequest request) {

        return new ResponseEntity<>(new ApiResponse<>(
                true,
                "Đăng ký thành công",
                authService.register(request),
                null,
                HttpStatus.CREATED
        ),HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestBody LoginRequest request) {

        try {
            JwtResponse user = authService.login(request);

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Đăng nhập thành công",
                            user,
                            null,
                            HttpStatus.OK
                    )
            );

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null,
                            null,
                            HttpStatus.UNAUTHORIZED
                    ));
        }
    }
}