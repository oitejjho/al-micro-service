package amlan.employee.controller;

import amlan.common.controller.ControllerSupport;
import amlan.common.exception.ServiceException;
import amlan.common.model.Response;
import amlan.employee.dto.EmployeeDTO;
import amlan.employee.dto.EmployeeListDTO;
import amlan.employee.dto.EmployeeResponse;
import amlan.employee.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${EmployeeController.get}")
    public Response<EmployeeDTO> getEmployee(@PathVariable Integer employeeId,
                                             HttpServletResponse response) {


        try {
            LOG.info("Start getting employee by employee id : {}", employeeId);
            EmployeeDTO employee = this.employeeService.getEmployee(employeeId);
            LOG.info("Done getting employee by employee id : {}", employeeId);
            return success(employee);
        } catch (ServiceException e) {
            LOG.error("Failed getting employee by employee id : {} and error: {}, {}", employeeId, e.getStatus().getCode(), e.getStatus().getDesc());
            return serverError(e.getStatus(), response);
        } catch (Exception e) {
            LOG.error("Failed getting employee by employee id : {} and error: {}, {}", employeeId, e, e.getMessage());
            return serverError(response);
        }

    }

    /*@PostMapping("/create")
    @ApiOperation(value = "${EmployeeController.create}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Username is already in use")})
    public String create(@ApiParam("Signup User") @RequestBody UserDataDTO user) {
        return userService.signup(modelMapper.map(user, User.class));
    }

    @PostMapping("/signin")
    @ApiOperation(value = "${UserController.signin}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public String login(@ApiParam("Username") @RequestParam String username,
                        @ApiParam("Password") @RequestParam String password) {
        return userService.signin(username, password);
    }*/

  /*@DeleteMapping(value = "/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${UserController.delete}", authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public String delete(@ApiParam("Username") @PathVariable String username) {
    userService.delete(username);
    return username;
  }*/

  /*@GetMapping(value = "/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "${UserController.search}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 404, message = "The user doesn't exist"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public UserResponseDTO search(@ApiParam("Username") @PathVariable String username) {
    return modelMapper.map(userService.search(username), UserResponseDTO.class);
  }*/

  /*@GetMapping(value = "/me")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
  @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = "Something went wrong"), //
      @ApiResponse(code = 403, message = "Access denied"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public UserResponseDTO whoami(HttpServletRequest req) {
    return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
  }*/


}
