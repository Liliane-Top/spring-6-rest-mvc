package nl.lilianetop.springframeworkmvc.services;

import nl.lilianetop.springframeworkmvc.models.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    Customer getCustomerById(UUID id);
    List<Customer> listCustomers();

    Customer createAndSaveCustomer(Customer customer);
}
