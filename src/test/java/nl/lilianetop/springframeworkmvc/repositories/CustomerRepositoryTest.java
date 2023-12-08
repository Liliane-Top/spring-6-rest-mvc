package nl.lilianetop.springframeworkmvc.repositories;

import nl.lilianetop.springframeworkmvc.domain.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository repository;
@Test
    void saveCustomer(){
        Customer customer = repository.save(Customer.builder()
                .customerName("Rodney Slante").build());

        assertThat(customer.getId()).isNotNull();
        assertThat(customer.getCustomerName()).isEqualTo("Rodney Slante");
    }


}