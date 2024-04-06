package exerciseStreamApi;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Employee {

    private String name;
    private Integer age ;
    private String position;
}
