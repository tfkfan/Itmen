package com.itmencompany.Exceptions;

public class UserAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserAlreadyExistsException(String arg){
		super(arg);
	}
}
