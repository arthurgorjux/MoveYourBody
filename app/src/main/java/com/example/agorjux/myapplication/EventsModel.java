package com.example.agorjux.myapplication;

/**
 * Created by arthur on 21/08/15.
 */
public class EventsModel {
    int id;
    String title;
    String description;
    String dateEvent;
    long latitude;
    long longitude;
    String sport;
    int people;
    int idUser;

    public EventsModel(int id, String title, String description, String dateEvent, long latitude, long longitude, String sport, int people, int idUser){
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateEvent = dateEvent;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sport = sport;
        this.people = people;
        this.idUser = idUser;
    }
}
