package ProductService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserProductType {
    COUNT("COUNT"),
    CARD("CARD");

    private String code;
}

