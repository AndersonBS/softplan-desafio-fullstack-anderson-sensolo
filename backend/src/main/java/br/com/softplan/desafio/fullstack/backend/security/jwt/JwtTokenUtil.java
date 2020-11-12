package br.com.softplan.desafio.fullstack.backend.security.jwt;

import java.util.Date;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {

	@Value("${jwt.secret}") private String jwtSecret;
	@Value("${jwt.expiration.milliseconds}") private int jwtExpirationMilliseconds;

	private Claims getClaimsFromToken(final String jwtToken) {
		return Jwts.parser().setSigningKey(this.jwtSecret).parseClaimsJws(jwtToken).getBody();
	}

	private boolean isTokenExpired(final String jwtToken) {
		return this.getClaimsFromToken(jwtToken).getExpiration().before(new Date());
	}

	public String getUsernameFromToken(final String jwtToken) {
		return this.getClaimsFromToken(jwtToken).getSubject();
	}

	public boolean validateToken(final String jwtToken, final UserDetails userDetails) {
		return this.getUsernameFromToken(jwtToken).equals(userDetails.getUsername()) && !this.isTokenExpired(jwtToken);
	}

	public String generateJwtToken(final UserDetails userDetails) {
		return Jwts.builder()
				.setClaims(new HashMap<>())
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + this.jwtExpirationMilliseconds))
				.signWith(SignatureAlgorithm.HS512, this.jwtSecret)
				.compact();
	}

}
