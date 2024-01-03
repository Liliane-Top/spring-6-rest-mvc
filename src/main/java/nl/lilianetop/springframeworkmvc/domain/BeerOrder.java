package nl.lilianetop.springframeworkmvc.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
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
public class BeerOrder {

  public BeerOrder(UUID id, Integer version, LocalDateTime createdDate, LocalDateTime updateDate,
      String customerRef, Customer customer, Set<BeerOrderLine> beerOrderLines) {
    this.id = id;
    this.version = version;
    this.createdDate = createdDate;
    this.updateDate = updateDate;
    this.customerRef = customerRef;
    setCustomer(customer);
    this.beerOrderLines = beerOrderLines;
  }
//Override the lombok setter to create a bidirectional relationship between BeerOrder and Customer
  //do not forget to initialize the BeerOrders set inside the Customer object
  public void setCustomer(Customer customer) {
    this.customer = customer;
    customer.getBeerOrders().add(this);
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

  private String customerRef;

  @ManyToOne
  private Customer customer;

  @Builder.Default
  @OneToMany(mappedBy = "beerOrder")
  private Set<BeerOrderLine> beerOrderLines = new HashSet<>();

  public boolean isNew() {
    return this.id == null;
  }

}
