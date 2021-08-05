package com.vinodh.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SpJobNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 509351511637285833L;

	public SpJobNotFoundException(Long id) {
        super("SpJob id not found : " + id);
    }
	
	public SpJobNotFoundException() {
        super("SpJob id not found : ");
    }

}
