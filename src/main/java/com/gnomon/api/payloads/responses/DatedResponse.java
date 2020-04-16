package com.gnomon.api.payloads.responses;

import java.time.Instant;

import com.gnomon.api.models.audits.DateAudit;

public abstract class DatedResponse<T extends DateAudit> extends MappedResponse<T>{
	private Instant createdAt;

    private Instant updatedAt;
    
    public DatedResponse(T datedObject) { 
    	super(datedObject);
    }
    
    @Override
    protected void MapObjectToResponse(T objectToMap) {
    	this.createdAt = objectToMap.getCreatedAt();
    	this.updatedAt = objectToMap.getUpdatedAt();
    }

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
}
