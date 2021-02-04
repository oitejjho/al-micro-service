package amlan.employee.service;

import amlan.common.constant.StatusConstants;
import amlan.common.exception.DuplicateEntityException;
import amlan.common.exception.NotFoundException;
import amlan.employee.dto.EmployeeDTO;
import amlan.employee.dto.EmployeeListDTO;
import amlan.employee.dto.EmployeeRequest;
import amlan.employee.model.Employee;
import amlan.employee.repository.EmployeeRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private EmployeeService employeeService;
    private int page;
    private int size;
    private Page<Employee> employeePage;
    private EmployeeDTO employeeDTO;

    private Integer employeeId;
    private Optional<Employee> employeeOptional;

    @Before
    public void setUp() {
        page = 0;
        size = 10;
        employeePage = new PageImpl<>(new ArrayList<Employee>() {{
            add(new Employee(1, "oitejjho", "dutta", "oitejjho@gmail.com", "teacher"));
        }});
        employeeDTO = EmployeeDTO.builder()
                .id(1)
                .firstName("oitejjho")
                .lastName("dutta")
                .email("oitejjho@gmail.com")
                .career("teacher")
                .build();

        employeeId = 1;
        employeeOptional = Optional.of(new Employee(1, "oitejjho", "dutta", "oitejjho@gmail.com", "teacher"));

    }


    @Test
    public void testGetEmployeeSuccess() {

        Mockito.doReturn(employeePage).when(employeeRepository).findAll(Mockito.any(Pageable.class));
        Mockito.doReturn(employeeDTO).when(modelMapper).map(Mockito.any(Employee.class), Mockito.eq(EmployeeDTO.class));

        EmployeeListDTO result = employeeService.getEmployee(page, size);

        Mockito.verify(employeeRepository, Mockito.times(1)).findAll(Mockito.any(Pageable.class));
        Mockito.verify(modelMapper, Mockito.times(1)).map(Mockito.any(Employee.class), Mockito.eq(EmployeeDTO.class));

        Assert.assertEquals(result.getEmployees().size(), 1);
        Assert.assertEquals(result.getPage(), 0);
        Assert.assertEquals(result.getSize(), 1);
        Assert.assertEquals(result.getTotalElementsInPage(), 1);
        Assert.assertEquals(result.getTotalPages(), 1);
        Assert.assertEquals(result.getTotalElements(), 1);

    }

    @Test
    public void testGetEmployeeSuccessWithEmpty() {

        employeePage = new PageImpl<>(new ArrayList<Employee>() {{
        }});
        Mockito.doReturn(employeePage).when(employeeRepository).findAll(Mockito.any(Pageable.class));
        Mockito.doReturn(employeeDTO).when(modelMapper).map(Mockito.any(Employee.class), Mockito.eq(EmployeeDTO.class));

        EmployeeListDTO result = employeeService.getEmployee(page, size);

        Mockito.verify(employeeRepository, Mockito.times(1)).findAll(Mockito.any(Pageable.class));
        Mockito.verify(modelMapper, Mockito.times(0)).map(Mockito.any(Employee.class), Mockito.eq(EmployeeDTO.class));

        Assert.assertEquals(result.getEmployees().size(), 0);
        Assert.assertEquals(result.getPage(), 0);
        Assert.assertEquals(result.getSize(), 0);
        Assert.assertEquals(result.getTotalElementsInPage(), 0);
        Assert.assertEquals(result.getTotalPages(), 1);
        Assert.assertEquals(result.getTotalElements(), 0);

    }

    @Test
    public void testGetEmployeeEmployeeIdSuccess() {

        Mockito.doReturn(employeeOptional).when(employeeRepository).findById(Mockito.any(Integer.class));
        Mockito.doReturn(employeeDTO).when(modelMapper).map(Mockito.any(Employee.class), Mockito.eq(EmployeeDTO.class));

        EmployeeDTO result = employeeService.getEmployee(employeeId);

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(Mockito.any(Integer.class));
        Mockito.verify(modelMapper, Mockito.times(1)).map(Mockito.any(Employee.class), Mockito.eq(EmployeeDTO.class));

    }

    @Test
    public void testGetEmployeeEmployeeIdWithNotFoundException() {

        employeeOptional = Optional.empty();
        Mockito.doReturn(employeeOptional).when(employeeRepository).findById(Mockito.any(Integer.class));
        Mockito.doReturn(employeeDTO).when(modelMapper).map(Mockito.any(Employee.class), Mockito.eq(EmployeeDTO.class));

        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.EMPLOYEE_DOES_NOT_EXIST.getDesc());
        EmployeeDTO result = employeeService.getEmployee(employeeId);

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(Mockito.any(Integer.class));
        Mockito.verify(modelMapper, Mockito.times(1)).map(Mockito.any(Employee.class), Mockito.eq(EmployeeDTO.class));

    }

    @Test
    public void testCreateSuccess() {

        employeeOptional = Optional.empty();
        Mockito.doReturn(employeeOptional).when(employeeRepository).findByEmail(Mockito.any(String.class));
        Mockito.doReturn(employeeDTO).when(modelMapper).map(Mockito.any(Employee.class), Mockito.eq(EmployeeDTO.class));

        EmployeeDTO result = employeeService.create(new Employee(1, "oitejjho", "dutta", "oitejjho@gmail.com", "teacher"));

        Mockito.verify(employeeRepository, Mockito.times(1)).findByEmail(Mockito.any(String.class));
        Mockito.verify(modelMapper, Mockito.times(1)).map(Mockito.any(Employee.class), Mockito.eq(EmployeeDTO.class));

    }

    @Test
    public void testCreateSuccessWithDuplicateEntityException() {

        Mockito.doReturn(employeeOptional).when(employeeRepository).findByEmail(Mockito.any(String.class));

        expectedException.expect(DuplicateEntityException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.EMPLOYEE_EMAIL_ALREADY_EXISTS.getDesc());
        EmployeeDTO result = employeeService.create(new Employee(1, "oitejjho", "dutta", "oitejjho@gmail.com", "teacher"));

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(Mockito.any(Integer.class));
        Mockito.verify(modelMapper, Mockito.times(0)).map(Mockito.any(Employee.class), Mockito.eq(EmployeeDTO.class));

    }

    @Test
    public void testUpdateSuccess() {

        Mockito.doReturn(employeeOptional).when(employeeRepository).findById(Mockito.any(Integer.class));
        Mockito.doReturn(Optional.empty()).when(employeeRepository).findByEmail(Mockito.any(String.class));
        Mockito.doReturn(new Employee(1, "oitejjho", "dutta", "oitejjho@gmail.com", "teacher")).when(employeeRepository).save(Mockito.any(Employee.class));
        Mockito.doReturn(employeeDTO).when(modelMapper).map(Mockito.any(Employee.class), Mockito.eq(EmployeeDTO.class));

        EmployeeDTO result = employeeService.update(employeeId, new EmployeeRequest("oitejjho","dutta","oitejjho@gmail.com","teacher"));

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(Mockito.any(Integer.class));
        Mockito.verify(employeeRepository, Mockito.times(0)).findByEmail(Mockito.any(String.class));
        Mockito.verify(employeeRepository, Mockito.times(1)).save(Mockito.any(Employee.class));
        Mockito.verify(modelMapper, Mockito.times(1)).map(Mockito.any(Employee.class), Mockito.eq(EmployeeDTO.class));

    }

    @Test
    public void testUpdateWithNotFoundException() {

        Mockito.doReturn(Optional.empty()).when(employeeRepository).findById(Mockito.any(Integer.class));

        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.EMPLOYEE_DOES_NOT_EXIST.getDesc());
        EmployeeDTO result = employeeService.update(employeeId, new EmployeeRequest("oitejjho","dutta","oitejjho@gmail.com","teacher"));

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(Mockito.any(Integer.class));

    }

    @Test
    public void testUpdateWithDuplicateEntityException() {

        Mockito.doReturn(employeeOptional).when(employeeRepository).findById(Mockito.any(Integer.class));
        Mockito.doReturn(Optional.of(new Employee(1, "oitejjho", "dutta", "oitejjho1@gmail.com", "teacher"))).when(employeeRepository).findByEmail(Mockito.any(String.class));

        expectedException.expect(DuplicateEntityException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.EMPLOYEE_EMAIL_ALREADY_EXISTS.getDesc());
        EmployeeDTO result = employeeService.update(employeeId, new EmployeeRequest("oitejjho","dutta","oitejjho1@gmail.com","teacher"));

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(Mockito.any(Integer.class));
        Mockito.verify(employeeRepository, Mockito.times(0)).findByEmail(Mockito.any(String.class));

    }

    @Test
    public void testDeleteSuccess() {

        Mockito.doReturn(employeeOptional).when(employeeRepository).findById(Mockito.any(Integer.class));
        Mockito.doNothing().when(employeeRepository).deleteById(Mockito.any(Integer.class));

        employeeService.delete(employeeId);

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(Mockito.any(Integer.class));
        Mockito.verify(employeeRepository, Mockito.times(1)).deleteById(Mockito.any(Integer.class));

    }

    @Test
    public void testDeleteWithNotFoundException() {

        Mockito.doReturn(Optional.empty()).when(employeeRepository).findById(Mockito.any(Integer.class));

        expectedException.expect(NotFoundException.class);
        expectedException.expectMessage(StatusConstants.HttpConstants.EMPLOYEE_DOES_NOT_EXIST.getDesc());
        employeeService.delete(employeeId);

        Mockito.verify(employeeRepository, Mockito.times(1)).findById(Mockito.any(Integer.class));

    }


}
