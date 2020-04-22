package com.gnomon.api.payloads.responses;

public abstract class MappedResponse<T> {
	
	public MappedResponse(T objectToMap) {
		mapObjectToResponse(objectToMap);
	}
	
	protected abstract void mapObjectToResponse(T objectToMap);
}
