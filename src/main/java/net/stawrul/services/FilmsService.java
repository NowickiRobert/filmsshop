package net.stawrul.services;

import net.stawrul.model.Film;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Komponent (serwis) biznesowy do realizacji operacji na książkach.
 */
@Service
public class FilmsService extends EntityService<Film> {

    //Instancja klasy EntityManger zostanie dostarczona przez framework Spring
    //(wstrzykiwanie zależności przez konstruktor).
    public FilmsService(EntityManager em) {

        //Film.class - klasa encyjna, na której będą wykonywane operacje
        //Film::getId - metoda klasy encyjnej do pobierania klucza głównego
        super(em, Film.class, Film::getId);
    }

    /**
     * Pobranie wszystkich książek z bazy danych.
     *
     * @return lista książek
     */
    public List<Film> findAll() {
        //pobranie listy wszystkich książek za pomocą zapytania nazwanego (ang. named query)
        //zapytanie jest zdefiniowane w klasie Book
        return em.createNamedQuery(Film.FIND_ALL, Film.class).getResultList();
    }

}
