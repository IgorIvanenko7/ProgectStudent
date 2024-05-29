package ProductService.config;

import ProductService.dto.UserProductType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class UserProductTypeConverter implements AttributeConverter<UserProductType, String> {

    @Override
    public String convertToDatabaseColumn(UserProductType userProductType) {
        if (userProductType == null) {
            return null;
        }
        return userProductType.getCode();
    }

    @Override
    public UserProductType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(UserProductType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}


