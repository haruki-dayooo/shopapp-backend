package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {
    @NotBlank(message = "Tittle is required!")
    @Size(min = 3, max = 200, message = "Tittle must be between 3 to 200 characters!")
    private String name;

    @Min(value = 0, message = "Price must be greater than or equal to 0!")
    @Max(value = 100000000, message = "Price must be lesser than or equal to 100000000!")
    private Float price;

    private String thumbnail;
    private String description;
    @JsonProperty("category_id")
    private String categoryId;

    private List<MultipartFile> files;
}
