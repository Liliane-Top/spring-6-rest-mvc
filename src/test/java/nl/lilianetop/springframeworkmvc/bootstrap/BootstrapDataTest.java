package nl.lilianetop.springframeworkmvc.bootstrap;

import lombok.extern.slf4j.Slf4j;
import nl.lilianetop.springframeworkmvc.repositories.BeerRepository;
import nl.lilianetop.springframeworkmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@Slf4j
class BootstrapDataTest {

    @Autowired
    BeerRepository beerRepository;
    @Autowired
    CustomerRepository customerRepository;

    BootstrapData bootstrapData;
    @BeforeEach
    void setUp(){
        bootstrapData = new BootstrapData(beerRepository, customerRepository);
    }

    @Test
    void callRun() throws Exception {
        bootstrapData.run((String[]) null);

        assertThat(beerRepository.count()).isEqualTo(3);
        assertThat(customerRepository.count()).isEqualTo(3);
        assertThat(beerRepository.findAll().get(0).getBeerName()).isEqualTo("Galaxy Cat");
        log.info("the id of the first customer is " + beerRepository.findAll().get(0).getId());
    }

}