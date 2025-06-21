package com.mycompany.photographer.resources;

import io.jsonwebtoken.*;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "99788380555sws"; // Keep this secure

    public static String generateToken(int id, String username,String email, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("email", email)
                .claim("id", id)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hour
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getRole(String token) {
        return validateToken(token).get("role", String.class);
    }

    public static int getId(String token) {
        return validateToken(token).get("id", Integer.class);
    }

    public static String getUsername(String token) {
        return validateToken(token).getSubject();
    }
}
