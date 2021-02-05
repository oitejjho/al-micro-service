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
public class CreateEmployeeTest {

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
    }

    @Test
    public void createEmployeeSuccessWithAdmin() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.setBearerAuth(adminJwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("test")
                .lastName("test")
                .email("test@test.com")
                .build();
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(employeeRequest, headers);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                "http://localhost:8080/employees",
                HttpMethod.POST,
                requestEntity,
                Response.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(new Integer(0), responseEntity.getBody().getStatus().getCode());
        assertEquals("Success", responseEntity.getBody().getStatus().getMessage());
    }

    @Test
    public void createEmployeeSuccessWithClient() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.setBearerAuth(clientJwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .firstName("test")
                .lastName("test")
                .email("test@test.com")
                .build();
        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(employeeRequest, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:8080/employees",
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void createEmployeeSuccessWithOutJwtToken() {
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
                "http://localhost:8080/employees",
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void createEmployeeFirstNameValidationSuccessWithAdmin() {
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
                "http://localhost:8080/employees",
                HttpMethod.POST,
                requestEntity,
                Response.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(new Integer(1), responseEntity.getBody().getStatus().getCode());
        assertEquals("Employee first name is required", responseEntity.getBody().getStatus().getMessage());
    }

    @Test
    public void createEmployeeLastNameValidationSuccessWithAdmin() {
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
                "http://localhost:8080/employees",
                HttpMethod.POST,
                requestEntity,
                Response.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(new Integer(2), responseEntity.getBody().getStatus().getCode());
        assertEquals("Employee last name is required", responseEntity.getBody().getStatus().getMessage());
    }

    @Test
    public void createEmployeeEmailValidationSuccessWithAdmin() {
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
                "http://localhost:8080/employees",
                HttpMethod.POST,
                requestEntity,
                Response.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(new Integer(3), responseEntity.getBody().getStatus().getCode());
        assertEquals("Employee email is required", responseEntity.getBody().getStatus().getMessage());
    }

    @Test
    public void createEmployeeEmailFormatValidationSuccessWithAdmin() {
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
                "http://localhost:8080/employees",
                HttpMethod.POST,
                requestEntity,
                Response.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(new Integer(7), responseEntity.getBody().getStatus().getCode());
        assertEquals("Email is invalid", responseEntity.getBody().getStatus().getMessage());
    }

}
