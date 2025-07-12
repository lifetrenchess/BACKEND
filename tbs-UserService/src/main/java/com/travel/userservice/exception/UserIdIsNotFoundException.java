package com.travel.userservice.exception;

public class UserIdIsNotFoundException extends RuntimeException {
	public UserIdIsNotFoundException(String msg) {
		super(msg);
	}

}
