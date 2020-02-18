package com.cinema.entities;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

@Projection(name="p1",types= {com.cinema.entities.Projection.class})
public interface ProjectionProj {
public Long getId();
public double getPrix();
public Date getDateProjection();
public Salle getSalle();
public Film getFilm();
public Seance getseance();
public Collection<Ticket> getTickets();


}
