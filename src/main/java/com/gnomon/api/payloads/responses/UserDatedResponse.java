package com.gnomon.api.payloads.responses;

import com.gnomon.api.models.audits.UserDateAudit;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class UserDatedResponse<T extends UserDateAudit> extends DatedResponse<T> {

	private Long createdBy;
	
    private Long updatedBy;	

    public UserDatedResponse(T userDatedObject) {
		super(userDatedObject);
	}
    
	@Override
	protected void mapObjectToResponse(T objectToMap) {		
		super.mapObjectToResponse(objectToMap);
		this.createdBy = objectToMap.getCreatedBy();
		this.updatedBy = objectToMap.getUpdatedBy();
	}
}
