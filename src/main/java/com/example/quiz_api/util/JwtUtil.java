package com.example.quiz_api.util;

import com.example.quiz_api.config.JwtConfig;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;             // Ojo: import correcto
import io.jsonwebtoken.security.Keys;          // Ojo: import correcto
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Component
public class JwtUtil {

    private JwtConfig jwtConfig;
//    private final Key key;

    @Autowired
    public JwtUtil(JwtConfig jwtConfig) {

        this.jwtConfig = jwtConfig;
//        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()));
    }

    // Generar Token
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration());

        // 2) Depende de cómo este configurada la 'secret' en tu `application.properties`
//        Key signingKey = getSigningKey(jwtConfig.getSecret());
        Key key = getSigningKey(jwtConfig.getSecret());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
//                .signWith(key)  // Cambio importante aquí
//                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
                .compact();
    }

    // Obtener Username del Token
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey(jwtConfig.getSecret()))
//                .setSigningKey(key)  // Usando la key inicializada
                .build()
//                .setSigningKey(jwtConfig.getSecret())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // Validar Token
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey(jwtConfig.getSecret()))
//                    .setSigningKey(jwtConfig.getSecret())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SecurityException | SignatureException ex) {
            System.out.println("Invalid JWT signature: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty: " + ex.getMessage());
        }
        return false;
    }

    /**
     *
     * Decodifica la 'secret' en Base64 y crea una Key (HMAC).
     * o texto plano
     */
//    private Key getSigningKey(String secret) {
//        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

    /**
     * en application.properties para Base64
     * jwt.secret=TWlDbGF2ZVNlY3JldG9NeVN1Z2VyYTEyMw==
     * jwt.expiration=86400000
     */

    private Key getSigningKey(String base64Secret) {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
