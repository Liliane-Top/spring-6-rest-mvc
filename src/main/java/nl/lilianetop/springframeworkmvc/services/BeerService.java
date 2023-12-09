package nl.lilianetop.springframeworkmvc.services;

import nl.lilianetop.springframeworkmvc.domain.Beer;
import nl.lilianetop.springframeworkmvc.models.BeerDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    List<BeerDto> listBeers();

    Optional<BeerDto> getBeerById(UUID id);
    BeerDto saveNewBeer(BeerDto beer);
    BeerDto updateBeerById(UUID id);
    void deleteById(UUID id);
    void patchBeerById(UUID id, BeerDto beer);

}
