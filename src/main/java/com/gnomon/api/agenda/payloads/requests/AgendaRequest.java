package com.gnomon.api.agenda.payloads.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AgendaRequest {
	@NotBlank
	@Size(min = 3, max = 64)
	private String name;
	
	@Size(max = 256)
	private String description;
	
	private boolean isPublic;

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

	public boolean isPublic() {
		return isPublic;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
}
