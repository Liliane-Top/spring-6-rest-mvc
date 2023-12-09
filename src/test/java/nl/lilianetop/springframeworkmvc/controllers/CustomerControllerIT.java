package nl.lilianetop.springframeworkmvc.controllers;

import nl.lilianetop.springframeworkmvc.domain.Customer;
import nl.lilianetop.springframeworkmvc.exceptions.ExceptionNotFound;
import nl.lilianetop.springframeworkmvc.models.CustomerDto;
import nl.lilianetop.springframeworkmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
        List<CustomerDto> dtos = customerController.customerList();
        assertThat(dtos.size()).isEqualTo(0);
    }

    @Test
    void customerList() {
        List<CustomerDto> dtos = customerController.customerList();
        assertThat(dtos.size()).isEqualTo(3);
    }

    @Test
    void getCustomerById() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDto customerById = customerController.getCustomerById(customer.getId());
        assertThat(customerById).isNotNull();
    }

    @Test
    void getCustomerByIdNotFound() {
        assertThrows(ExceptionNotFound.class, () ->
                customerController.getCustomerById(UUID.randomUUID()));
    }
}