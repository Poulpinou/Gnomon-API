package com.gnomon.api.payloads.responses;

public abstract class MappedResponse<T> {
	public MappedResponse() {}
	
	public MappedResponse(T objectToMap) {
		MapObjectToResponse(objectToMap);
	}
	
	protected abstract void MapObjectToResponse(T objectToMap);
}
