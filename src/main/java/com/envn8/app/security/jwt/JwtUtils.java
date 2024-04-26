package com.envn8.app.security.jwt;

import java.security.Key;
import java.util.Date;
// import javax.servlet.http.Cookie;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
// import org.springframework.web.util.WebUtils;
import org.springframework.web.util.WebUtils;

import com.envn8.app.service.UserDetailsImpl;
import com.envn8.app.security.jwt.JwtUtils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${TextEditor.app.jwtSecret}")
  private String jwtSecret;

  @Value("${TextEditor.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${TextEditor.app.jwtCookieName}")
  private String jwtCookie;

  // public String getJwtFromCookies(HttpServletRequest request) {
  // Cookie cookie = WebUtils.getCookie(request, jwtCookie);
  // if (cookie != null) {
  // return cookie.getValue();
  // } else {
  // return null;
  // }
  // }

  // public String getJwtFromCookies(HttpServletRequest request) {
  // Cookie[] cookies = request.getCookies();
  // if (cookies != null) {
  // for (Cookie cookie : cookies) {
  // if (cookie.getName().equals(jwtCookie)) {
  // return cookie.getValue();
  // }
  // }
  // }
  // return null;
  // }

  public String getJwtFromCookies(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, jwtCookie);
    System.out.println("in getJwtFromCookies cookie: " + cookie);
    if (cookie != null) {
      return cookie.getValue();
    } else {
      return null;
    }
  }

  public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
    String jwt = generateTokenFromUsername(userPrincipal.getUsername());
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true)
        .build();
    return cookie;
  }

  public ResponseCookie getCleanJwtCookie() {
    // ResponseCookie cookie = ResponseCookie.from(jwtCookie,
    // null).path("/api").build();
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, "").path("/api").build();
    return cookie;
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
        .parseClaimsJws(token).getBody().getSubject();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

  public String generateTokenFromUsername(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }
}
