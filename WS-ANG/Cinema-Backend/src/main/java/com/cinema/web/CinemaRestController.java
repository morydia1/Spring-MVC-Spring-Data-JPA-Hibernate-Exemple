package com.cinema.web;

import java.awt.PageAttributes.MediaType;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cinema.dao.FilmRepository;
import com.cinema.dao.TicketRepository;
import com.cinema.entities.Film;
import com.cinema.entities.Ticket;

import lombok.Data;


@RestController
public class CinemaRestController {
	@Autowired
	private FilmRepository FilmRepository;
	@Autowired
	private TicketRepository ticketRepository;
	
	@GetMapping(path="/imageFilms/{id}",produces=org.springframework.http.MediaType.IMAGE_JPEG_VALUE)
	public byte[] image(@PathVariable(name="id")Long id) throws Exception{
		Film f=FilmRepository.findById(id).get();
		String photoName=f.getPhoto();
		File file=new File(System.getProperty("user.home")+"/"+photoName);
		Path path =Paths.get(file.toURI());
				return Files.readAllBytes(path);
	}
	@PostMapping("/payerTickets")
	@Transactional
public List<Ticket> payerTickets(@RequestBody TicketForm ticketForm){
		List<Ticket> listTicket =new ArrayList<>();
		
	    ticketForm.getTickets().forEach(id->{
		Ticket ticket =ticketRepository.findById(id).get();
		ticket.setNomClient(ticketForm.getNomClient());
		ticket.setReserve(true);
		ticketRepository.save(ticket);
		listTicket.add(ticket);		
		
	});
	    return listTicket;
}
	}
@Data 
class TicketForm{
	private String nomClient;
	private List<Long> tickets=new ArrayList<>();
}
