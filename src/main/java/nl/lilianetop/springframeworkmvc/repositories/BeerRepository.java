package nl.lilianetop.springframeworkmvc.repositories;

import nl.lilianetop.springframeworkmvc.domain.Beer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

//@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID> {
}
