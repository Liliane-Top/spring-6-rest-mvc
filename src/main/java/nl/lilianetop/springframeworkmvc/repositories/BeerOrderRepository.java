package nl.lilianetop.springframeworkmvc.repositories;

import nl.lilianetop.springframeworkmvc.domain.BeerOrder;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID> {

}
