package amlan.user.controller;

import amlan.common.constant.StatusConstants;
import amlan.common.exception.CustomException;
import amlan.common.exception.DuplicateEntityException;
import amlan.common.exception.NotFoundException;
import amlan.common.model.Response;
import amlan.employee.controller.EmployeeController;
import amlan.employee.dto.EmployeeDTO;
import amlan.employee.dto.EmployeeListDTO;
import amlan.employee.dto.EmployeeRequest;
import amlan.employee.dto.EmployeeResponse;
import amlan.employee.model.Employee;
import amlan.employee.service.EmployeeService;
import amlan.user.controller.UserController;
import amlan.user.dto.UserDataDTO;
import amlan.user.dto.UserSignInRequest;
import amlan.user.model.Role;
import amlan.user.model.User;
import amlan.user.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private UserService userService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private UserController userController;

    private HttpServletResponse httpResponse;
    private UserDataDTO userDataDTO;
    private User user;
    private UserSignInRequest userSignInRequest;


    @Before
    public void setUp() {
        httpResponse = new MockHttpServletResponse();
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ROLE_ADMIN);
        userDataDTO = UserDataDTO.builder()
                .username("oitejjho")
                .email("oitejjho@gmail.com")
                .password("1Admin@1")
                .roles(roles)
                .build();

        user = new User();

        userSignInRequest = UserSignInRequest
                .builder()
                .username("oitejjho")
                .password("1Admin@1")
                .build();
    }


    @Test
    public void testSignupSuccess() {

        Mockito.doReturn(user).when(modelMapper).map(Mockito.any(UserDataDTO.class), Mockito.eq(User.class));
        Mockito.doReturn("employeeDTO").when(userService).signup(Mockito.any(User.class));

        Response<String> response = userController.signup(userDataDTO, httpResponse);

        Mockito.verify(modelMapper, Mockito.times(1)).map(Mockito.any(UserDataDTO.class), Mockito.eq(User.class));
        Mockito.verify(userService, Mockito.times(1)).signup(Mockito.any(User.class));

        Assert.assertEquals(HttpStatus.CREATED.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getDesc(), response.getStatus().getMessage());

        Assert.assertNotNull(response.getData());

    }

    @Test
    public void testSignupInvalidRequestExceptionForUserNameMissing() {

        userDataDTO.setUsername("");

        Response<String> response = userController.signup(userDataDTO, httpResponse);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.USERNAME_IS_REQUIRED.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.USERNAME_IS_REQUIRED.getDesc(), response.getStatus().getMessage());

        Assert.assertNull(response.getData());

    }

    @Test
    public void testSignupInvalidRequestExceptionForEmailMissing() {

        userDataDTO.setEmail("");

        Response<String> response = userController.signup(userDataDTO, httpResponse);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMAIL_IS_REQUIRED.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMAIL_IS_REQUIRED.getDesc(), response.getStatus().getMessage());

        Assert.assertNull(response.getData());

    }

    @Test
    public void testSignupInvalidRequestExceptionForPasswordMissing() {

        userDataDTO.setPassword("");

        Response<String> response = userController.signup(userDataDTO, httpResponse);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.PASSWORD_IS_REQUIRED.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.PASSWORD_IS_REQUIRED.getDesc(), response.getStatus().getMessage());

        Assert.assertNull(response.getData());

    }

    @Test
    public void testSignupInvalidRequestExceptionForRoleMissing() {

        userDataDTO.setRoles(null);

        Response<String> response = userController.signup(userDataDTO, httpResponse);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.ROLE_IS_REQUIRED.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.ROLE_IS_REQUIRED.getDesc(), response.getStatus().getMessage());

        Assert.assertNull(response.getData());

    }

    @Test
    public void testSignupInvalidRequestExceptionForInvalidEmail() {

        userDataDTO.setEmail("blah");

        Response<String> response = userController.signup(userDataDTO, httpResponse);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMAIL_IS_INVALID.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMAIL_IS_INVALID.getDesc(), response.getStatus().getMessage());

        Assert.assertNull(response.getData());

    }

    @Test
    public void testSignupInvalidRequestExceptionForInvalidPassword() {

        userDataDTO.setPassword("blah");

        Response<String> response = userController.signup(userDataDTO, httpResponse);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.PASSWORD_IS_INVALID.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.PASSWORD_IS_INVALID.getDesc(), response.getStatus().getMessage());

        Assert.assertNull(response.getData());

    }

    @Test
    public void testSignupDuplicateEntityException() {

        Mockito.doReturn(user).when(modelMapper).map(Mockito.any(UserDataDTO.class), Mockito.eq(User.class));
        Mockito.doThrow(new DuplicateEntityException(StatusConstants.HttpConstants.USERNAME_ALREADY_EXISTS)).when(userService).signup(Mockito.any(User.class));

        Response<String> response = userController.signup(userDataDTO, httpResponse);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.USERNAME_ALREADY_EXISTS.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.USERNAME_ALREADY_EXISTS.getDesc(), response.getStatus().getMessage());

        Assert.assertNull(response.getData());

    }

    @Test
    public void testSignupUnknownException() {

        Mockito.doReturn(user).when(modelMapper).map(Mockito.any(UserDataDTO.class), Mockito.eq(User.class));
        Mockito.doThrow(Exception.class).when(userService).signup(Mockito.any(User.class));

        Response<String> response = userController.signup(userDataDTO, httpResponse);

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getDesc(), response.getStatus().getMessage());

        Assert.assertNull(response.getData());

    }

    @Test
    public void testLoginSuccess() {

        Mockito.doReturn("webtoken").when(userService).signin(Mockito.anyString(), Mockito.anyString());

        Response<String> response = userController.login(userSignInRequest, httpResponse);

        Mockito.verify(userService, Mockito.times(1)).signin(Mockito.anyString(), Mockito.anyString());

        Assert.assertEquals(HttpStatus.OK.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getDesc(), response.getStatus().getMessage());

        Assert.assertNotNull(response.getData());

    }

    @Test
    public void testLoginInvalidRequestExceptionForUserNameMissing() {

        userSignInRequest.setUsername("");

        Response<String> response = userController.login(userSignInRequest, httpResponse);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.USERNAME_IS_REQUIRED.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.USERNAME_IS_REQUIRED.getDesc(), response.getStatus().getMessage());

        Assert.assertNull(response.getData());

    }

    @Test
    public void testLoginInvalidRequestExceptionForPasswordMissing() {

        userSignInRequest.setPassword("");

        Response<String> response = userController.login(userSignInRequest, httpResponse);

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.PASSWORD_IS_REQUIRED.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.PASSWORD_IS_REQUIRED.getDesc(), response.getStatus().getMessage());

        Assert.assertNull(response.getData());

    }

    @Test
    public void testLoginWithCustomException() {

        Mockito.doThrow(new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY)).when(userService).signin(Mockito.anyString(), Mockito.anyString());

        Response<String> response = userController.login(userSignInRequest, httpResponse);

        Mockito.verify(userService, Mockito.times(1)).signin(Mockito.anyString(), Mockito.anyString());

        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.UNAUTHORIZED_ACCESS.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.UNAUTHORIZED_ACCESS.getDesc(), response.getStatus().getMessage());

        Assert.assertNull(response.getData());

    }

    @Test
    public void testLoginWithUnknownException() {

        Mockito.doThrow(Exception.class).when(userService).signin(Mockito.anyString(), Mockito.anyString());

        Response<String> response = userController.login(userSignInRequest, httpResponse);

        Mockito.verify(userService, Mockito.times(1)).signin(Mockito.anyString(), Mockito.anyString());

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getDesc(), response.getStatus().getMessage());

        Assert.assertNull(response.getData());

    }

}
