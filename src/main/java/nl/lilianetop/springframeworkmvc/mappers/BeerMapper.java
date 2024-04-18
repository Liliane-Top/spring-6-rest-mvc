package nl.lilianetop.springframeworkmvc.mappers;

import nl.lilianetop.springframeworkmvc.domain.Beer;
import nl.lilianetop.springframeworkmvc.models.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    Beer beerDtoToBeer(BeerDTO beerDto);

    BeerDTO beerToBeerDto(Beer beer);
}
