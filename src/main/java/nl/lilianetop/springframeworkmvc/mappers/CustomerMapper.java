package nl.lilianetop.springframeworkmvc.mappers;

import nl.lilianetop.springframeworkmvc.domain.Customer;
import nl.lilianetop.springframeworkmvc.models.CustomerDto;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDto customerDto);
    CustomerDto customerToCustomerDto(Customer customer);
}
