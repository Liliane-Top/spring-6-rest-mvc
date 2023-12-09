package nl.lilianetop.springframeworkmvc.bootstrap;

import lombok.RequiredArgsConstructor;
import nl.lilianetop.springframeworkmvc.domain.Beer;
import nl.lilianetop.springframeworkmvc.domain.Customer;
import nl.lilianetop.springframeworkmvc.models.BeerStyle;
import nl.lilianetop.springframeworkmvc.repositories.BeerRepository;
import nl.lilianetop.springframeworkmvc.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        createBeerData();
        createCustomerData();
    }

    private void createBeerData() {
        Beer beer1 = Beer.builder()
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerRepository.saveAll(Arrays.asList(beer1, beer2, beer3));

    }
    private void createCustomerData() {
        Customer customer1 = Customer.builder()
                .customerName("Liliane")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .customerName("Krystina")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
//                .id(UUID.randomUUID())
//                .version(1)
                .customerName("Ton")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));

    }
}
