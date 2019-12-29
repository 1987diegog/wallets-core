package uy.com.demente.ideas.wallets.view.resources;

import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import uy.com.demente.ideas.wallets.business.exceptions.*;
import uy.com.demente.ideas.wallets.business.services.SecurityService;
import uy.com.demente.ideas.wallets.business.services.TransferService;
import uy.com.demente.ideas.wallets.model.response.ListTransfersDTO;
import uy.com.demente.ideas.wallets.model.response.TransferDTO;

/**
 * @author 1987diegog
 */
@RestController
@RequestMapping("/api/v1/transfers")
@Api(tags = "Transfer")
public class TransferResource {

    Logger logger = LogManager.getLogger(TransferResource.class);

    private final TransferService transferService;

    @Autowired
    SecurityService securityService;

    public TransferResource(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping( //
            consumes = {MediaType.APPLICATION_JSON_VALUE}, //
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Create a transfer", //
            notes = "Service to transfer balance between two wallets")
    @ApiResponses(value = { //
            @ApiResponse(code = 201, message = "Transfer successful"),
            @ApiResponse(code = 400, message = "The information received is not correct"),
            @ApiResponse(code = 402, message = "Origin Wallet not have funds"),
            @ApiResponse(code = 500, message = "Internal system error")})
    public ResponseEntity<TransferDTO> addTransfer(@RequestHeader("Session-Token") String sessionToken,
                                                   @RequestBody TransferDTO transferDTO)
            throws NotFoundException, BadRequestException, PaymentRequiredException,
            UnauthorizedException, InternalServerErrorException {
        try {
            securityService.validateSessionToken(sessionToken);
            logger.info("[ADD_TRANSFER] - It will try to create the transfer, balance transfer: "
                    + transferDTO.getAmount() + ",origin wallet: " + transferDTO.getOriginWallet()
                    + ",destination wallet : " + transferDTO.getDestinationWallet());
            TransferDTO newTransferDTO = this.transferService.createTransfer(transferDTO);
            logger.info("[ADD_TRANSFER] - Transfer created successful");
            return new ResponseEntity<>(newTransferDTO, HttpStatus.CREATED);
        } catch (WalletNotFoundException e) {
            logger.error("[ADD_TRANSFER] [ERROR] - " + e.getLocalizedMessage());
            throw new BadRequestException(e.getLocalizedMessage());
        } catch (WalletMatchesException e) {
            logger.error("[ADD_TRANSFER] [ERROR] - " + e.getLocalizedMessage());
            throw new BadRequestException(e.getLocalizedMessage());
        } catch (TypeCoinDontMatchesException e) {
            logger.error("[ADD_TRANSFER] [ERROR] - " + e.getLocalizedMessage());
            throw new BadRequestException(e.getLocalizedMessage());
        } catch (InsufficientBalanceException e) {
            logger.error("[ADD_TRANSFER] [ERROR] - " + e.getLocalizedMessage());
            throw new PaymentRequiredException(e.getLocalizedMessage());
        } catch (UnauthorizedException e) {
            logger.error("[ADD_TRANSFER] [ERROR] - " + e.getLocalizedMessage());
            throw new UnauthorizedException(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error("[ADD_TRANSFER] [ERROR] - Internal system error when trying to make a transfer");
            throw new InternalServerErrorException(e.getLocalizedMessage());
        }
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Update transfer", //
            notes = "Service for update transfer")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Transfer updated successfully"), //
            @ApiResponse(code = 404, message = "Transfer not found"), //
            @ApiResponse(code = 500, message = "Internal system error")})
    public ResponseEntity<TransferDTO> update(@RequestBody TransferDTO transferDTO)
            throws NotFoundException, InternalServerErrorException {
        try {
            logger.info("[UPDATE_TRANSFER] - It will try to update transfer with id: " + transferDTO.getIdTransfer());
            TransferDTO response = transferService.update(transferDTO);
            logger.info("[UPDATE_TRANSFER] - Transfer updated successful, id: " + transferDTO.getIdTransfer());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (TransferNotFoundException e) {
            logger.error("[UPDATE_TRANSFER] [ERROR] - To try transfer with id: " + transferDTO.getIdTransfer());
            throw new NotFoundException(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error("[UPDATE_TRANSFER] [ERROR] - Internal server error, when trying " +
                    "to update transfer with id: "
                    + transferDTO.getIdTransfer());
            throw new InternalServerErrorException(e.getLocalizedMessage());
        }
    }

    @DeleteMapping(path = "/{id}", //
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Delete a Transfer", //
            notes = "Service to delete a Transfer")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Delete a transfer successfully"),
            @ApiResponse(code = 404, message = "Transfer not found"),
            @ApiResponse(code = 500, message = "Internal system error")})
    public ResponseEntity<String> delete(@PathVariable("id") Long id) throws NotFoundException, InternalServerErrorException {

        try {
            logger.info("[DELETE_TRANSFER] - It will try to delete a transfer with id: " + id);
            this.transferService.delete(id);
            logger.info("[DELETE_TRANSFER] - The transfer with id " + id + " was deleted successful");
            return new ResponseEntity<>("- The transfer with id " + id + " was deleted successful", HttpStatus.OK);
        } catch (TransferNotFoundException e) {
            throw new NotFoundException(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error("[DELETE_TRANSFER] [ERROR] - Internal system error when trying to get " +
                    "transfers with id: " + id);
            throw new InternalServerErrorException(e.getLocalizedMessage());
        }
    }

    //////////////////////////////////////////////////////////////////
    ///////////////////////////// QUERIES ////////////////////////////
    /////////////////////////////////////////////////////////////////

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Returns all Transfers filtered by the parameters received", //
            notes = "Service returns all Transfers filtered by the parameters received")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Transfers found"),
            @ApiResponse(code = 400, message = "The information received is not correct"),
            @ApiResponse(code = 404, message = "Transfers not found"),
            @ApiResponse(code = 500, message = "Internal system error")})
    public ResponseEntity<ListTransfersDTO> findByFilter(
            @ApiParam(name = "fromTimestamp", value = "Transactions from date") //
            @RequestParam(value = "fromTimestamp") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromTimestamp,
            @ApiParam(name = "toTimestamp", value = "Transactions to date") //
            @RequestParam(value = "toTimestamp") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toTimestamp,
            @ApiParam(name = "originWallet", value = "Transactions with origin wallet") //
            @RequestParam(value = "originWallet") Optional<String> originWallet)
            throws InternalServerErrorException {

        try {
            logger.info("[GET_ALL_TRANSFERS_FILTER] - It will try to return all system transfers filtered " +
                    "by: from date: " + fromTimestamp.toString() + ", to date: " + toTimestamp.toString() +
                    ", origin wallet: " + originWallet);
            ListTransfersDTO listTransfersDTO = this.transferService.findByFilter(fromTimestamp, toTimestamp,
                    originWallet);
            logger.info("[GET_ALL_TRANSFERS_FILTER] - Get all transfer finished successfully");
            return new ResponseEntity<>(listTransfersDTO, HttpStatus.OK);
        } catch (WalletNotFoundException e) {
            logger.info("[GET_ALL_TRANSFERS_FILTER] - " + e.getLocalizedMessage());
            return new ResponseEntity<>(new ListTransfersDTO(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[GET_ALL_TRANSFERS_FILTER] [ERROR] - Internal system error when trying " +
                    "to get transfers ");
            throw new InternalServerErrorException(e.getLocalizedMessage());
        }
    }

    @GetMapping(path = "/{id}", //
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Get a transfer by id", //
            notes = "Service to returns a transfer by id")
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Transfer found"),
            @ApiResponse(code = 404, message = "Transfer not found"),
            @ApiResponse(code = 500, message = "Internal system error")})
    public ResponseEntity<TransferDTO> findById(@PathVariable("id") Long id)
            throws NotFoundException, InternalServerErrorException {

        try {
            logger.info("[TRANSFER_FIND_BY_ID] - It will try to return a transfer by id: " + id);
            TransferDTO transferDTO = this.transferService.findById(id);
            logger.info("[TRANSFER_FIND_BY_ID] - Transfer found successful, id: " + id);
            return new ResponseEntity<>(transferDTO, HttpStatus.OK);
        } catch (TransferNotFoundException e) {
            logger.info("[TRANSFER_FIND_BY_ID] - Transfer not found, id: " + id);
            throw new NotFoundException(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error("[TRANSFER_FIND_BY_ID] [ERROR] - Internal system error when trying "
                    + "to get transfers with id: " + id);
            throw new InternalServerErrorException(e.getLocalizedMessage());
        }
    }
}
