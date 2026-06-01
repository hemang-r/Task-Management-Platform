package com.tmp.user_auth_service.dto;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tmp.user_auth_service.entity.User;

import lombok.Getter;

@Getter
public class TmpUserDetail implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final User user;

	public TmpUserDetail(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(
                new SimpleGrantedAuthority( "ROLE_" + user.getRole()) );
	}

	@Override
	public String getPassword() {

		return user.getPasswordHash();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	
	/**
	 * The <code>getLoggedInUserId</code> is used to get the user id of the logged in user.
	 * @return
	 */
	public UUID getLoggedInUserId() {
		return user.getId();
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}