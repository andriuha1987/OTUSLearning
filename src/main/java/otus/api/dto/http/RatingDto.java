package otus.api.dto.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RatingDto {
    @JsonProperty("name")
    String name;
    @JsonProperty("score")
    int score;
}
