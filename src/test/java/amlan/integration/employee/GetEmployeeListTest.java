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
public class GetEmployeeListTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String adminJwtToken;

    private String clientJwtToken;

    ParameterizedTypeReference<Response<String>> parameterizedTypeReference ;

    @Before
    public void setUp() {

        //these configuration can be added statically or in application.yml or in different file system
        //based on profiles
        //for the simplicity I used hardcoded value.

        parameterizedTypeReference = new ParameterizedTypeReference<Response<String>>() {};

        UserSignInRequest userSignInRequestAdmin = UserSignInRequest.builder()
                .username("admin")
                .password("1Admin@1")
                .build();
        HttpEntity<UserSignInRequest> requestEntityAdmin = new HttpEntity<>(userSignInRequestAdmin);

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

        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Response> responseEntity = restTemplate.exchange(
                "http://localhost:8080/employees",
                HttpMethod.GET,
                requestEntity,
                Response.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(new Integer(0), responseEntity.getBody().getStatus().getCode());
        assertEquals("Success", responseEntity.getBody().getStatus().getMessage());
    }

    @Test
    public void createEmployeeSuccessWithClient() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.setBearerAuth(clientJwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:8080/employees",
                HttpMethod.GET,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void createEmployeeSuccessWithOutJwtToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EmployeeRequest> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:8080/employees",
                HttpMethod.POST,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }
}
