package ProductService.entity;

import ProductService.config.UserProductTypeConverter;
import ProductService.dto.UserProductType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iduser")
    private UserEntity user;

    @Column(name = "numbercount")
    private Long numberCount;

    @Column(name = "balans")
    private BigDecimal balans;

    @Column(name = "typeproduct")
    @Convert(converter = UserProductTypeConverter.class)
    private UserProductType typeProduct;
}


