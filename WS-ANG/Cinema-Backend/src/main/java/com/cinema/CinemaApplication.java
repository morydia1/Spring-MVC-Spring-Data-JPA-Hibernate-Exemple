package com.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import com.cinema.entities.Film;
import com.cinema.entities.Salle;
import com.cinema.service.ICinemaInitService;

@SpringBootApplication
public class CinemaApplication implements CommandLineRunner{
    @Autowired
    private ICinemaInitService cinemaInitService;
    //pour integrer le ID dans les résultats
    @Autowired
    private RepositoryRestConfiguration restConfiguration;
	
	public static void main(String[] args) {
		SpringApplication.run(CinemaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		restConfiguration.exposeIdsFor(Film.class,Salle.class);
		
		cinemaInitService.initVilles();
		cinemaInitService.initCinema();
		cinemaInitService.initSalles();
		cinemaInitService.initPlaces();
		cinemaInitService.initSeances();
		cinemaInitService.initCategorie();
		cinemaInitService.initFilms();
		cinemaInitService.initProjections();
		cinemaInitService.initTickets();
		
	}

}
