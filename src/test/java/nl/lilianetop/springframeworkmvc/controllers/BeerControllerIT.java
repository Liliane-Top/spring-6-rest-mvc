package nl.lilianetop.springframeworkmvc.controllers;

import nl.lilianetop.springframeworkmvc.domain.Beer;
import nl.lilianetop.springframeworkmvc.exceptions.ExceptionNotFound;
import nl.lilianetop.springframeworkmvc.mappers.BeerMapper;
import nl.lilianetop.springframeworkmvc.models.BeerDto;
import nl.lilianetop.springframeworkmvc.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    BeerMapper beerMapper;

    @Test
    void deleteBeerNotFound() {
        assertThrows(ExceptionNotFound.class, () -> beerController.deleteBeerById(UUID.randomUUID()));
    }

    @Transactional
    @Rollback
    @Test
    void deleteBeer() {
        Beer beer = beerRepository.findAll().get(0);
        ResponseEntity<BeerDto> responseEntity = beerController.deleteBeerById(beer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
        assertThat(beerRepository.findById(beer.getId())).isEmpty();
    }
    @Transactional
    @Rollback
    @Test
    void updateBeerNotFound() {
        assertThrows(ExceptionNotFound.class, () ->
                beerController.updateBeer(UUID.randomUUID(), BeerDto.builder().build()));
    }
    @Transactional
    @Rollback
    @Test
    void updateBeer() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDto beerDto = beerMapper.beerToBeerDto(beer);
        beerDto.setId(null);
        beerDto.setVersion(null);
        final String updatedName = "Updated";
        beerDto.setBeerName(updatedName);

        ResponseEntity<BeerDto> responseEntity = beerController.updateBeer(beer.getId(), beerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Beer updatedBeer = beerRepository.findAll().get(0);
        assertThat(updatedBeer.getBeerName()).isEqualTo(updatedName);

    }

    @Rollback
    @Transactional
    @Test
    void saveNewBeer() {
        BeerDto beerDto = BeerDto.builder().beerName("Lager").build();
        ResponseEntity<BeerDto> responseEntity = beerController.createAndSaveBeer(beerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        System.out.println(responseEntity.getHeaders().getLocation().getPath());
        String[] UUIDLocations = responseEntity.getHeaders().getLocation().getPath().split("/");

        UUID savedUuid = UUID.fromString(UUIDLocations[4]);

        Beer beer = beerRepository.findById(savedUuid).get();
        assertThat(beer).isNotNull();
    }

    @Test
    void getBeerById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDto beerDto = beerController.getBeerById(beer.getId());
        assertThat(beerDto).isNotNull();
    }

    @Test
    void getBeerByIdNotFound() {
        assertThrows(ExceptionNotFound.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Test
    void getBeerList() {
        List<BeerDto> dtos = beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(3);
    }
    @Transactional
    @Rollback
    @Test
    void getEmptyList() {
        beerRepository.deleteAll();
        List<BeerDto> dtos = beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(0);
    }
}