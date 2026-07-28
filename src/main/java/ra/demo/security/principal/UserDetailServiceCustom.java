package ra.demo.security.principal;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import ra.demo.entity.User;
import ra.demo.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailServiceCustom implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserPrincipal loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username không tìm thấy: " + username));

        return UserPrincipal.builder()
                .user(user)
                .authorities(mapRoleToGrandAuthority(user.getRole()))
                .build();
    }

    private Collection<? extends GrantedAuthority> mapRoleToGrandAuthority(String role) {
        return List.of(new SimpleGrantedAuthority(role));
    }
}
