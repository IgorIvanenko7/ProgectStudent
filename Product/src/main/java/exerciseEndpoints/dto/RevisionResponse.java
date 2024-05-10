package exerciseEndpoints.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevisionResponse<T> {
    private String dateOperation;
    private T content;

    public static <T> RevisionResponse<T> of(String revision, T content) {
        return new RevisionResponse<>(revision, content);
    }
}
