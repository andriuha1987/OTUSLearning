
package ru.otus.qa.auto.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuppressFBWarnings(value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class Pet {
    private Category category;
    private Long id;
    private String name;
    private List<String> photoUrls;
    private Status status;
    private List<Tag> tags;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return Objects.equals(category, pet.category) && Objects.equals(name, pet.name) && Objects.equals(photoUrls, pet.photoUrls) && status == pet.status && Objects.equals(tags, pet.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, name, photoUrls, status, tags);
    }
}
