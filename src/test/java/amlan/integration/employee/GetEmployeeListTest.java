package amlan.integration.employee;

import amlan.common.model.Response;
import amlan.employee.dto.EmployeeRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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

    @Before
    public void setUp() {

        //these configuration can be added statically or in application.yml or in different file system
        //based on profiles
        //for the simplicity I used hardcoded value.

        ResponseEntity<String> adminResponseEntity = restTemplate.exchange(
                "http://localhost:8080/users/signin?password=admin&username=admin",
                HttpMethod.POST,
                null,
                String.class
        );
        adminJwtToken = adminResponseEntity.getBody();

        ResponseEntity<String> clientResponseEntity = restTemplate.exchange(
                "http://localhost:8080/users/signin?password=client&username=client",
                HttpMethod.POST,
                null,
                String.class
        );
        adminJwtToken = adminResponseEntity.getBody();
        clientJwtToken = clientResponseEntity.getBody();
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
