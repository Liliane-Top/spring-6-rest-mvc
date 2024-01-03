package nl.lilianetop.springframeworkmvc.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
public class BeerOrderLine {

  public BeerOrderLine(UUID id, Integer version, LocalDateTime createdDate,
      LocalDateTime updateDate,
      Integer orderQuantity, Integer quantityAllocated, BeerOrder beerOrder, Beer beer) {
    this.id = id;
    this.version = version;
    this.createdDate = createdDate;
    this.updateDate = updateDate;
    this.orderQuantity = orderQuantity;
    this.quantityAllocated = quantityAllocated;
    setBeerOrder(beerOrder);
    setBeer(beer);
  }

  public void setBeerOrder(BeerOrder beerOrder) {
    this.beerOrder.getBeerOrderLines().add(this);
  }

  public void setBeer(Beer beer) {
    this.beer.getBeerOrderLines().add(this);
  }

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @JdbcTypeCode(SqlTypes.CHAR)
  @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
  private UUID id;

  @Version
  private Integer version;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdDate;

  @UpdateTimestamp
  private LocalDateTime updateDate;

  private Integer orderQuantity;

  private Integer quantityAllocated;

  @ManyToOne
  private BeerOrder beerOrder;

  @ManyToOne
  private Beer beer;

  public boolean isNew() {
    return this.id == null;
  }

}
