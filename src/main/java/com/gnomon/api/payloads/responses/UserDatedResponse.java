package com.gnomon.api.payloads.responses;

import com.gnomon.api.models.audits.UserDateAudit;

public abstract class UserDatedResponse<T extends UserDateAudit> extends DatedResponse<T> {

	private Long createdBy;
	
    private Long updatedBy;	

	@Override
	protected void MapObjectToResponse(T objectToMap) {		
		super.MapObjectToResponse(objectToMap);
		this.createdBy = objectToMap.getCreatedBy();
		this.updatedBy = objectToMap.getUpdatedBy();
	}
	
	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public UserDatedResponse(T userDatedObject) {
		super(userDatedObject);
	}
}
