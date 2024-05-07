package exerciseEndpoints.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevisionContent<T> {
    private Long timeSaveMilliseconds;
    private T saveContent;

    public static <T> RevisionContent<T> of(Long revision, T content) {
        return new RevisionContent<>(revision, content);
    }
}
