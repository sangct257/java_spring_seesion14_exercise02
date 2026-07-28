package ra.demo.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public String generateToken(UserDetails userDetails) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(jwtExpiration)))
                .signWith(key)
                .claim("role", userDetails.getAuthorities().stream().findFirst().orElseThrow().getAuthority())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        try{
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        }catch (UnsupportedJwtException e){
            log.error("Hệ thống không hỗ trợ bảo mật với jwt "+e.getMessage());
            throw new JwtException("Hệ thống không hỗ trợ bảo mật với jwt "+e.getMessage());
        }catch (ExpiredJwtException e){
            log.error("Chuỗi JWT hết hạn "+e.getMessage());
            throw new JwtException("Chuỗi JWT hết hạn "+e.getMessage());
        }catch (MalformedJwtException e){
            log.error("Chuỗi JWT không đúng "+e.getMessage());
            throw new JwtException("Chuỗi JWT không đúng "+e.getMessage());
        }catch (JwtException e){
            log.error("Lỗi xử lý chuỗi JWT "+e.getMessage());
            throw new JwtException("Lỗi xử lý chuỗi JWT "+e.getMessage());
        } catch (Exception e) {
            log.error("Có lỗi xảy ra "+e.getMessage());
            throw new JwtException("Có lỗi xảy ra "+e.getMessage());
        }
    }
}
