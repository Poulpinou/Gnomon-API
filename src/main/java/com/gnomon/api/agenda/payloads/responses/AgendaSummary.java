package com.gnomon.api.agenda.payloads.responses;

import com.gnomon.api.agenda.models.Agenda;
import com.gnomon.api.payloads.responses.UserDatedResponse;

public class AgendaSummary extends UserDatedResponse<Agenda> {
	
	private Long id;
	private String name;
	private String description;
	private Boolean isPublic;
	
	public AgendaSummary(Agenda agenda) {
		super(agenda);
	}
	
	@Override
	protected void MapObjectToResponse(Agenda agenda) {
		super.MapObjectToResponse(agenda);
		
		this.id = agenda.getId();
		this.name = agenda.getName();
		this.description = agenda.getDescription();
		this.isPublic = agenda.isPublic();
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
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Boolean isPublic() {
		return isPublic;
	}
	
	public void setPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
}
