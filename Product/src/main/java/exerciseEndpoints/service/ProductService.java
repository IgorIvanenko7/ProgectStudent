package exerciseEndpoints.service;

import exerciseCRUD.DAO.SqlDdlEnum;
import exerciseCRUD.DAO.UserDao;
import exerciseEndpoints.dto.ProductDto;
import exerciseEndpoints.dto.RevisionResponse;
import exerciseEndpoints.dto.SaveEntityUserProducts;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static exerciseCRUD.DAO.SqlDdlEnum.*;

@Service
public class ProductService {

    private final NamedParameterJdbcTemplate namedJdbcTemplateStudentPostgresSQL;
    private final UserDao userDao;

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");

    public ProductService (NamedParameterJdbcTemplate namedJdbcTemplateStudent,
                           UserDao userDao) {
        this.namedJdbcTemplateStudentPostgresSQL = namedJdbcTemplateStudent;
        this.userDao = userDao;
    }

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

    public RevisionResponse<List<ProductDto>> getProductForUserId(Long idUser) {
        List<ProductDto> productList = getRecords(ProductDto.class,
                Map.of("idUser", idUser), selectProductsForUserId);
        return RevisionResponse.of(DATE_FORMAT.format(System.currentTimeMillis()), productList);
    }

    public RevisionResponse<SaveEntityUserProducts> saveProductForUserId(SaveEntityUserProducts saveEntity){
        String currentUser = saveEntity.getUser().getUsername();
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
