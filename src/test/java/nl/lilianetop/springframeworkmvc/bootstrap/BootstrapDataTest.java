package nl.lilianetop.springframeworkmvc.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import nl.lilianetop.springframeworkmvc.repositories.BeerRepository;
import nl.lilianetop.springframeworkmvc.repositories.CustomerRepository;
import nl.lilianetop.springframeworkmvc.services.BeerCsvService;
import nl.lilianetop.springframeworkmvc.services.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest//ony creates limited context of importing the controllers and we have to import the otther classes otherwise error can't find bean of that class
@Import(BeerCsvServiceImpl.class)
@Slf4j
class BootstrapDataTest {

  @Autowired
  BeerRepository beerRepository;
  @Autowired
  CustomerRepository customerRepository;
  @Autowired
  BeerCsvService beerCsvService;

  BootstrapData bootstrapData;

  @BeforeEach
  void setUp() {
    bootstrapData = new BootstrapData(beerRepository, customerRepository, beerCsvService);
  }

  @Test
  void callRun() throws Exception {
    bootstrapData.run((String[]) null);

    assertThat(beerRepository.count()).isEqualTo(2413);
    assertThat(customerRepository.count()).isEqualTo(3);
    assertThat(beerRepository.findAll().get(0).getBeerName()).isEqualTo("Galaxy Cat");
    log.info("the id of the first customer is " + beerRepository.findAll().get(0).getId());
  }

}