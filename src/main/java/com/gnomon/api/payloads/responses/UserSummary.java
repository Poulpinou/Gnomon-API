package com.gnomon.api.payloads.responses;

import com.gnomon.api.models.User;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserSummary extends DatedResponse<User> {
	
	Long id;
	
	String name;
	
	String email;
	
	public UserSummary(User user) {
		super(user);		
	}
	
	@Override
	protected void mapObjectToResponse(User user) {
		super.mapObjectToResponse(user);
		
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
	}
}
