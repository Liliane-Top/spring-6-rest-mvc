package nl.lilianetop.springframeworkmvc.services;

import lombok.RequiredArgsConstructor;
import nl.lilianetop.springframeworkmvc.mappers.CustomerMapper;
import nl.lilianetop.springframeworkmvc.models.CustomerDto;
import nl.lilianetop.springframeworkmvc.repositories.CustomerRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDto getCustomerById(UUID id) {
        return null;
    }

    @Override
    public List<CustomerDto> listCustomers() {
        return null;
    }

    @Override
    public CustomerDto createAndSaveCustomer(CustomerDto customer) {
        return null;
    }

    @Override
    public void updateCustomerById(UUID id, CustomerDto customer) {

    }

    @Override
    public void deleteCustomerById(UUID id) {

    }

    @Override
    public void patchCustomerById(UUID id, CustomerDto customer) {

    }
}
