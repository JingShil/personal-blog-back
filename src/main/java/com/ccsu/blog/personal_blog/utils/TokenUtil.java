package com.ccsu.blog.personal_blog.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
public class TokenUtil {


    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 1天的过期时间
    private static String base64Encoded;
    public static String generateToken(String username) {
        byte[] secretBytes = key.getEncoded();
        base64Encoded = Base64.getEncoder().encodeToString(secretBytes);

        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, base64Encoded)
                .compact();
    }
    public static String extractSubjectFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(base64Encoded).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(base64Encoded).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
