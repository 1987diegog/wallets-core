package uy.com.demente.ideas.wallets.view.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import uy.com.demente.ideas.wallets.exceptions.InternalServerErrorException;
import uy.com.demente.ideas.wallets.exceptions.NotFoundException;
import uy.com.demente.ideas.wallets.exceptions.UserNotFoundException;
import uy.com.demente.ideas.wallets.services.UserService;
import uy.com.demente.ideas.wallets.services.WalletService;
import uy.com.demente.ideas.wallets.dtos.ListUsersDTO;
import uy.com.demente.ideas.wallets.dtos.ListWalletsDTO;
import uy.com.demente.ideas.wallets.dtos.UserDTO;

import javax.validation.Valid;

/**
 * @author 1987diegog
 */
@RestController
@RequestMapping("/api/v1/users")
@Api(tags = "User")
public class UserResource {

    private Logger logger = LogManager.getLogger(UserResource.class);

    private final UserService userService;
    private final WalletService walletService;

    public UserResource(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }

    @PostMapping(//
            consumes = {MediaType.APPLICATION_JSON_VALUE}, //
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Create a user", //
            notes = "Service to create a user")
    @ApiResponses(value = { //
            @ApiResponse(code = 201, message = "User created successfully"), //
            @ApiResponse(code = 500, message = "Internal system error")})
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO userDTO)
            throws InternalServerErrorException {
        try {
            logger.info("[CREATE_USER] - It will try to create the user with email: " + userDTO.getEmail());
            UserDTO response = userService.create(userDTO);
            logger.info("[CREATE_USER] - User created successful, the associated id was: " + response.getIdUser()
                    + ", email: " + userDTO.getEmail());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("[CREATE_USER] [ERROR] - Internal server error, when trying to create user with email: "
                    + userDTO.getEmail(), e);
            throw new InternalServerErrorException("Internal server error, when trying to create user with email: " +
                    userDTO.getEmail());
        }
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Update user data", //
            notes = "Service for update user data")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "User updated successfully"), //
            @ApiResponse(code = 404, message = "User not found"), //
            @ApiResponse(code = 500, message = "Internal system error")})
    public ResponseEntity<UserDTO> update(@Valid @RequestBody UserDTO userDTO)
            throws NotFoundException, InternalServerErrorException {
        try {
            logger.info("[UPDATE_USER] - It will try to update the user with id: " + userDTO.getIdUser());
            UserDTO response = userService.update(userDTO);
            logger.info("[UPDATE_USER] - User updated successful, id: " + userDTO.getIdUser());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            logger.info("[UPDATE_USER] [NOT_FOUND] - User not found, id: " + userDTO.getIdUser());
            throw new NotFoundException("User not found, id: " + userDTO.getIdUser());
        } catch (Exception e) {
            logger.error("[UPDATE_USER] [ERROR] - Internal server error, when trying to update user data with id: "
                    + userDTO.getIdUser(), e);
            throw new InternalServerErrorException("Internal server error, when trying to update user data with id: "
                    + userDTO.getIdUser());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a user", //
            notes = "Service to delete a user")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "User deleted successful"), //
            @ApiResponse(code = 404, message = "User not found"), //
            @ApiResponse(code = 500, message = "Internal system error")})
    public ResponseEntity<String> delete(@PathVariable("id") Long id)
            throws NotFoundException, InternalServerErrorException {
        try {
            logger.info("[DELETE_USER] - It will try to delete user with id: " + id);
            this.userService.delete(id);
            logger.info("[DELETE_USER] - User was deleted successful, id: " + id);
            return new ResponseEntity<>("User was deleted successful, id:" + id, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            logger.info("[DELETE_USER] [NOT_FOUND] - User not found, id: " + id);
            throw new NotFoundException("User not found, id: " + id);
        } catch (Exception e) {
            logger.error("[DELETE_USER] [ERROR] - Internal system error when trying to delete user whit id: " + id, e);
            throw new InternalServerErrorException("Internal system error when trying to delete user whit id: " + id);
        }
    }

    //////////////////////////////////////////////////////////////////
    ///////////////////////////// QUERIES ////////////////////////////
    /////////////////////////////////////////////////////////////////

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Returns all users", //
            notes = "Service returns all users")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Users found"), //
            @ApiResponse(code = 500, message = "Internal system error")})
    public ResponseEntity<ListUsersDTO> findAll()
            throws InternalServerErrorException {
        try {
            logger.info("[GET_ALL_USERS] - It will try to return all system users...");
            ListUsersDTO listUsersDTO = this.userService.findAll();
            logger.info("[GET_ALL_USERS] - Get all users finished successfully ");
            return new ResponseEntity<>(listUsersDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[GET_ALL_USERS] [ERROR] - Internal system error, when trying to get all users", e);
            throw new InternalServerErrorException("Internal server error, when trying to get all users");
        }
    }

    @GetMapping(path = "/{id}", //
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Get a user by id", //
            notes = "Service to returns a user by id")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "User found"), //
            @ApiResponse(code = 404, message = "User not found"), //
            @ApiResponse(code = 500, message = "Internal system error")})
    public ResponseEntity<UserDTO> findById(@PathVariable("id") Long id)
            throws NotFoundException, InternalServerErrorException {
        try {
            logger.info("[FIND_BY_USER_ID] - It will try to return a user by id: " + id);
            UserDTO usersDTO = this.userService.findById(id);
            logger.info("[FIND_BY_USER_ID] - User found successful, id: " + id);
            return new ResponseEntity<>(usersDTO, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            logger.info("[FIND_BY_USER_ID] [NOT_FOUND] - User not found, id: " + id);
            throw new NotFoundException("User not found, id: " + id);
        } catch (Exception e) {
            logger.error("[FIND_BY_USER_ID] [ERROR] - To try get user with id: " + id, e);
            throw new InternalServerErrorException("Internal server error, when trying to get user with id: " + id);
        }
    }

    @GetMapping(path = "/{id}/wallets", //
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Returns all user wallets", //
            notes = "Service returns all users wallets")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "User wallets found"), //
            @ApiResponse(code = 500, message = "Internal system error")})
    public ResponseEntity<ListWalletsDTO> findWalletsByUser(@PathVariable("id") Long id)
            throws InternalServerErrorException {
        try {
            logger.info("[FIND_ALL_WALLETS_BY_USER] - It will try to return all system user wallets...");
            ListWalletsDTO listWalletsDTO = this.walletService.findByUser(id);
            logger.info("[FIND_ALL_WALLETS_BY_USER] - Get all user wallets finished successfully ");
            return new ResponseEntity<>(listWalletsDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[FIND_ALL_WALLETS_BY_USER] [ERROR] - Internal system error, when trying to get all user wallets", e);
            throw new InternalServerErrorException("Internal system error, when trying to get all user wallets");
        }
    }
}