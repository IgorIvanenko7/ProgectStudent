package ProductService.entity;


import ProductService.dto.UserProductType;
import ProductService.config.UserProductTypeConverter;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "newtable")
public class NewTableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "typeproduct")
    @Convert(converter = UserProductTypeConverter.class)
    private UserProductType typeproduct;
}