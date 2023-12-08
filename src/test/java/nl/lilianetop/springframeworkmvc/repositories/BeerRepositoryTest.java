package nl.lilianetop.springframeworkmvc.repositories;

import nl.lilianetop.springframeworkmvc.domain.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository repository;

    @Test
    void saveBeer() {
        Beer savedBeer = repository.save(Beer.builder()
                .beerName("Heineken").build());

        assertThat(savedBeer.getId()).isNotNull();
        assertThat(savedBeer.getBeerName()).isEqualTo("Heineken");
    }

}