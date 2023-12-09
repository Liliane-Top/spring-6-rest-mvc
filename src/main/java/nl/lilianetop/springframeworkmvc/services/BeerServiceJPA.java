package nl.lilianetop.springframeworkmvc.services;

import lombok.RequiredArgsConstructor;
import nl.lilianetop.springframeworkmvc.mappers.BeerMapper;
import nl.lilianetop.springframeworkmvc.models.BeerDto;
import nl.lilianetop.springframeworkmvc.repositories.BeerRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDto> listBeers() {
        return beerRepository.findAll().stream().map(beerMapper::beerToBeerDto).toList();
    }

    @Override
    public Optional<BeerDto> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(id).orElse(null)));
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beer){
        return null;
    }

    @Override
    public BeerDto updateBeerById(UUID id){
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public void patchBeerById(UUID id, BeerDto beer) {

    }
}
