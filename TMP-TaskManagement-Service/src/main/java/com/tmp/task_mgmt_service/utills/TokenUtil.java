package com.tmp.task_mgmt_service.utills;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class TokenUtil 
{
	
	 @Value("${jwt.secret}")
	    private String secret;

	    @Value("${jwt.expiration}")
	    private Long expiration;

		@Autowired
		private CommonServices commonServices;
	
	private Claims getClaimsFromToken(final String token) {

		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (final Exception e) {
			claims = null;
			System.err.println(e);
			throw new JwtException(commonServices.getMessageByCode(ErrorDataEnum.USER_HAVE_LOGGED_OUT.getCode()));
		}
		return claims;
	}
	
	public boolean validateToken(String token) {

        return !isTokenExpired(token);
    }

	private Boolean isTokenExpired(final String token) {

		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public Date getExpirationDateFromToken(final String token) {

		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (final Exception e) {
			expiration = null;
		}
		return expiration;
	}
	
	public String getUserIdFromToken(String token) {
	    Claims claims = getClaimsFromToken(token);
	    return claims.getSubject();
	}
	
	public String getRoleFromToken(String token) {
	    Claims claims = getClaimsFromToken(token);
	    return claims.get("role", String.class);
	}
	
	public String getEmailFromToken(String token) {
	    Claims claims = getClaimsFromToken(token);
	    return claims.get("email", String.class);
	}

}
