package amlan.employee.controller;

import amlan.common.constant.StatusConstants;
import amlan.common.exception.DuplicateEntityException;
import amlan.common.exception.NotFoundException;
import amlan.common.model.Response;
import amlan.employee.dto.EmployeeDTO;
import amlan.employee.dto.EmployeeListDTO;
import amlan.employee.dto.EmployeeRequest;
import amlan.employee.dto.EmployeeResponse;
import amlan.employee.model.Employee;
import amlan.employee.service.EmployeeService;
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
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private EmployeeService employeeService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private EmployeeController employeeController;
    private int page;
    private int size;
    private EmployeeListDTO employeeListDTO;
    private EmployeeDTO employeeDTO;

    private Integer employeeId;
    private Optional<Employee> employeeOptional;

    private EmployeeRequest employeeRequest;
    private Employee employee;
    private HttpServletResponse httpResponse;

    @Before
    public void setUp() {
        httpResponse = new MockHttpServletResponse();
        page = 0;
        size = 10;
        employeeDTO = EmployeeDTO.builder()
                .id(1)
                .firstName("oitejjho")
                .lastName("dutta")
                .email("oitejjho@gmail.com")
                .career("teacher")
                .build();
        List<EmployeeDTO> employeeDTOList = new ArrayList<EmployeeDTO>() {{
            add(employeeDTO);
        }};
        employeeListDTO = EmployeeListDTO.builder()
                .employees(employeeDTOList)
                .page(page)
                .size(size)
                .totalElementsInPage(employeeDTOList.size())
                .totalElements(employeeDTOList.size())
                .totalPages(1)
                .build();


        employeeRequest = EmployeeRequest.builder()
                .firstName("oitejjho")
                .lastName("dutta")
                .email("oitejjho@gmail.com")
                .career("teacher")
                .build();
        employee = new Employee(1, "oitejjho", "dutta", "oitejjho@gmail.com", "teacher");
    }


    @Test
    public void testGetEmployeeSuccess() {

        Mockito.doReturn(employeeListDTO).when(employeeService).getEmployee(page, size);

        Response<EmployeeResponse> response = employeeController.getEmployee(page, size, httpResponse);

        Mockito.verify(employeeService, Mockito.times(1)).getEmployee(page, size);

        Assert.assertEquals(response.getData().getEmployees().size(), 1);
        Assert.assertEquals(response.getData().getPage(), 0);
        Assert.assertEquals(response.getData().getSize(), 10);
        Assert.assertEquals(response.getData().getTotalElementsInPage(), 1);
        Assert.assertEquals(response.getData().getTotalPages(), 1);
        Assert.assertEquals(response.getData().getTotalElements(), 1);

    }

    @Test
    public void testGetEmployeeByIdSuccess() {

        Mockito.doReturn(employeeDTO).when(employeeService).getEmployee(Mockito.any(Integer.class));

        Response<EmployeeDTO> response = employeeController.getEmployeeById(employeeId, httpResponse);

        Mockito.verify(employeeService, Mockito.times(1)).getEmployee(Mockito.any(Integer.class));

        Assert.assertNotNull(response.getData());
        Assert.assertEquals(HttpStatus.OK.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testGetEmployeeByIdWithNotFoundException() {

        Mockito.doThrow(new NotFoundException(StatusConstants.HttpConstants.EMPLOYEE_DOES_NOT_EXIST)).when(employeeService).getEmployee(Mockito.anyInt());

        Response<EmployeeDTO> response = employeeController.getEmployeeById(employeeId, httpResponse);

        Mockito.verify(employeeService, Mockito.times(1)).getEmployee(Mockito.any(Integer.class));

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_DOES_NOT_EXIST.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_DOES_NOT_EXIST.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testGetEmployeeByIdWithException() {

        Mockito.doThrow(Exception.class).when(employeeService).getEmployee(Mockito.anyInt());

        Response<EmployeeDTO> response = employeeController.getEmployeeById(employeeId, httpResponse);

        Mockito.verify(employeeService, Mockito.times(1)).getEmployee(Mockito.any(Integer.class));

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testCreateSuccess() {

        Mockito.doReturn(employee).when(modelMapper).map(Mockito.any(EmployeeRequest.class), Mockito.eq(Employee.class));
        Mockito.doReturn(employeeDTO).when(employeeService).create(Mockito.any(Employee.class));

        Response<EmployeeDTO> response = employeeController.create(employeeRequest, httpResponse);

        Mockito.verify(modelMapper, Mockito.times(1)).map(Mockito.any(EmployeeRequest.class), Mockito.eq(Employee.class));
        Mockito.verify(employeeService, Mockito.times(1)).create(Mockito.any(Employee.class));

        Assert.assertNotNull(response.getData());
        Assert.assertEquals(HttpStatus.CREATED.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testCreateWithInvalidRequestExceptionForFirstNameMissing() {

        employeeRequest.setFirstName(null);

        Response<EmployeeDTO> response = employeeController.create(employeeRequest, httpResponse);

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_FIRST_NAME_IS_REQUIRED.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_FIRST_NAME_IS_REQUIRED.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testCreateWithInvalidRequestExceptionForLastNameMissing() {

        employeeRequest.setLastName(null);

        Response<EmployeeDTO> response = employeeController.create(employeeRequest, httpResponse);

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_LAST_NAME_IS_REQUIRED.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_LAST_NAME_IS_REQUIRED.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testCreateWithInvalidRequestExceptionForEmailMissing() {

        employeeRequest.setEmail(null);

        Response<EmployeeDTO> response = employeeController.create(employeeRequest, httpResponse);

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_EMAIL_IS_REQUIRED.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_EMAIL_IS_REQUIRED.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testCreateWithInvalidRequestExceptionForInvalidEmail() {

        employeeRequest.setEmail("blah blah");

        Response<EmployeeDTO> response = employeeController.create(employeeRequest, httpResponse);

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMAIL_IS_INVALID.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMAIL_IS_INVALID.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testCreateWithDuplicateEntityException() {

        Mockito.doReturn(employee).when(modelMapper).map(Mockito.any(EmployeeRequest.class), Mockito.eq(Employee.class));
        Mockito.doThrow(new DuplicateEntityException(StatusConstants.HttpConstants.EMPLOYEE_EMAIL_ALREADY_EXISTS)).when(employeeService).create(Mockito.any(Employee.class));

        Response<EmployeeDTO> response = employeeController.create(employeeRequest, httpResponse);

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_EMAIL_ALREADY_EXISTS.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_EMAIL_ALREADY_EXISTS.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testCreateWithUnknownException() {

        Mockito.doReturn(employee).when(modelMapper).map(Mockito.any(EmployeeRequest.class), Mockito.eq(Employee.class));
        Mockito.doThrow(Exception.class).when(employeeService).create(Mockito.any(Employee.class));

        Response<EmployeeDTO> response = employeeController.create(employeeRequest, httpResponse);

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testUpdateSuccess() {

        Mockito.doReturn(employeeDTO).when(employeeService).update(Mockito.any(Integer.class), Mockito.any(EmployeeRequest.class));

        Response response = employeeController.update(employeeId, employeeRequest, httpResponse);

        Mockito.verify(employeeService, Mockito.times(1)).update(Mockito.any(Integer.class), Mockito.any(EmployeeRequest.class));

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.NO_CONTENT.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testUpdateWithInvalidRequestExceptionForFirstNameMissing() {

        employeeRequest.setFirstName(null);

        Response response = employeeController.update(employeeId, employeeRequest, httpResponse);

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_FIRST_NAME_IS_REQUIRED.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_FIRST_NAME_IS_REQUIRED.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testUpdateWithInvalidRequestExceptionForLastNameMissing() {

        employeeRequest.setLastName(null);

        Response response = employeeController.update(employeeId, employeeRequest, httpResponse);

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_LAST_NAME_IS_REQUIRED.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_LAST_NAME_IS_REQUIRED.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testUpdateWithInvalidRequestExceptionForEmailMissing() {

        employeeRequest.setEmail(null);

        Response response = employeeController.update(employeeId, employeeRequest, httpResponse);

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_EMAIL_IS_REQUIRED.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_EMAIL_IS_REQUIRED.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testUpdateWithInvalidRequestExceptionForInvalidEmail() {

        employeeRequest.setEmail("blah blah");

        Response response = employeeController.update(employeeId, employeeRequest, httpResponse);

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMAIL_IS_INVALID.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMAIL_IS_INVALID.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testUpdateWithNotFoundException() {

        Mockito.doThrow(new NotFoundException(StatusConstants.HttpConstants.EMPLOYEE_DOES_NOT_EXIST)).when(employeeService).update(Mockito.any(Integer.class), Mockito.any(EmployeeRequest.class));

        Response response = employeeController.update(employeeId, employeeRequest, httpResponse);

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_DOES_NOT_EXIST.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_DOES_NOT_EXIST.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testUpdateWithDuplicateEntityException() {

        Mockito.doThrow(new DuplicateEntityException(StatusConstants.HttpConstants.EMPLOYEE_EMAIL_ALREADY_EXISTS)).when(employeeService).update(Mockito.any(Integer.class), Mockito.any(EmployeeRequest.class));

        Response response = employeeController.update(employeeId, employeeRequest, httpResponse);

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_EMAIL_ALREADY_EXISTS.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_EMAIL_ALREADY_EXISTS.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testUpdateWithUnknownException() {

        Mockito.doThrow(Exception.class).when(employeeService).update(Mockito.any(Integer.class), Mockito.any(EmployeeRequest.class));

        Response response = employeeController.update(employeeId, employeeRequest, httpResponse);

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testDeleteSuccess() {

        Mockito.doNothing().when(employeeService).delete(Mockito.any(Integer.class));

        Response response = employeeController.delete(employeeId, httpResponse);

        Mockito.verify(employeeService, Mockito.times(1)).delete(Mockito.any(Integer.class));

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.NO_CONTENT.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.SUCCESS.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testDeleteWithNotFoundException() {

        Mockito.doThrow(new NotFoundException(StatusConstants.HttpConstants.EMPLOYEE_DOES_NOT_EXIST)).when(employeeService).delete(Mockito.any(Integer.class));

        Response response = employeeController.delete(employeeId, httpResponse);

        Mockito.verify(employeeService, Mockito.times(1)).delete(Mockito.any(Integer.class));

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_DOES_NOT_EXIST.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.EMPLOYEE_DOES_NOT_EXIST.getDesc(), response.getStatus().getMessage());

    }

    @Test
    public void testDeleteWithUnknownException() {

        Mockito.doThrow(Exception.class).when(employeeService).delete(Mockito.any(Integer.class));

        Response response = employeeController.delete(employeeId, httpResponse);

        Mockito.verify(employeeService, Mockito.times(1)).delete(Mockito.any(Integer.class));

        Assert.assertNull(response.getData());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), httpResponse.getStatus());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getCode(), response.getStatus().getCode());
        Assert.assertEquals(StatusConstants.HttpConstants.INTERNAL_SERVER_ERROR.getDesc(), response.getStatus().getMessage());

    }

}
