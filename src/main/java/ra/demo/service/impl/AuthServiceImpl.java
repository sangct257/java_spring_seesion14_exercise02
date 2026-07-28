package ra.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.demo.dto.request.LoginRequest;
import ra.demo.dto.request.RegisterRequest;
import ra.demo.dto.response.JwtResponse;
import ra.demo.entity.User;
import ra.demo.repository.UserRepository;
import ra.demo.security.jwt.JwtProvider;
import ra.demo.service.AuthService;

import java.util.NoSuchElementException;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider JwtProvider;

    @Override
    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role("USER")
                .enabled(true)
                .build();

        return userRepository.save(user);
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = JwtProvider.generateToken(userDetails);

            return JwtResponse.builder()
                    .accessToken(token)
                    .type("Bearer")
                    .username(authentication.getName())
                    .build();
        } catch (Exception e) {
//            throw new NoSuchElementException("Username hoặc password không đúng");
            e.printStackTrace();
            throw e;

        }
    }
}
