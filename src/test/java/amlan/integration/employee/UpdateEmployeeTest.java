package amlan.integration.employee;

import amlan.common.model.Response;
import amlan.employee.dto.EmployeeRequest;
import amlan.user.dto.UserSignInRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UpdateEmployeeTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String adminJwtToken;

    private String clientJwtToken;

    @Before
    public void setUp() {

        //these configuration can be added statically or in application.yml or in different file system
        //based on profiles
        //for the simplicity I used hardcoded value.

        UserSignInRequest userSignInRequestAdmin = UserSignInRequest.builder()
                .username("admin")
                .password("1Admin@1")
                .build();
        HttpEntity<UserSignInRequest> requestEntityAdmin = new HttpEntity<>(userSignInRequestAdmin);
        ParameterizedTypeReference<Response<String>> parameterizedTypeReference = new ParameterizedTypeReference<Response<String>>() {
        };
        ResponseEntity<Response<String>> adminResponseEntity = restTemplate.exchange(
                "http://localhost:8080/users/signin",
                HttpMethod.POST,
                requestEntityAdmin,
                parameterizedTypeReference
        );
        adminJwtToken = adminResponseEntity.getBody().getData();

        UserSignInRequest userSignInRequestClient = UserSignInRequest.builder()
                .username("client")
                .password("1Client@1")
                .build();
        HttpEntity<UserSignInRequest> requestEntityClient = new HttpEntity<>(userSignInRequestClient);
        ResponseEntity<Response<String>> clientResponseEntity = restTemplate.exchange(
                "http://localhost:8080/users/signin?password=1Client@1&username=client",
                HttpMethod.POST,
                requestEntityClient,
                parameterizedTypeReference
        );
        clientJwtToken = clientResponseEntity.getBody().getData();

        //could create employee dynamically and update
        //for the simplicity didn't implement rather keep the scope separated
    }

    @Test
    public void updateEmployeeSuccessWithAdmin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.setBearerAuth(adminJwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("oitejjho")
                .lastName("test")
                .email("test@test.com")
                .build();
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(employeeRequest, headers);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                "http://localhost:8080/employees/3",
                HttpMethod.PUT,
                requestEntity,
                Response.class);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void updateEmployeeSuccessWithClient() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.setBearerAuth(clientJwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("oitejjho")
                .lastName("test")
                .email("test@test.com")
                .build();
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(employeeRequest, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:8080/employees/3",
                HttpMethod.PUT,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void updateEmployeeSuccessWithOutJwtToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.setContentType(MediaType.APPLICATION_JSON);

        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("test")
                .lastName("test")
                .email("test@test.com")
                .build();
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(employeeRequest, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:8080/employees/3",
                HttpMethod.PUT,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void updateEmployeeFirstNameValidationSuccessWithAdmin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.setBearerAuth(adminJwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("")
                .lastName("test")
                .email("test@test.com")
                .build();
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(employeeRequest, headers);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                "http://localhost:8080/employees/3",
                HttpMethod.PUT,
                requestEntity,
                Response.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(new Integer(1), responseEntity.getBody().getStatus().getCode());
        assertEquals("Employee first name is required", responseEntity.getBody().getStatus().getMessage());
    }

    @Test
    public void updateEmployeeLastNameValidationSuccessWithAdmin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.setBearerAuth(adminJwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("test")
                .lastName("")
                .email("test@test.com")
                .build();
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(employeeRequest, headers);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                "http://localhost:8080/employees/3",
                HttpMethod.PUT,
                requestEntity,
                Response.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(new Integer(2), responseEntity.getBody().getStatus().getCode());
        assertEquals("Employee last name is required", responseEntity.getBody().getStatus().getMessage());
    }

    @Test
    public void updateEmployeeEmailValidationSuccessWithAdmin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.setBearerAuth(adminJwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("test")
                .lastName("test")
                .email("")
                .build();
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(employeeRequest, headers);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                "http://localhost:8080/employees/3",
                HttpMethod.PUT,
                requestEntity,
                Response.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(new Integer(3), responseEntity.getBody().getStatus().getCode());
        assertEquals("Employee email is required", responseEntity.getBody().getStatus().getMessage());
    }

    @Test
    public void updateEmployeeEmailFormatValidationSuccessWithAdmin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.setBearerAuth(adminJwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("test")
                .lastName("test")
                .email("asdflasd")
                .build();
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(employeeRequest, headers);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                "http://localhost:8080/employees/3",
                HttpMethod.PUT,
                requestEntity,
                Response.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(new Integer(7), responseEntity.getBody().getStatus().getCode());
        assertEquals("Email is invalid", responseEntity.getBody().getStatus().getMessage());
    }

    @Test
    public void updateEmployeeNotFoundWithAdmin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.setBearerAuth(adminJwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("test")
                .lastName("test")
                .email("oitejjho@gmail.com")
                .build();
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(employeeRequest, headers);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                "http://localhost:8080/employees/10000",
                HttpMethod.PUT,
                requestEntity,
                Response.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(new Integer(5), responseEntity.getBody().getStatus().getCode());
        assertEquals("Employee does not exists", responseEntity.getBody().getStatus().getMessage());
    }

    @Test
    public void updateEmployeeEmailOccupiedAlreadyWithAdmin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.setBearerAuth(adminJwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("test")
                .lastName("test")
                .email("bill@bill.com")
                .build();
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(employeeRequest, headers);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                "http://localhost:8080/employees/3",
                HttpMethod.PUT,
                requestEntity,
                Response.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(new Integer(4), responseEntity.getBody().getStatus().getCode());
        assertEquals("Employee email already exists", responseEntity.getBody().getStatus().getMessage());
    }

}
