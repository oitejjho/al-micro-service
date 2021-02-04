package amlan.employee.dto;

import amlan.common.utils.PdpaUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EmployeeDTO {

    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String career;

    public void setEmail(String email) {
        this.email = PdpaUtils.maskEmail(email);
    }

}
