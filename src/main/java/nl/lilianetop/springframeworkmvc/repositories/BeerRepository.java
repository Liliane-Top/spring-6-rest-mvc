package nl.lilianetop.springframeworkmvc.repositories;

import java.util.UUID;
import nl.lilianetop.springframeworkmvc.domain.Beer;
import nl.lilianetop.springframeworkmvc.models.BeerStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID> {

  Page<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName, Pageable pageable);
  Page<Beer> findAllByBeerStyle(BeerStyle beerStyle, Pageable pageable);
  Page<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle,
      Pageable pageable);

}
