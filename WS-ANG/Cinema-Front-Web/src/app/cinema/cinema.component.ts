import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CinemaService} from '../service/cinema.service';

@Component({
  selector: 'app-cinema',
  templateUrl: './cinema.component.html',
  styleUrls: ['./cinema.component.css']
})
export class CinemaComponent implements OnInit {

  public villes;
  public cinemas;
  public currentVille;
  public currentCinema;
  private salles;
  private currentProjection;
  private selectedTickets: any;

  constructor(public cinemaService: CinemaService) {
  }

  ngOnInit() {
    this.cinemaService.getVilles()
      .subscribe(data => {
        this.villes = data;
      }, err => {
        console.log(err);
      });
  }

  onGetCinema(v) {
    this.currentVille = v;
    this.salles = undefined;
    this.cinemaService.getCinemas(v)
      .subscribe(data => {
        this.cinemas = data;
      }, err => {
        console.log(err);
      });
  }

  onGetSalles(c) {
    this.currentCinema = c;
    this.cinemaService.getSalles(c)
      .subscribe(data => {
        this.salles = data;
        this.salles._embedded.salles.forEach(salle => {
          this.cinemaService.getProjections(salle)
            // tslint:disable-next-line:no-shadowed-variable
            .subscribe(data => {
              salle.projections = data;
            }, err => {
              console.log(err);
            });
        });
      }, err => {
        console.log(err);
      });
  }

  onGetTicketsPlaces(p) {
    this.currentProjection = p;
    this.cinemaService.getTicketsPlaces(p)
      .subscribe(data => {
      this.currentProjection.tickets = data;
      this.selectedTickets = [];
      }, err => {
        console.log(err);
    });
  }

  onSelectTicket(t) {
  t.selected = true;
  this.selectedTickets.push(t);
  }

  getTicketClass(t) {
 let str = 'btn ticket';
 if (t.reserve === true) {
    str += 'btn-danger';
  } else if (t.selected) {
    str += 'btn-warning';
  } else {
   str += 'btn-success';
 }
 return str;
  }
}

