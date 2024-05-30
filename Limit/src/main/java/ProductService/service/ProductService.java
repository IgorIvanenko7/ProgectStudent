package ProductService.service;

import ProductService.config.CollectionModelMapper;
import ProductService.dto.*;
import ProductService.entity.ProductEntity;
import ProductService.entity.UserEntity;
import ProductService.handleExeption.HandlerExeptionProduct;
import ProductService.repo.ProductRepo;
import ProductService.repo.UserRepo;
import ProductService.repository.DbOperations;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ProductService.dto.SqlDdlEnum.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductService {

    private final DbOperations dbOperations;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final CollectionModelMapper collectionModelMapper;

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");

    public RevisionResponse<List<ProductDto>> getProduct(Long idProduct) {
        List<ProductDto> productList;
        if (idProduct == null) {
            productList = dbOperations.getRecords(ProductDto.class, Collections.emptyMap(), selectProducts);
        } else {
            productList = dbOperations.getRecords(ProductDto.class, Map.of("idProduct", idProduct), selectProduct);
        }
        System.out.printf("###%s###%n", productList);
        return RevisionResponse.of(DATE_FORMAT.format(System.currentTimeMillis()), productList);
    }

    public RevisionResponse<List<ProductDto>> getProductJPA(Long idProduct) {
        List<ProductDto> productList;
        if (idProduct == null) {
            productList = collectionModelMapper.mapAsList(
                    productRepo.findAll(), ProductDto.class);
        } else {
            productList = collectionModelMapper.mapAsList(
                    productRepo.findProductId(idProduct), ProductDto.class);
        }
        log.info("### Response product: {} ###", productList);
        return RevisionResponse.of(DATE_FORMAT.format(System.currentTimeMillis()), productList);
    }

    public RevisionResponse<List<ProductDto>> payProduct(Long userId, String typeProduct, BigDecimal sumPay) {
        //-- Update (pay)
        dbOperations.queryDML(payProduct,
                Map.of( "idUser", userId,
                        "typeProduct", typeProduct,
                        "divSum", sumPay));

        // -- Get Update Product
        List<ProductDto> productList = dbOperations.getRecords(ProductDto.class, Map.of("idProduct", userId), selectProduct);
        return RevisionResponse.of(DATE_FORMAT.format(System.currentTimeMillis()), productList);
    }

    public RevisionResponse<List<ProductDto>> payProductJPA(Long userId, String typeProduct, BigDecimal sumPay) {
        var productEntityList = productRepo.payProduct(userId, typeProduct, sumPay);
        List<ProductDto> productList = collectionModelMapper.mapAsList(
                productEntityList, ProductDto.class);
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
        dbOperations.queryDML(insertUser, Map.of("nameUser", currentUser));
        // -- Save Products for current user
        saveEntity.getListProducts().forEach(product -> {
            Map<String, String> saveMap = Map.of("nameUser", currentUser,
                    "numberCount", product.getNumberCount().toString(),
                    "balans", product.getBalans().toString(),
                    "typeProduct", product.getTypeProduct().toString());
            dbOperations.queryDML(insertProductForCurrentUser, saveMap);
        });
        return RevisionResponse.of(DATE_FORMAT.format(System.currentTimeMillis()), saveEntity);
    }

    @Transactional
    public RevisionResponse<EntityUserProducts> saveProductForUserIdJPA(EntityUserProducts saveEntity) {
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
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(currentUser);
        userRepo.save(userEntity);
        // -- Convert DTO to Entity & Save Products for current user
        List<ProductEntity> productEntityList = collectionModelMapper.mapAsList(
                saveEntity.getListProducts(), ProductEntity.class);
        productEntityList.forEach(productEntity -> productEntity.setUser(userEntity));
        productRepo.saveAll(productEntityList);

        return RevisionResponse.of(DATE_FORMAT.format(System.currentTimeMillis()), saveEntity);
    }

    public RevisionResponse<List<ProductDto>> getProductForUserId(Long idUser) {
        List<ProductDto> productList = dbOperations.getRecords(ProductDto.class,
                Map.of("idUser", idUser), selectProductsForUserId);
        return RevisionResponse.of(DATE_FORMAT.format(System.currentTimeMillis()), productList);
    }

    // Реализация через JPA Join
    public RevisionResponse<List<ProductDto>> getProductForUserIdJPA(Long idUser) {
        var productEntityList = userRepo.findUserId(idUser).getProductEntityList();
        List<ProductDto> productList = collectionModelMapper.mapAsList(
                productEntityList, ProductDto.class);
        return RevisionResponse.of(DATE_FORMAT.format(System.currentTimeMillis()), productList);
    }

    public void deleteUser(String username) {
        dbOperations.queryDML(deleteUsers,
                Map.of("nameUser", "%" + username + "%"));
    }

    public ResponseEntity<User> deleteUserJPA(String username) {
        if (userRepo.getUsers(username).isEmpty()) {
            throw new HandlerExeptionProduct(
                    "Пользователь :" + username," не найден");
        }
        var deleteUser = userRepo.deleteUser(username);
        return Optional.of(collectionModelMapper.map(deleteUser, User.class))
                .map(delU -> new ResponseEntity<>(delU, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
}
