package amlan.integration.user;

import amlan.common.model.Response;
import amlan.user.dto.UserDataDTO;
import amlan.user.dto.UserSignInRequest;
import amlan.user.model.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SignInUserTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private UserSignInRequest request;

    @Before
    public void setUp() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ROLE_ADMIN);
        request = UserSignInRequest.builder()
                .password("1Admin@1")
                .username("admin")
                .build();
    }

    @Test
    public void loginUserSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserSignInRequest> requestEntity = new HttpEntity<>(request, headers);
        ParameterizedTypeReference<Response<String>> parameterizedTypeReference = new ParameterizedTypeReference<Response<String>>() {};
        ResponseEntity<Response<String>> responseEntity = restTemplate.exchange(
                "http://localhost:8080/users/signin",//these configuration can be added statically or in application.yml or in different file system//based on profiles//for the simplicity I used hardcoded value.
                HttpMethod.POST,
                requestEntity,
                parameterizedTypeReference);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(new Integer(0), responseEntity.getBody().getStatus().getCode());
        assertEquals("Success", responseEntity.getBody().getStatus().getMessage());
        assertNotNull(responseEntity.getBody().getData());
    }

    @Test
    public void loginUserWithoutUserName() {

        request.setUsername("");

        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserSignInRequest> requestEntity = new HttpEntity<>(request, headers);
        ParameterizedTypeReference<Response<String>> parameterizedTypeReference = new ParameterizedTypeReference<Response<String>>() {};
        ResponseEntity<Response<String>> responseEntity = restTemplate.exchange(
                "http://localhost:8080/users/signin",//these configuration can be added statically or in application.yml or in different file system//based on profiles//for the simplicity I used hardcoded value.
                HttpMethod.POST,
                requestEntity,
                parameterizedTypeReference);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(new Integer(11), responseEntity.getBody().getStatus().getCode());
        assertEquals("Username is required", responseEntity.getBody().getStatus().getMessage());
        assertNull(responseEntity.getBody().getData());
    }

    @Test
    public void loginUserWithoutPassword() {

        request.setPassword("");

        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserSignInRequest> requestEntity = new HttpEntity<>(request, headers);
        ParameterizedTypeReference<Response<String>> parameterizedTypeReference = new ParameterizedTypeReference<Response<String>>() {};
        ResponseEntity<Response<String>> responseEntity = restTemplate.exchange(
                "http://localhost:8080/users/signin",//these configuration can be added statically or in application.yml or in different file system//based on profiles//for the simplicity I used hardcoded value.
                HttpMethod.POST,
                requestEntity,
                parameterizedTypeReference);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(new Integer(13), responseEntity.getBody().getStatus().getCode());
        assertEquals("Password is required", responseEntity.getBody().getStatus().getMessage());
        assertNull(responseEntity.getBody().getData());
    }

}
