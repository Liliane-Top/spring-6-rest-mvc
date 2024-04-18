package nl.lilianetop.springframeworkmvc.mappers;

import nl.lilianetop.springframeworkmvc.domain.Customer;
import nl.lilianetop.springframeworkmvc.models.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO customerDto);

    CustomerDTO customerToCustomerDto(Customer customer);
}
