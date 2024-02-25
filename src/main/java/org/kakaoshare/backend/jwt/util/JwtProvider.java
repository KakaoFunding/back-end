package org.kakaoshare.backend.jwt.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtProvider {
    private final Key key;

    public JwtProvider(@Value("${spring.jwt.secret}") final String secret) {
        final byte[] decodeSecret = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(decodeSecret);
    }

    public boolean validateToken(final String token) {
        try {
            getJwtParser().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new IllegalArgumentException("JWT를 지원하지 않습니다.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("토큰을 찾을 수 없습니다.");
        }
    }

    public String getUsername(final String accessToken) {
        return getJwtParser()
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();
    }

    public String createAccessToken(final String username,
                                    final Collection<? extends GrantedAuthority> authorities) {
        return Jwts.builder()
                .setHeaderParam("alg", SignatureAlgorithm.HS256.getValue())
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(getExpiration())
                .claim("auth", authorities)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Date getExpiration() {
        return new Date(LocalTime.now().getHour() + 1000L * 60 * 60 * 24);
    }

    private JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }
}
