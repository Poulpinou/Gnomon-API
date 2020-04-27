package com.gnomon.api.utils.editors;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;

public class LocalDateEditor extends PropertyEditorSupport {
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if ("today".equals(text)) {
            setValue(LocalDate.now());
        }else if("endOfLife".equals(text)) {
        	setValue(LocalDate.now().plusYears(100));
        } else {
        	try {
        		setValue(LocalDate.parse(text));
        	}catch(Exception e) {
        		throw new IllegalArgumentException(text + " is not a valid date format");
        	}
        }
	}
}
