package amlan.employee.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EmployeeResponse {

    @ApiModelProperty(position = 1)
    private List<EmployeeDTO> employees;

    @ApiModelProperty(position = 2)
    private int page;

    @ApiModelProperty(position = 3)
    private int size;

    @ApiModelProperty(position = 4)
    private int totalElementsInPage;

    @ApiModelProperty(position = 5)
    private long totalPages;

    @ApiModelProperty(position = 6)
    private long totalElements;

}
