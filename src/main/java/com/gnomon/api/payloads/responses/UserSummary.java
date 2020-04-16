package com.gnomon.api.payloads.responses;

import com.gnomon.api.models.User;

public class UserSummary extends DatedResponse<User> {
	Long id;
	String name;
	String email;
	
	public UserSummary(User user) {
		super(user);
		
	}
	
	@Override
	protected void MapObjectToResponse(User user) {
		super.MapObjectToResponse(user);
		
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
}
