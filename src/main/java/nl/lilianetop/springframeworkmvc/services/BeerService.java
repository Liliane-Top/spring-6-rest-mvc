package nl.lilianetop.springframeworkmvc.services;

import nl.lilianetop.springframeworkmvc.models.BeerDto;

import java.util.List;
import java.util.UUID;

public interface BeerService {

    List<BeerDto> listBeers();

    BeerDto getBeerById(UUID id);
}
