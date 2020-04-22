package com.gnomon.api.payloads.responses;

import java.time.Instant;

import com.gnomon.api.models.audits.DateAudit;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class DatedResponse<T extends DateAudit> extends MappedResponse<T>{
	private Instant createdAt;

    private Instant updatedAt;
    
    public DatedResponse(T datedObject) { 
    	super(datedObject);
    }
    
    @Override
    protected void mapObjectToResponse(T objectToMap) {
    	this.createdAt = objectToMap.getCreatedAt();
    	this.updatedAt = objectToMap.getUpdatedAt();
    }
}
