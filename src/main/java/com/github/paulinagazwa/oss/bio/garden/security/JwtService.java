package com.github.paulinagazwa.oss.bio.garden.security;

import com.github.paulinagazwa.oss.bio.garden.logging.LogMessages;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {

	@Value("${security.jwt.secret}")
	private String secret;

	@Value("${security.jwt.access-token-expiration}")
	private long accessTokenExpiration;

	@Value("${security.jwt.refresh-token-expiration}")
	private long refreshTokenExpiration;

	public String generateAccessToken(UserDetails userDetails) {
		return buildToken(userDetails.getUsername(), accessTokenExpiration);
	}

	public String generateRefreshToken(UserDetails userDetails) {
		return buildToken(userDetails.getUsername(), refreshTokenExpiration);
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		try {
			String username = extractUsername(token);
			return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
		} catch (JwtException | IllegalArgumentException e) {
			log.warn(LogMessages.JWT_VALIDATION_FAILED, e.getMessage());
			return false;
		}
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return claimsResolver.apply(claims);
	}

	private String buildToken(String subject, long expiration) {
		long now = System.currentTimeMillis();
		return Jwts.builder()
				.subject(subject)
				.issuedAt(new Date(now))
				.expiration(new Date(now + expiration))
				.signWith(getSigningKey())
				.compact();
	}

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}
}
