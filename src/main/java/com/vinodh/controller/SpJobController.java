package com.vinodh.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import com.vinodh.dto.JobDTO;
import com.vinodh.dto.TaskDTO;
import com.vinodh.entity.SpJob;
import com.vinodh.exceptionhandling.SpJobNotFoundException;
import com.vinodh.repository.SpJobRepository;
import com.vinodh.repository.SpTaskRepository;
import com.vinodh.service.GenericService;

import oracle.jdbc.proxy.annotation.Post;

@Path("/spJob")
public class SpJobController {

	@Autowired
	private SpJobRepository spJobRepository;

	@Autowired
	private SpTaskRepository spTaskRepository;

	@GET@Path("/spjob")
	@Consumes(MediaType.APPLICATION_JSON)@Produces(MediaType.APPLICATION_JSON)
	public Response findAll() {
		Response response = null;
		System.out.println("vinodh findAll");
		List<JobDTO> jobDTOs =GenericService.getResponseForJob(spJobRepository.findAll());
		response = Response.status(200).entity(jobDTOs).build();
		return response;
	}
	@GET
	@Path("/sptask")@Consumes(MediaType.APPLICATION_JSON)@Produces(MediaType.APPLICATION_JSON)
	public List<TaskDTO> findAllSpTask() {
		return GenericService.getResponseForSpTask(spTaskRepository.findAll());
	}

	@Path("/spjob")
	@Post@Consumes(MediaType.APPLICATION_JSON)@Produces(MediaType.APPLICATION_JSON)
	public SpJob newBook(@RequestBody SpJob alarm) {
		return spJobRepository.save(alarm);
	}

	@GET @Path( "/spjob/{id}")@Consumes(MediaType.APPLICATION_JSON)@Produces(MediaType.APPLICATION_JSON)
	public JobDTO findOne(@PathParam("id") Long id) {
		System.out.println("findOne id   ::::   "+id);
		return GenericService.getDTOFromSpJob(spJobRepository.findById(id).orElseThrow(() 
				-> new SpJobNotFoundException(id)));
	}

	@GET@Path("/sptask/{id}")@Consumes(MediaType.APPLICATION_JSON)@Produces(MediaType.APPLICATION_JSON)
	public TaskDTO findOneBySpTask(@PathParam("id") Long id) {
		return GenericService.getDTOFromSpTask(spTaskRepository.findById(id).get());
	}

	@PUT @Path("/spjob/{id}")@Consumes(MediaType.APPLICATION_JSON)@Produces(MediaType.APPLICATION_JSON)
	public SpJob saveOrUpdate(@RequestBody SpJob newBook, @PathParam("id") Long id) {
		return spJobRepository.save(newBook);
	}

	@DELETE @Path("/spjob/{id}")@Consumes(MediaType.APPLICATION_JSON)@Produces(MediaType.APPLICATION_JSON)
	public void deleteBook(@PathParam("id") Long id) {
		spJobRepository.deleteById(id);
	}	

}
