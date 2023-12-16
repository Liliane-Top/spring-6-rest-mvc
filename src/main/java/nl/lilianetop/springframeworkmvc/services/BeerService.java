package nl.lilianetop.springframeworkmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import nl.lilianetop.springframeworkmvc.models.BeerDto;
import nl.lilianetop.springframeworkmvc.models.BeerStyle;

public interface BeerService {

  List<BeerDto> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory);

  Optional<BeerDto> getBeerById(UUID id);

  BeerDto saveNewBeer(BeerDto beer);

  Optional<BeerDto> updateBeerById(UUID id, BeerDto beer);

  Boolean deleteById(UUID id);

  Optional<BeerDto> patchBeerById(UUID id, BeerDto beer);

}
