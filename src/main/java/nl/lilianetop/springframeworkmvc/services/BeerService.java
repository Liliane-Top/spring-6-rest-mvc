package nl.lilianetop.springframeworkmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import nl.lilianetop.springframeworkmvc.models.BeerDto;
import org.springframework.stereotype.Service;

public interface BeerService {

  List<BeerDto> listBeers();

  Optional<BeerDto> getBeerById(UUID id);

  BeerDto saveNewBeer(BeerDto beer);

  Optional<BeerDto> updateBeerById(UUID id, BeerDto beer);

  Boolean deleteById(UUID id);

  Optional<BeerDto> patchBeerById(UUID id, BeerDto beer);

}
