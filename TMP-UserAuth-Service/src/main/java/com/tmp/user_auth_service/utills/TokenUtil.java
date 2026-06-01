package com.tmp.user_auth_service.utills;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.tmp.user_auth_service.dto.TmpUserDetail;
import com.tmp.user_auth_service.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtil implements Serializable {

	private static final long serialVersionUID = 680674901313380631L;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

	@Autowired
	private CommonServices commonServices;

	private Date generateExpirationDate() {

		return new Date(System.currentTimeMillis() + (expiration * 1000));
	}

	public String generateToken() {

		final Map<String, Object> claims = new HashMap<>();
		final TmpUserDetail tmpUserDetail = (TmpUserDetail) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		User user = tmpUserDetail.getUser();
		
		return Jwts.builder()
	            .setSubject(user.getId().toString()) // userId as subject
	            .claim("email", user.getEmail())
	            .claim("role", user.getRole())       // role claim
	            .setIssuedAt(new Date())             // iat
	            .setExpiration(generateExpirationDate()) // exp
	            .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, secret)
	            .compact();
	}

	public String generateToken(final Map<String, Object> claims) {

		return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
				.signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, secret).compact();
	}
	
	public String generateToken(User user) {

	    return Jwts.builder()
	            .setSubject(user.getId().toString())
	            .claim("role", user.getRole())
	            .setIssuedAt(new Date())
	            .setExpiration(generateExpirationDate())
	            .signWith(SignatureAlgorithm.HS512, secret)
	            .compact();
	}

	public String getUsernameFromToken(final String token) {
		String username;
		final Claims claims = getClaimsFromToken(token);
		username = claims.getSubject();
//		try {
//			final Claims claims = getClaimsFromToken(token);
//			username = claims.getSubject();
//		} catch (final Exception e) {
//			username = null;
//		}
		return username;
	}

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

	public Boolean validateToken(final String token, final User employee) {

		if (!employee.getIsActive()) {
			throw new LockedException(commonServices.getMessageByCode(ErrorDataEnum.ACCOUNT_LOCK_MESSAGE.getCode()));
		}
		
		return !isTokenExpired(token);
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
}

