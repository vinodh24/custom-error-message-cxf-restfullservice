package com.vinodh.exceptionhandling;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.vinodh.dto.Resource;
import com.vinodh.dto.ResponseDTO;
import com.vinodh.util.CustomMessageIntilizer;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public Response toResponse(Throwable ex) {
        //System.out.println("GenericExceptionMapper :::: "+ex);
		Response.StatusType type = getStatusType(ex);
		ResponseDTO error = new ResponseDTO(type.getStatusCode(), type.getReasonPhrase(), ex.getLocalizedMessage());
		return Response.status(error.getResponseCode()).entity(error).type(MediaType.APPLICATION_JSON).build();
	}

	private Response.StatusType getStatusType(Throwable ex) {
		if (ex instanceof SpJobNotFoundException) {
			//System.out.println("SpJobNotFoundException :::: "+ex);
			return Response.Status.NOT_FOUND;
		}
		else if (ex instanceof WebApplicationException) {
			return ((WebApplicationException) ex).getResponse().getStatusInfo();
		} else {
			return Response.Status.INTERNAL_SERVER_ERROR;
		}
	}

	public static ResponseEntity<ResponseDTO> getErrorResource(HttpStatus internalServerError, String message,
			HttpServletRequest request) {
		Resource errorMessage = null;
		ResponseDTO response = new ResponseDTO();
		for (Entry<String, Resource> entry : CustomMessageIntilizer.resourceBundleMap.entrySet()) {
			if (message.contains(entry.getKey())) {
				errorMessage = entry.getValue();
				break;
			}
		}

		if (null != errorMessage) {
			response.setErrorCode(errorMessage.getId());
			response.setResponseMessage(errorMessage.getValue());
			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setTimstap(dateFormat.format(new Date()));
			response.setRequestURI(request.getRequestURI());
			return new ResponseEntity<ResponseDTO>(response, internalServerError);
		} else {
			return getErrorResponse(internalServerError, message, request);
		}
	}

	public static ResponseEntity<ResponseDTO> getErrorResponse(HttpStatus internalServerError, String message,
			HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setResponseCode(internalServerError.value());
		responseDTO.setTimstap(dateFormat.format(new Date()));
		responseDTO.setResponseMessage(message);
		responseDTO.setErrorCode(internalServerError + "");
		responseDTO.setRequestURI(request.getRequestURI());
		return new ResponseEntity<ResponseDTO>(responseDTO, internalServerError);
	}

}
