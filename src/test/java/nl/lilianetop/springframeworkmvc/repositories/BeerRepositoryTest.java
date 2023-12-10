package nl.lilianetop.springframeworkmvc.repositories;

import jakarta.validation.ConstraintViolationException;
import nl.lilianetop.springframeworkmvc.domain.Beer;
import nl.lilianetop.springframeworkmvc.models.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository repository;

    @Test
    void saveBeer() {
        Beer savedBeer = repository.save(Beer.builder()
                .beerName("Heineken")
                .beerStyle(BeerStyle.PILSNER)
                .upc("267368783424")
                .price(new BigDecimal("3.96"))
                .build());

        repository.flush();

        assertThat(savedBeer.getId()).isNotNull();
        assertThat(savedBeer.getBeerName()).isEqualTo("Heineken");
    }

    @Test
    void saveBeerTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            repository.save(Beer.builder()
                    .beerName("HeinekenbhjbjhiudhiuwehdiuhsdlakshdiuhsihkwnskjshdkhduishduidwhDFIUHKNKADWF")
                    .beerStyle(BeerStyle.PILSNER)
                    .upc("267368783424")
                    .price(new BigDecimal("3.96"))
                    .build());
            repository.flush();
        });
    }


}