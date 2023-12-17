package nl.lilianetop.springframeworkmvc.services;

import java.util.Optional;
import java.util.UUID;
import nl.lilianetop.springframeworkmvc.models.BeerDto;
import nl.lilianetop.springframeworkmvc.models.BeerStyle;
import org.springframework.data.domain.Page;

public interface BeerService {

  Page<BeerDto> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
      Integer pageNumber, Integer pageSize);

  Optional<BeerDto> getBeerById(UUID id);

  BeerDto saveNewBeer(BeerDto beer);

  Optional<BeerDto> updateBeerById(UUID id, BeerDto beer);

  Boolean deleteById(UUID id);

  Optional<BeerDto> patchBeerById(UUID id, BeerDto beer);

}
