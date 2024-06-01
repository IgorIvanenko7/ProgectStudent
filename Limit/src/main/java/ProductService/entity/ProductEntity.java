package ProductService.entity;

import ProductService.dto.UserProductType;
import ProductService.config.UserProductTypeConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iduser", referencedColumnName="id")
    private UserEntity user;

    @Column(name = "numbercount")
    private Long numberCount;

    @Column(name = "balans")
    private BigDecimal balans;

    @Column(name = "typeproduct")
    @Convert(converter = UserProductTypeConverter.class)
    private UserProductType typeProduct;
}


