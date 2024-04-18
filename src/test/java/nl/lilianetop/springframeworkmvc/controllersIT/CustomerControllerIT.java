package nl.lilianetop.springframeworkmvc.controllersIT;

import nl.lilianetop.springframeworkmvc.controllers.CustomerController;
import nl.lilianetop.springframeworkmvc.domain.Customer;
import nl.lilianetop.springframeworkmvc.exceptions.ExceptionNotFound;
import nl.lilianetop.springframeworkmvc.models.CustomerDTO;
import nl.lilianetop.springframeworkmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Transactional
    @Rollback
    @Test
    void returnEmptyList() {
        customerRepository.deleteAll();
        List<CustomerDTO> dtos = customerController.customerList();
        assertThat(dtos.size()).isEqualTo(0);
    }

    @Test
    void customerList() {
        List<CustomerDTO> dtos = customerController.customerList();
        assertThat(dtos.size()).isEqualTo(3);
    }

    @Test
    void getCustomerById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerById = customerController.getCustomerById(customer.getId());
        assertThat(customerById).isNotNull();
    }

    @Test
    void getCustomerByIdNotFound() {
        assertThrows(ExceptionNotFound.class, () ->
                customerController.getCustomerById(UUID.randomUUID()));
    }

    @Transactional
    @Rollback
    @Test
    void saveNewCustomer() {
        CustomerDTO customerDto = CustomerDTO.builder().customerName("Erika").build();
        ResponseEntity<CustomerDTO> responseEntity = customerController.createAndSaveCustomer(customerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String[] urlPAth = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID uuid = UUID.fromString(urlPAth[4]);
        assertThat(customerRepository.findById(uuid)).isNotNull();

    }

    @Transactional
    @Rollback
    @Test
    void getUpdateById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDto = CustomerDTO.builder().customerName("Valerie").build();
        customerController.updateCustomer(customer.getId(), customerDto);

        assertThat(customer.getCustomerName()).isEqualTo("Valerie");
    }

    @Test
    void updateCustomerNotFound() {
        assertThrows(ExceptionNotFound.class, () -> customerController.updateCustomer(UUID.randomUUID(), CustomerDTO.builder().build()));
    }


    @Transactional
    @Rollback
    @Test
    void deleteById() {
        Customer customer = customerRepository.findAll().get(0);
        ResponseEntity<CustomerDTO> responseEntity = customerController.deleteCustomerById(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void deleteCustomerNotFound() {
        assertThrows(ExceptionNotFound.class, () -> customerController.deleteCustomerById(UUID.randomUUID()));
    }

    @Transactional
    @Rollback
    @Test
    void patchCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDto = CustomerDTO.builder().customerName("Maaike").build();
        ResponseEntity<CustomerDTO> responseEntity = customerController.patchCustomerById(customer.getId(), customerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
        assertThat(customer.getCustomerName()).isEqualTo("Maaike");

    }

    @Test
    void patchCustomerNotFound() {
        assertThrows(ExceptionNotFound.class, () ->
                customerController.patchCustomerById(UUID.randomUUID(), CustomerDTO.builder().build()));
    }
}