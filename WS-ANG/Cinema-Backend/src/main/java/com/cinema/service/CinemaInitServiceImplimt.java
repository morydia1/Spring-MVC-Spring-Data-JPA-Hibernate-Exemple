package com.cinema.service;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cinema.dao.CategorieRepository;
import com.cinema.dao.CinemaRepository;
import com.cinema.dao.FilmRepository;
import com.cinema.dao.PlaceRepository;
import com.cinema.dao.ProjectionRepository;
import com.cinema.dao.SalleRepository;
import com.cinema.dao.SeanceRepository;
import com.cinema.dao.TicketRepository;
import com.cinema.dao.VilleRepository;
import com.cinema.entities.Categorie;
import com.cinema.entities.Cinema;
import com.cinema.entities.Film;
import com.cinema.entities.Place;
import com.cinema.entities.Projection;
import com.cinema.entities.Salle;
import com.cinema.entities.Seance;
import com.cinema.entities.Ticket;
import com.cinema.entities.Ville;


@Service
@Transactional
public class CinemaInitServiceImplimt implements ICinemaInitService {
    @Autowired
	private VilleRepository villeRepository;    
    @Autowired
	private CinemaRepository cinemaRepository;    
    @Autowired
	private SalleRepository salleRepository;   
    @Autowired
	private PlaceRepository placeRepository;
    @Autowired
	private SeanceRepository seanceRepository;
    @Autowired
	private FilmRepository filmRepository;
    @Autowired
	private ProjectionRepository projectionRepository;
    @Autowired
	private CategorieRepository categorieRepository;
    @Autowired
   	private TicketRepository ticketRepository;
	
	@Override
	public void initVilles() {
		Stream.of("Casablanca","Marrakech","Rabat","Tanger").forEach(nameVille->{
			Ville ville=new Ville();
			ville.setName(nameVille);
			villeRepository.save(ville);
		});
		
	}

	@Override
	public void initCinema() {
		villeRepository.findAll().forEach(v->{
			Stream.of("MegaRama","Imax","Pathé","Chahrazad","ForYou")
			.forEach(nameCinema->{
				Cinema cinema=new Cinema();
				cinema.setName(nameCinema);
				//définir le nombre de salle dans chaque cinema d'une manière aleatoire entre 3 et 10 
				cinema.setNombreSalles(3);
				cinema.setVille(v);
				cinemaRepository.save(cinema);
			});
		});
		
	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema->{
			for(int i=0;i<cinema.getNombreSalles();i++) {
				Salle salle=new Salle();
				salle.setName("Salle"+(i+1));
				salle.setCinema(cinema);
				salle.setNombrePlace(15);
				salleRepository.save(salle);
			}
		});
		
	}

	@Override
	public void initPlaces() {
		salleRepository.findAll().forEach(salle->{
			for(int i=0;i<salle.getNombrePlace();i++) {
				Place place=new Place();
				place.setNumero(i+1);
				place.setSalle(salle);
				placeRepository.save(place);
				
			}
		});
		
	}

	@Override
	public void initSeances() {
		DateFormat dateFormat=new SimpleDateFormat("HH:mm");
		Stream.of("12:15","15:30","19:00","21:00").forEach(s->{
			Seance seance=new Seance();
			try {
				seance.setHeureDebut(dateFormat.parse(s));
				seanceRepository.save(seance);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
	}

	@Override
	public void initCategorie() {
		Stream.of("Historique","Actions","Fiction","Drama").forEach(cat->{
			Categorie categorie=new Categorie();
			categorie.setName(cat);
			categorieRepository.save(categorie);
		});
		
	}

	@Override
	public void initFilms() {
		double[] durees =new double[] {1,1.5,2,2.5,3};   
		List<Categorie> categories=categorieRepository.findAll();
		Stream.of("Jurracic World","Doctor Strange","Ducobu","Champagne","Yakuza","Black Widow","Free Guy",
				"Avatar","Fast Furious","Film").forEach(TitreFilm->{
		Film film=new Film();
		film.setTitre(TitreFilm);
		film.setDuree(durees[new Random().nextInt(durees.length)]);
		film.setPhoto(TitreFilm.replace(" ","_")+".jpeg");
		film.setCategorie(categories.get(new Random().nextInt(categories.size())));
		filmRepository.save(film);
		
		});
	}

	@Override
	public void initProjections() {
		double[] prices=new double[] {30.0,50,60,70.56,90,100};
		List<Film> films=filmRepository.findAll();
		villeRepository.findAll().forEach(ville->{
			ville.getCinemas().forEach(cinema->{
				cinema.getSalles().forEach(salle->{
					int index=new Random().nextInt(films.size());
					Film film=films.get(index);
						seanceRepository.findAll().forEach(seance->{
							Projection projection=new Projection();
							projection.setDateProjection(new Date());
							projection.setFilm(film);
							projection.setPrix(prices[new Random().nextInt(prices.length)]);
							projection.setSalle(salle);
							projection.setSeance(seance);
							projectionRepository.save(projection);
							
						});
					
				});
			});
		});
		
	}

	@Override
	public void initTickets() {
		projectionRepository.findAll().forEach(p->{
			p.getSalle().getPlaces().forEach(place->{
				Ticket ticket =new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjection(p);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
				
			});
				
			
		});
		
	}

}
