package com.tmp.user_auth_service.utills;

import lombok.Getter;

@Getter
public enum ErrorDataEnum {
	
	ACCOUNT_LOCK_MESSAGE("account.lock.message"),
	 USER_HAVE_LOGGED_OUT("user.have.logged.out"), X_APIKEY_MISSING("api.key.missing"),
	 INVALID_TOKEN_MESSAGE("invalid.token.message");
	 ;
	
	private String code;

	private ErrorDataEnum(final String code) {
		this.code = code;
	}

}