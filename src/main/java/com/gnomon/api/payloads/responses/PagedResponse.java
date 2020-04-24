package com.gnomon.api.payloads.responses;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagedResponse<T> {
	private List<T> content;
	
    private int page;
    
    private int size;
    
    private long totalElements;
    
    private int totalPages;
    
    private boolean last;
    
    public static <T> PagedResponse<T> emptyFromPage(Page<T> page){
    	return new PagedResponse<T>(
        		Collections.emptyList(), 
        		page.getNumber(),
        		page.getSize(), 
        		page.getTotalElements(), 
        		page.getTotalPages(), 
        		page.isLast()
    		);
    }
    
    public static <T> PagedResponse<T> fromPage(Page<T> page){
    	return new PagedResponse<T>(
    			page.getContent(), 
        		page.getNumber(),
        		page.getSize(), 
        		page.getTotalElements(), 
        		page.getTotalPages(), 
        		page.isLast()
    		);
    }
}
