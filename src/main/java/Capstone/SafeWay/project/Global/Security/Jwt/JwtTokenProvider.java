package Capstone.SafeWay.project.Global.Security.Jwt;

import Capstone.SafeWay.project.Global.Security.UserDetailsServiceImpl;
import Capstone.SafeWay.project.User.Dto.UserDetailsImpl;
import Capstone.SafeWay.project.User.Exception.UserNotFoundException;
import Capstone.SafeWay.project.User.UserEntity;
import Capstone.SafeWay.project.User.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final UserRepository userRepository;
    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    private final Key key;

    private final UserDetailsServiceImpl userDetailsService;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret, UserDetailsServiceImpl userDetailsService, UserRepository userRepository){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id", userDetails.getUser().getId())
                .claim("auth", "ROLE_USER")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String token(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("auth", "ROLE_USER")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateAccessToken(String accessToken){
        if(accessToken == null)
            throw new JwtException("AccessToken is null");

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException("토큰이 만료되었습니다.");
        } catch (MalformedJwtException e) {
            throw new JwtException("토큰의 형식이 옳바르지 않습니다.");
        } catch (SignatureException e) {
            throw new JwtException("토큰의 서명이 옳바르지 않습니다.");
        } catch(SecurityException e) {
            throw new JwtException("토큰 오류 ㅅㄱ");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("토큰의 형식이 만료되었습니다. ");
        }
    }

    public String getUserEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getTokenFromRequest(HttpServletRequest req){
        return req
                .getHeader("Authorization")
                .substring(7);
    }

    public Authentication getAuthentication(String accessToken){

        Claims claims = getClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(getEmailFromToken(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public Claims getClaims(String accessToken){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }

    public String getEmailFromToken(String accessToken){
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();
    }
}
