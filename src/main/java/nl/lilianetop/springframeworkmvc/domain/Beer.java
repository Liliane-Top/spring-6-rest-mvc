package nl.lilianetop.springframeworkmvc.domain;

import jakarta.persistence.*;
import lombok.*;
import nl.lilianetop.springframeworkmvc.models.BeerStyle;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Beer {

    @Id
    @GeneratedValue(generator =  "UUID", strategy = GenerationType.AUTO)
    @Column(length=36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID id;
    @Version
    private Integer version;
    private String beerName;
    private BeerStyle beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;


}
