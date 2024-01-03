package nl.lilianetop.springframeworkmvc.repositories;

import nl.lilianetop.springframeworkmvc.domain.Beer;
import nl.lilianetop.springframeworkmvc.domain.Category;
import nl.lilianetop.springframeworkmvc.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CategoryRepositoryTest {

  @Autowired
  CategoryRepository categoryRepository;

  Beer testBeer;
  Customer testCustomer;
  @Autowired
  private BeerRepository beerRepository;

  @BeforeEach
  void setup() {
    testBeer = beerRepository.findAll().get(0);
  }

  @Transactional
  @Test
  void categoryRepoTest() {
    Category savedCategory = categoryRepository.save(
        Category.builder()
            .description("ales").build());

    testBeer.addCategory(savedCategory);

    Beer savedBeer = beerRepository.save(testBeer);
    System.out.println(savedBeer);

    Long number = 0L;
    long number2 = 0; //this will be considered an int;

    int one = 1;
    float two = 2.0f;
    long sum = (long) (one + two);


  }

}