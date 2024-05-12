package ProductService.service;

import ProductService.handleExeption.HandlerExeptionProduct;
import exerciseCRUD.DAO.SqlDdlEnum;
import exerciseCRUD.DAO.UserDao;
import ProductService.dto.ProductDto;
import ProductService.dto.RevisionResponse;
import ProductService.dto.EntityUserProducts;
import ProductService.dto.UserProductType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static exerciseCRUD.DAO.SqlDdlEnum.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final NamedParameterJdbcTemplate namedJdbcTemplateStudentPostgresSQL;
    private final UserDao userDao;

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");

    public RevisionResponse<List<ProductDto>> getProduct(Long idProduct) {
        List<ProductDto> productList = null;
        if (idProduct == null) {
            productList = getRecords(ProductDto.class, Collections.emptyMap(), selectProducts);
        } else {
            productList = getRecords(ProductDto.class, Map.of("idProduct", idProduct), selectProduct);
        }
        System.out.printf("###%s###%n", productList);
        return RevisionResponse.of(DATE_FORMAT.format(System.currentTimeMillis()), productList);
    }

    public RevisionResponse<List<ProductDto>> payProduct(Long userId, UserProductType typeProduct, BigDecimal sumPay) {
        //-- Update (pay)
        userDao.queryDML(payProduct,
                Map.of( "idUser", userId,
                        "typeProduct", typeProduct.toString(),
                        "divSum", sumPay));
        // -- Get Update Product
        List<ProductDto> productList = getRecords(ProductDto.class, Map.of("idProduct", userId), selectProduct);
        return RevisionResponse.of(DATE_FORMAT.format(System.currentTimeMillis()), productList);
    }

    public RevisionResponse<EntityUserProducts> saveProductForUserId(EntityUserProducts saveEntity) {
        String currentUser = saveEntity.getUser().getUsername();

        // -- Validate distinct product
        Map<UserProductType, Long> countProduct = saveEntity.getListProducts().stream()
                .map(ProductDto::getTypeProduct)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        if (countProduct.entrySet().stream()
                .filter(val -> val.getValue() > 1)
                .count() > 0){
            throw new HandlerExeptionProduct("Дублирование продуктов для пользователя :", currentUser);
        }
        // -- Save user
        userDao.queryDML(insertUser, Map.of("nameUser", currentUser));
        // -- Save Products for current user
        saveEntity.getListProducts().forEach(product -> {
            Map<String, String> saveMap = Map.of("nameUser", currentUser,
                    "numberCount", product.getNumberCount().toString(),
                    "balans", product.getBalans().toString(),
                    "typeProduct", product.getTypeProduct().toString());
           userDao.queryDML(insertProductForCurrentUser, saveMap);
        });
        return RevisionResponse.of(DATE_FORMAT.format(System.currentTimeMillis()), saveEntity);
    }

    public RevisionResponse<List<ProductDto>> getProductForUserId(Long idUser) {
        List<ProductDto> productList = getRecords(ProductDto.class,
                Map.of("idUser", idUser), selectProductsForUserId);
        return RevisionResponse.of(DATE_FORMAT.format(System.currentTimeMillis()), productList);
    }

    public void deleteUser(String username) {
        userDao.queryDML(deleteUsers,
                Map.of("nameUser", "%" + username + "%"));
    }

    public  <T> List<T> getRecords(Class<T> clazz, Map<String, Object> mapParameters,
                                   SqlDdlEnum sqlDdlEnum) {
        List<T> setRows = namedJdbcTemplateStudentPostgresSQL
                .query(sqlDdlEnum.getQuerySQL(), mapParameters, new BeanPropertyRowMapper<>(clazz));
        return Optional.of(setRows)
                .orElse(Collections.emptyList());
    }
}
