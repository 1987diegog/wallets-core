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
import uy.com.demente.ideas.wallets.business.exceptions.InternalServerErrorException;
import uy.com.demente.ideas.wallets.business.exceptions.WalletNotFoundException;
import uy.com.demente.ideas.wallets.business.services.WalletService;
import uy.com.demente.ideas.wallets.model.response.ListWalletsDTO;
import uy.com.demente.ideas.wallets.model.response.WalletDTO;

/**
 * @author 1987diegog
 */
@RestController
@RequestMapping("/api/v1/wallets")
@Api(tags = "Wallet")
public class WalletResource {

    Logger logger = LogManager.getLogger(WalletResource.class);

    private final WalletService walletService;

    public WalletResource(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping( //
            consumes = {MediaType.APPLICATION_JSON_VALUE}, //
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Create a wallet", //
            notes = "Service to create a wallet")
    @ApiResponses(value = { //
            @ApiResponse(code = 201, message = "Wallet created successfully"), //
            @ApiResponse(code = 400, message = "The information received is not correct"), //
            @ApiResponse(code = 500, message = "Internal system error")})
    public ResponseEntity<WalletDTO> create(@RequestBody WalletDTO walletDTO) throws InternalServerErrorException {

        try {
            logger.info("[CREATE_WALLET] - It will try to create the wallet with name: " + walletDTO.getName());
            WalletDTO response = walletService.create(walletDTO);
            logger.info(
                    "[CREATE_WALLET] - Wallet created successful, the associated id was: " + response.getIdWallet());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("[CREATE_WALLET] -[ERROR] - Internal server error, when trying to create the wallet with name: " + walletDTO.getName(), e);
            throw new InternalServerErrorException("Internal server error, when trying to create the wallet with name: " + walletDTO.getName());
        }
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Update wallet", //
            notes = "Service for update wallet")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Wallet updated successfully"), //
            @ApiResponse(code = 404, message = "Wallet not found"), //
            @ApiResponse(code = 500, message = "Internal system error")})
    public ResponseEntity<WalletDTO> update(@RequestBody WalletDTO walletDTO) throws InternalServerErrorException, WalletNotFoundException {

        try {
            logger.info("[UPDATE_WALLET] - It will try to update wallet with id: " + walletDTO.getIdWallet());
            WalletDTO response = walletService.update(walletDTO);
            logger.info("[UPDATE_WALLET] - Wallet updated successful, id: " + walletDTO.getIdWallet());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (WalletNotFoundException e) {
            logger.info("[UPDATE_WALLET] [NOT_FOUND] - Wallet not found, id: " + walletDTO.getIdWallet());
            throw new WalletNotFoundException("Wallet not found, id: " + walletDTO.getIdWallet());
        } catch (Exception e) {
            logger.error("[UPDATE_WALLET] [ERROR] - Internal server error, when trying to update wallet with id: "
                    + walletDTO.getIdWallet(), e);
            throw new InternalServerErrorException("Internal server error, when trying to update wallet with id: " + walletDTO.getIdWallet());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a wallet", //
            notes = "Service to delete a wallet")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Wallet deleted successful"), //
            @ApiResponse(code = 404, message = "Wallet not found"), //
            @ApiResponse(code = 500, message = "Internal system error")})
    public ResponseEntity<String> delete(@PathVariable("id") Long id) throws InternalServerErrorException, WalletNotFoundException {

        try {
            logger.info("[DELETE_WALLET] - It will try to delete wallet with id: " + id);
            this.walletService.delete(id);
            logger.info("[DELETE_WALLET] - Wallet was deleted successful, id: " + id);
            return new ResponseEntity<>("Wallet was deleted successful, id:", HttpStatus.OK);
        } catch (WalletNotFoundException e) {
            logger.info("[DELETE_WALLET] [NOT_FOUND] - Wallet not found, id: " + id);
            throw new WalletNotFoundException("Wallet not found, id: " + id);
        } catch (Exception e) {
            logger.error("[DELETE_WALLET] [ERROR] - Internal system error, when trying to delete wallet whit id: " + id, e);
            throw new InternalServerErrorException("Internal system error, when trying to delete wallet whit id: " + id);
        }
    }

    //////////////////////////////////////////////////////////////////
    ///////////////////////////// QUERIES ////////////////////////////
    /////////////////////////////////////////////////////////////////

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Returns all wallets", //
            notes = "Service returns all wallets")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Wallets found"), //
            @ApiResponse(code = 500, message = "Internal system error")})
    public ResponseEntity<ListWalletsDTO> findAll() throws InternalServerErrorException {

        try {
            logger.info("[GET_ALL_WALLETS] - It will try to return all system wallets...");
            ListWalletsDTO listWalletsDTO = this.walletService.findAll();
            logger.info("[GET_ALL_WALLETS] - Get all wallets finished successfully");
            return new ResponseEntity<>(listWalletsDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[GET_ALL_WALLETS] [ERROR] - Internal system error when trying to get all wallets", e);
            throw new InternalServerErrorException("Internal system error, when trying to get all wallets");
        }
    }

    @GetMapping("/{hash}")
    @ApiOperation(value = "Get a user by hash", //
            notes = "Service to returns a user by hash")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Wallet found"), //
            @ApiResponse(code = 404, message = "Wallet not found"), //
            @ApiResponse(code = 500, message = "Internal system error")})
    public ResponseEntity<WalletDTO> findByHash(@PathVariable("hash") String hash) throws InternalServerErrorException, WalletNotFoundException {

        try {
            logger.info("[FIND_BY_HASH] - It will try to return a wallet by hash: " + hash);
            WalletDTO walletDTO = this.walletService.findByHash(hash);
            logger.info("[FIND_BY_HASH] - Wallet found successful, hash: " + hash);
            return new ResponseEntity<>(walletDTO, HttpStatus.OK);
        } catch (WalletNotFoundException e) {
            logger.info("[FIND_BY_HASH] [NOT_FOUND] - Wallet not found, hash: " + hash);
            throw new WalletNotFoundException("Wallet not found, hash: " + hash);
        } catch (Exception e) {
            logger.error("[FIND_BY_HASH] [ERROR] - To try get wallet with hash: " + hash, e);
            throw new InternalServerErrorException("Internal system error, when trying to get wallet with hash: " + hash);
        }
    }
}
