package nl.lilianetop.springframeworkmvc.services;

import nl.lilianetop.springframeworkmvc.models.BeerDTO;
import nl.lilianetop.springframeworkmvc.models.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
                            Integer pageNumber, Integer pageSize);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeerById(UUID id, BeerDTO beer);

    Boolean deleteById(UUID id);

    Optional<BeerDTO> patchBeerById(UUID id, BeerDTO beer);

}
