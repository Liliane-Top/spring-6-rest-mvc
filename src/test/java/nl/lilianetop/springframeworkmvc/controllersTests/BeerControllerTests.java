package nl.lilianetop.springframeworkmvc.controllersTests;

import nl.lilianetop.springframeworkmvc.controllers.BeerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BeerControllerTests {

    @Autowired
    BeerController controller;

    @Test
    void call_beerList(){
        System.out.println(controller.listBeers());
    }
}
