package amlan.user.controller;

import amlan.common.constant.StatusConstants;
import amlan.common.controller.ControllerSupport;
import amlan.common.exception.CustomException;
import amlan.common.exception.DuplicateEntityException;
import amlan.common.exception.InvalidRequestException;
import amlan.common.inteface.StringValidation;
import amlan.common.model.Response;
import amlan.user.dto.UserDataDTO;
import amlan.user.dto.UserSignInRequest;
import amlan.user.model.User;
import amlan.user.service.UserService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/users")
@Api(tags = "users")
public class UserController implements ControllerSupport {

    private static final Logger LOG = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/signup")
    @ApiOperation(value = "${UserController.signup}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 422, message = "Username is already in use")})
    public Response<String> signup(@ApiParam("Signup User") @RequestBody UserDataDTO user,
                                   HttpServletResponse response) {
        try {
            LOG.error("Start creating user with username: {}", user.getUsername());

            if (StringUtils.isEmpty(user.getUsername()))
                throw new InvalidRequestException(StatusConstants.HttpConstants.USERNAME_IS_REQUIRED);
            if (StringUtils.isEmpty(user.getEmail()))
                throw new InvalidRequestException(StatusConstants.HttpConstants.EMAIL_IS_REQUIRED);
            if (StringUtils.isEmpty(user.getPassword()))
                throw new InvalidRequestException(StatusConstants.HttpConstants.PASSWORD_IS_REQUIRED);
            if (user.getRoles() == null || (user.getRoles() != null && user.getRoles().isEmpty()))
                throw new InvalidRequestException(StatusConstants.HttpConstants.ROLE_IS_REQUIRED);

            StringValidation validation = amlan.common.utils.StringUtils::isValidEmail;
            if (!validation.isValid(user.getEmail()))
                throw new InvalidRequestException(StatusConstants.HttpConstants.EMAIL_IS_INVALID);

            validation = amlan.common.utils.StringUtils::isValidPassword;
            if (!validation.isValid(user.getPassword())) {
                throw new InvalidRequestException(StatusConstants.HttpConstants.PASSWORD_IS_INVALID);
            }
            return created(userService.signup(modelMapper.map(user, User.class)), response);
        } catch (InvalidRequestException e) {
            LOG.error("Failed creating user with bad request: {}, {}", e, e.getMessage());
            return badRequest(e.getStatus(), response);
        } catch (DuplicateEntityException e) {
            LOG.error("Failed creating user with bad request: {}, {}", e, e.getMessage());
            return badRequest(e.getStatus(), response);
        } catch (Exception e) {
            LOG.error("Failed creating user error: {}, {}", e, e.getMessage());
            return serverError(response);
        }
    }

    @PostMapping("/signin")
    @ApiOperation(value = "${UserController.signin}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public Response<String> login(@ApiParam("Signup User") @RequestBody UserSignInRequest user,
                                  HttpServletResponse response) {
        try {
            LOG.error("Start login user with username: {}", user.getUsername());
            if (StringUtils.isEmpty(user.getUsername()))
                throw new InvalidRequestException(StatusConstants.HttpConstants.USERNAME_IS_REQUIRED);
            if (StringUtils.isEmpty(user.getPassword()))
                throw new InvalidRequestException(StatusConstants.HttpConstants.PASSWORD_IS_REQUIRED);
            return success(userService.signin(user.getUsername(), user.getPassword()));
        } catch (InvalidRequestException e) {
            LOG.error("Failed login user with bad request: {}, {}", e, e.getMessage());
            return badRequest(e.getStatus(), response);
        } catch (CustomException e) {
            LOG.error("Failed login user with bad request: {}, {}", e, e.getMessage());
            return unauthorizedAccess(response);// 422 can be used as well but preferred to use 401
        } catch (Exception e) {
            LOG.error("Failed login user error: {}, {}", e, e.getMessage());
            return serverError(response);
        }

    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public Response<String> refresh(HttpServletRequest req) {
        return success(userService.refresh(req.getRemoteUser()));
    }


}
