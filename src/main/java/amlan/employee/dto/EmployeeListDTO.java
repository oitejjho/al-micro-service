package amlan.employee.dto;

import amlan.common.utils.PdpaUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EmployeeListDTO {

    private List<EmployeeDTO> employees;

    private int page;

    private int size;

    private int totalElementsInPage;

    private int totalPages;

    private long totalElements;

}
