package nl.lilianetop.springframeworkmvc.repositories;

import java.util.List;
import java.util.UUID;
import nl.lilianetop.springframeworkmvc.domain.Beer;
import nl.lilianetop.springframeworkmvc.models.BeerStyle;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID> {

  List<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName);
  List<Beer> findAllByBeerStyle(BeerStyle beerStyle);
  List<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle);

}
