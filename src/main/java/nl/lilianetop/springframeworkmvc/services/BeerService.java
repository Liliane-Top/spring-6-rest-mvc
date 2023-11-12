package nl.lilianetop.springframeworkmvc.services;

import nl.lilianetop.springframeworkmvc.models.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {

    List<Beer> listBeers();

    Beer getBeerById(UUID id);
}
