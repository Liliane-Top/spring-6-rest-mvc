package nl.lilianetop.springframeworkmvc.controllers;

import nl.lilianetop.springframeworkmvc.domain.Beer;
import nl.lilianetop.springframeworkmvc.exceptions.ExceptionNotFound;
import nl.lilianetop.springframeworkmvc.models.BeerDto;
import nl.lilianetop.springframeworkmvc.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    void getBeerById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDto beerDto = beerController.getBeerById(beer.getId());
        assertThat(beerDto).isNotNull();
    }

    @Test
    void getBeerByIdNotFound() {
        assertThrows(ExceptionNotFound.class, () -> { beerController.getBeerById(UUID.randomUUID());});
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