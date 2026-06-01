package com.tmp.user_auth_service.utills;

public enum SuccessMsgEnum {
	
	INVALID_TOKEN_MESSAGE("invalid.token.message");
	
	String code;

	private SuccessMsgEnum(final String code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

}
