package amlan.employee.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 4, max = 250, message = "Minimum first name length: 4 characters")
    @Column(nullable = false)
    private String firstName;

    @Size(min = 4, max = 250, message = "Minimum last name length: 4 characters")
    @Column(nullable = false)
    private String lastName;

    @Size(min = 7, max = 250, message = "Minimum email length: 7 characters")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = true)
    private String career;

}
