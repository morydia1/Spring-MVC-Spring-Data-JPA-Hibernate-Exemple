import { NgForm } from '@angular/forms';
import { CinemaService } from '../services/cinema.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-cinema',
  templateUrl: './cinema.component.html',
  styleUrls: ['./cinema.component.css']
})
export class CinemaComponent implements OnInit {

  public villes:any;
  public currentVille: any;
  public cinemas: any;
  public currentCinema: any;
  public salles: any;
  public currentProjection: any;
  public selectedTickets : any;

  constructor(public cinemaService:CinemaService) { }

  ngOnInit(): void {
    this.cinemaService.getVilles()
      .subscribe(res => {
        this.villes = res;
      },error =>{
        console.log(error);
      })
  }

  onGetCinemas(v:any){
    this.currentVille= v;
    this.salles = undefined;
    this.cinemaService.getCinemas(v)
      .subscribe(res => {
        this.cinemas = res;

      },error =>{
        console.log(error);
      })
  }

  onGetSalles(c:any){
    this.currentCinema= c;
    this.currentProjection = undefined;
    this.cinemaService.getSalles(c)
      .subscribe(res => {
        this.salles = res;
        this.salles._embedded.salles.forEach((salle:any) => {
          this.cinemaService.getProjections(salle)
            .subscribe(projections => {
              salle.projections = projections;
            },errors => {
              console.log(errors);
            })
        })
      },error =>{
        console.log(error);
      })
  }

  public getTicketsPlace(p:any){
    this.currentProjection = p;
    this.cinemaService.getTickets(p)
      .subscribe(tickets =>{
        this.currentProjection.tickets = tickets;
        this.selectedTickets = [];
      },error =>{
        console.log(error);
      })
  }

  public onSelectTicket(t:any){
    if(!t.selected){
      t.selected = true;
      this.selectedTickets.push(t);
    }else{
      t.selected = false;
      this.selectedTickets.splice(this.selectedTickets.indexOf(t),1);
    }

  }

  public getTicketClass(t:any){
    let str = "btn ticket ";
    if(t.reserve==true){
      str+= "btn-danger";
    }else if(t.selected==true){
      str+= "btn-warning";
    }else
      str+= "btn-success";

    return str;
  }

  onPayTickets(f:any){
    let tickets: any = [];
    this.selectedTickets.forEach((t:any)  => {
      tickets.push(t.id);
    });
    f.tickets = tickets;
    console.log(f);
    this.cinemaService.payTickets(f)
      .subscribe(res=>{
        alert('Ticket reserved successfully');
        this.getTicketsPlace(this.currentProjection);
      },err=>{
        console.log(err);
      })
  }

}
