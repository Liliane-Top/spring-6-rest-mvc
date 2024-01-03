package nl.lilianetop.springframeworkmvc.repositories;

import nl.lilianetop.springframeworkmvc.domain.Beer;
import nl.lilianetop.springframeworkmvc.domain.BeerOrder;
import nl.lilianetop.springframeworkmvc.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class BeerOrderRepositoryTest {

  @Autowired
  BeerOrderRepository beerOrderRepository;

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  BeerRepository beerRepository;

  Beer testBeer;
  Customer testCustomer;

  @BeforeEach
  void setup() {
    testBeer = beerRepository.findAll().get(0);
    testCustomer = customerRepository.findAll().get(0);
  }

  @Transactional
  @Test
  void testBeerOrderRepository() {
    BeerOrder beerOrder = BeerOrder.builder()
        .customerRef("new beer order")
        .customer(testCustomer)
        .build();

    BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);//BeerOrder has a Customer but the Customer doesn't have a BeerOrder
//    BeerOrder savedBeerOrder = beerOrderRepository.saveAndFlush(beerOrder);//after being flushed to DB it is now Bidirectional. Customer also has a BeerOrder.
//other option is to override the Lombok setter by creating a setter that references the customer back to the beerOrder.

  }


}