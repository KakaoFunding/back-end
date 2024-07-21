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
import org.kakaoshare.backend.jwt.exception.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import static org.kakaoshare.backend.jwt.exception.JwtErrorCode.*;

@Component
public class JwtProvider {
    private static final String ALGORITHM_HEADER_KEY = "alg";
    private static final String TYPE_HEADER_KEY = "typ";
    private static final String TYPE_HEADER_VALUE = "JWT";
    private static final String CLAIM_AUTH_KEY = "auth";

    private final long expireTime;
    private final Key key;

    public JwtProvider(@Value("${spring.jwt.secret}") final String secret,
                       @Value("${security.token.access.expire-time}") final long expireTime) {
        final byte[] decodeSecret = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(decodeSecret);
        this.expireTime = expireTime;
    }

    public void validateToken(final String token) {
        try {
            getJwtParser().parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtException(INVALID);
        } catch (ExpiredJwtException e) {
            throw new JwtException(EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new JwtException(UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            throw new JwtException(NOT_FOUND);
        }
    }

    public String getUsername(final String accessToken) {
        return getJwtParser()
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();
    }

    public String createAccessToken(final UserDetails userDetails) {
        return Jwts.builder()
                .setHeaderParam(ALGORITHM_HEADER_KEY, SignatureAlgorithm.HS256.getValue())
                .setHeaderParam(TYPE_HEADER_KEY, TYPE_HEADER_VALUE)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(getExpiration())
                .claim(CLAIM_AUTH_KEY, userDetails.getAuthorities())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Date getExpiration() {
        final Date now = new Date();
        return new Date(now.getTime() + expireTime);
    }

    private JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }
}
