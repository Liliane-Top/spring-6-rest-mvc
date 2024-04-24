package nl.lilianetop.springframeworkmvc.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import nl.lilianetop.springframeworkmvc.models.BeerStyle;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
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
public class Beer {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @JdbcTypeCode(SqlTypes.CHAR)//telling hiberbate specifically you want to use UUID to a String this is a mapping annotation
  @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)//Mysql needs to know the length of the varchar this is not required for H2 database
  private UUID id;

  @Version
  private Integer version;

  @NotNull
  @NotBlank
  @Size(max = 50)
  @Column(length = 50)
  private String beerName;

  @NotNull
  private BeerStyle beerStyle;

  @NotNull
  @NotBlank
  @Size(max = 255)
  private String upc;

  private Integer quantityOnHand;

  @NotNull
  private BigDecimal price;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdDate;

  @UpdateTimestamp
  private LocalDateTime updateDate;

  @Builder.Default
  @OneToMany(mappedBy = "beer")
  private Set<BeerOrderLine> beerOrderLines = new HashSet<>();

  @Builder.Default
  @ManyToMany
  @JoinTable(name = "beer_category",
      joinColumns = @JoinColumn(name = "beer_id") ,
      inverseJoinColumns = @JoinColumn(name = "category_id"))
  private Set<Category> categories = new HashSet<>();

  public void addCategory(Category category) {
    this.categories.add(category);
    category.getBeers().add(this);

  }

  public void removeCategory(Category category) {
    this.getCategories().remove(category);
    category.getBeers().remove(this);
  }
}
