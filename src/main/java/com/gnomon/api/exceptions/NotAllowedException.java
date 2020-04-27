package com.gnomon.api.exceptions;

public class NotAllowedException extends RuntimeException {
	
	public NotAllowedException(String message) {
		super("Not allowed : " + message);
	}
}
