package com.growcast.growcast.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "secret_jwt_keyfskdjfksdfjlsjdlfjslkdjflsjdlfkjslkdjfjshdsljdflskjdfl"; //보안상 환경변수로넣어야 하는데 일단

    private static final long ACCESS_EXPIRATION_TIME = 1000 * 60 * 60 * 24;       //24시간
    private static final long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;  //7일

    private final Map<String, String> refreshTokenStore = new HashMap<>();

    public String generateToken(Long userId, String nickname) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("nickname", nickname)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        String refreshToken = Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
        refreshTokenStore.put(userId.toString(), refreshToken);
        return refreshToken;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        return Long.parseLong(
                Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }

    public boolean isRefreshTokenMatch(Long userId, String token) {
        return token.equals(refreshTokenStore.get(userId.toString()));
    }

    public void revokeRefreshToken(Long userId) {
        refreshTokenStore.remove(userId.toString());
    }
}
