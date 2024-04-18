package nl.lilianetop.springframeworkmvc.services;

import nl.lilianetop.springframeworkmvc.models.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    Optional<CustomerDTO> getCustomerById(UUID id);

    List<CustomerDTO> listCustomers();

    CustomerDTO createAndSaveCustomer(CustomerDTO customer);

    Optional<CustomerDTO> updateCustomerById(UUID id, CustomerDTO customer);

    Boolean deleteCustomerById(UUID id);

    Optional<CustomerDTO> patchCustomerById(UUID id, CustomerDTO customer);
}
