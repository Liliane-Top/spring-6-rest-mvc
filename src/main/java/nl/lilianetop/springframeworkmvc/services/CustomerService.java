package nl.lilianetop.springframeworkmvc.services;

import nl.lilianetop.springframeworkmvc.models.CustomerDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    Optional<CustomerDto> getCustomerById(UUID id);
    List<CustomerDto> listCustomers();

    CustomerDto createAndSaveCustomer(CustomerDto customer);

    void updateCustomerById(UUID id, CustomerDto customer);

    void deleteCustomerById(UUID id);

    void patchCustomerById(UUID id, CustomerDto customer);
}
