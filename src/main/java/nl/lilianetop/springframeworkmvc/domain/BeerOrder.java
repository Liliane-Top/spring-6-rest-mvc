package nl.lilianetop.springframeworkmvc.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BeerOrder {

//  public BeerOrder(UUID id, Integer version, LocalDateTime createdDate, LocalDateTime updateDate,
//      String customerRef, Customer customer, Set<BeerOrderLine> beerOrderLines) {
//    this.id = id;
//    this.version = version;
//    this.createdDate = createdDate;
//    this.updateDate = updateDate;
//    this.customerRef = customerRef;
//    setCustomer(customer);
//    this.beerOrderLines = beerOrderLines;
//  }
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
