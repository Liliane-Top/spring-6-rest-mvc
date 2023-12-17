package nl.lilianetop.springframeworkmvc.services;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import nl.lilianetop.springframeworkmvc.domain.Beer;
import nl.lilianetop.springframeworkmvc.mappers.BeerMapper;
import nl.lilianetop.springframeworkmvc.models.BeerDto;
import nl.lilianetop.springframeworkmvc.models.BeerStyle;
import nl.lilianetop.springframeworkmvc.repositories.BeerRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

  private final BeerRepository beerRepository;
  private final BeerMapper beerMapper;

  private final static Integer DEFAULT_PAGE_INDEX = 0;
  private final static Integer DEFAULT_PAGE_SIZE = 25;


  @Override
  public Page<BeerDto> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
      Integer pageNumber, Integer pageSize) {

    Page<Beer> beerPage;

    PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

    if (StringUtils.hasText(beerName) && beerStyle == null) {
      beerPage = listBeersByName(beerName, pageRequest);
    } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
      beerPage = listBeersByBeerStyle(beerStyle, pageRequest);
    } else if (StringUtils.hasText(beerName) && beerStyle != null) {
      beerPage = listBeersByNameAndBeerStyle(beerName, beerStyle, pageRequest);
    } else {
      beerPage = beerRepository.findAll(pageRequest);
    }

    if (showInventory != null && !showInventory) {
      beerPage.forEach(beer -> beer.setQuantityOnHand(null));
    }

    return beerPage.map(beerMapper::beerToBeerDto);
  }

  private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
    int pageNumberRequest;
    int pageSizeRequest;

    if (pageNumber != null && pageNumber > 0) {
      pageNumberRequest = pageNumber - 1;
    } else {
      pageNumberRequest = DEFAULT_PAGE_INDEX;
    }

    if (pageSize == null) {
      pageSizeRequest = DEFAULT_PAGE_SIZE;
    } else if (pageSize > 1000) {
      pageSizeRequest = 1000;
    } else {
      pageSizeRequest = pageSize;
    }

//    Sort sort = Sort.by("beerName").ascending();
    Sort sort = Sort.by(Sort.Order.asc("beerName"));
    return PageRequest.of(pageNumberRequest, pageSizeRequest, sort);

  }

  private Page<Beer> listBeersByNameAndBeerStyle(String beerName, BeerStyle beerStyle,
      //PageRequest is a concrete implementation of the Pageable interface.
      // By using Pageable as the method parameter type, the code becomes more flexible and independent of a specific implementation.
      Pageable pageable) {
    return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%",
        beerStyle, pageable);
  }

  public Page<Beer> listBeersByBeerStyle(BeerStyle beerStyle, Pageable pageable) {
    return beerRepository.findAllByBeerStyle(beerStyle, pageable);
  }

  public Page<Beer> listBeersByName(String beerName, Pageable pageable) {
    return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageable);
  }

  @Override
  public Optional<BeerDto> getBeerById(UUID id) {
    return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(id).orElse(null)));
  }

  @Override
  public BeerDto saveNewBeer(BeerDto beer) {
    return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beer)));
  }

  @Override
  public Optional<BeerDto> updateBeerById(UUID id, BeerDto beerDto) {
    AtomicReference<Optional<BeerDto>> atomicReference = new AtomicReference<>();

    beerRepository.findById(id).ifPresentOrElse(
        beer -> {
          beer.setBeerName(beerDto.getBeerName());
          beer.setBeerStyle(beerDto.getBeerStyle());
          beer.setPrice(beerDto.getPrice());
          beer.setUpc(beerDto.getUpc());

          atomicReference.set(Optional.of(
              beerMapper.beerToBeerDto(beerRepository.save(beer))));
        }, () -> atomicReference.set(Optional.empty())
    );

    return atomicReference.get();
  }

  @Override
  public Boolean deleteById(UUID id) {
    if (beerRepository.existsById(id)) {
      beerRepository.deleteById(id);
      return true;
    }
    return false;
  }

  @Override
  public Optional<BeerDto> patchBeerById(UUID id, BeerDto beerDto) {
    AtomicReference<Optional<BeerDto>> atomicReference = new AtomicReference<>();

    beerRepository.findById(id).ifPresentOrElse(foundBeer -> {
      if (StringUtils.hasText(beerDto.getBeerName())) {
        foundBeer.setBeerName(beerDto.getBeerName());
      }
      if (beerDto.getBeerStyle() != null) {
        foundBeer.setBeerStyle(beerDto.getBeerStyle());
      }
      if (StringUtils.hasText(beerDto.getUpc())) {
        foundBeer.setUpc(beerDto.getUpc());
      }
      if (beerDto.getPrice() != null) {
        foundBeer.setPrice(beerDto.getPrice());
      }
      if (beerDto.getQuantityOnHand() != null) {
        foundBeer.setQuantityOnHand(beerDto.getQuantityOnHand());
      }
      atomicReference.set(Optional.of(beerMapper.beerToBeerDto(beerRepository.save(foundBeer))));
    }, () -> atomicReference.set(Optional.empty()));
    return atomicReference.get();
  }
}
