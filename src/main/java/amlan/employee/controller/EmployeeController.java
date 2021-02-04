package amlan.employee.controller;

import amlan.common.constant.StatusConstants;
import amlan.common.controller.ControllerSupport;
import amlan.common.exception.DuplicateEntityException;
import amlan.common.exception.InvalidRequestException;
import amlan.common.exception.NotFoundException;
import amlan.common.model.Response;
import amlan.employee.dto.EmployeeDTO;
import amlan.employee.dto.EmployeeListDTO;
import amlan.employee.dto.EmployeeRequest;
import amlan.employee.dto.EmployeeResponse;
import amlan.employee.model.Employee;
import amlan.employee.service.EmployeeService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/employees")
@Api(tags = "employees")
public class EmployeeController implements ControllerSupport {

    private static final Logger LOG = LogManager.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${EmployeeController.get}")
    public Response<EmployeeResponse> getEmployee(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                  HttpServletResponse response) {

        LOG.info("Start getting employee page : {}, size {}", page, size);

        EmployeeListDTO employeeListDTO = this.employeeService.getEmployee(page, size);
        EmployeeResponse employeeResponse = EmployeeResponse.builder()
                .employees(employeeListDTO.getEmployees())
                .page(page)
                .size(size)
                .totalElementsInPage(employeeListDTO.getTotalElementsInPage())
                .totalPages(employeeListDTO.getTotalPages())
                .totalElements(employeeListDTO.getTotalElements())
                .build();

        LOG.info("Done getting employee page : {}, size {}", page, size);
        return success(employeeResponse);
    }

    @GetMapping("/{employeeId:.+}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${EmployeeController.getByEmployeeId}")
    public Response<EmployeeDTO> getEmployeeById(@PathVariable Integer employeeId,
                                                 HttpServletResponse response) {


        try {
            LOG.info("Start getting employee by employee id : {}", employeeId);
            EmployeeDTO employee = this.employeeService.getEmployee(employeeId);
            LOG.info("Done getting employee by employee id : {}", employeeId);
            return success(employee);
        } catch (NotFoundException e) {
            LOG.error("Failed getting employee by employee id : {} and error: {}, {}", employeeId, e.getStatus().getCode(), e.getStatus().getDesc());
            return notFound(e.getStatus(), response);
        } catch (Exception e) {
            LOG.error("Failed getting employee by employee id : {} and error: {}, {}", employeeId, e, e.getMessage());
            return serverError(response);
        }

    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "${EmployeeController.create}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 403, message = "Access denied")})
    public Response<EmployeeDTO> create(@ApiParam("Create employee") @RequestBody EmployeeRequest employee,
                                        HttpServletResponse response) {
        try {
            LOG.info("Start creating employee");
            if (StringUtils.isEmpty(employee.getFirstName())) {
                throw new InvalidRequestException(StatusConstants.HttpConstants.EMPLOYEE_FIRST_NAME_IS_REQUIRED);
            }
            if (StringUtils.isEmpty(employee.getLastName())) {
                throw new InvalidRequestException(StatusConstants.HttpConstants.EMPLOYEE_LAST_NAME_IS_REQUIRED);
            }
            if (StringUtils.isEmpty(employee.getEmail())) {
                throw new InvalidRequestException(StatusConstants.HttpConstants.EMPLOYEE_EMAIL_IS_REQUIRED);
            }
            EmployeeDTO employeeDTO = employeeService.create(modelMapper.map(employee, Employee.class));
            LOG.info("Done creating employee");
            return created(employeeDTO, response);
        } catch (InvalidRequestException e) {
            LOG.error("Failed creating employee with bad request: {}, {}", e, e.getMessage());
            return badRequest(e.getStatus(), response);
        } catch (DuplicateEntityException e) {
            LOG.error("Failed creating employee with bad request: {}, {}", e, e.getMessage());
            return badRequest(e.getStatus(), response);
        } catch (Exception e) {
            LOG.error("Failed creating employee error: {}, {}", e, e.getMessage());
            return serverError(response);
        }

    }

    @PutMapping("/{employeeId:.+}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "${EmployeeController.update}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 403, message = "Access denied")})
    public Response<EmployeeDTO> update(@PathVariable Integer employeeId,
                                        @ApiParam("Create employee") @RequestBody EmployeeRequest employee,
                                        HttpServletResponse response) {
        try {
            LOG.info("Start updating employee by employee id : {}", employeeId);
            if (StringUtils.isEmpty(employee.getFirstName())) {
                throw new InvalidRequestException(StatusConstants.HttpConstants.EMPLOYEE_FIRST_NAME_IS_REQUIRED);
            }
            if (StringUtils.isEmpty(employee.getLastName())) {
                throw new InvalidRequestException(StatusConstants.HttpConstants.EMPLOYEE_LAST_NAME_IS_REQUIRED);
            }
            if (StringUtils.isEmpty(employee.getEmail())) {
                throw new InvalidRequestException(StatusConstants.HttpConstants.EMPLOYEE_EMAIL_IS_REQUIRED);
            }
            EmployeeDTO employeeDTO = employeeService.update(employeeId, employee);
            LOG.info("Start updating employee by employee id : {}", employeeId);
            return updated(response);
        } catch (InvalidRequestException e) {
            LOG.error("Failed updating employee with bad request: {}, {}", e, e.getMessage());
            return badRequest(e.getStatus(), response);
        } catch (NotFoundException e) {
            LOG.error("Failed updating employee with bad request: {}, {}", e, e.getMessage());
            return badRequest(e.getStatus(), response);
        } catch (DuplicateEntityException e) {
            LOG.error("Failed updating employee with bad request: {}, {}", e, e.getMessage());
            return badRequest(e.getStatus(), response);
        } catch (Exception e) {
            LOG.error("Failed updating employee error: {}, {}", e, e.getMessage());
            return serverError(response);
        }

    }

    @DeleteMapping("/{employeeId:.+}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "${EmployeeController.delete}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 403, message = "Access denied")})
    public Response delete(@PathVariable Integer employeeId, HttpServletResponse response) {
        try {
            LOG.info("Start deleting employee by employee id : {}", employeeId);
            employeeService.delete(employeeId);
            LOG.info("Start deleting employee by employee id : {}", employeeId);
            return updated(response);
        } catch (NotFoundException e) {
            LOG.error("Failed deleting employee with bad request: {}, {}", e, e.getMessage());
            return badRequest(e.getStatus(), response);
        } catch (Exception e) {
            LOG.error("Failed deleting employee error: {}, {}", e, e.getMessage());
            return serverError(response);
        }

    }


}
