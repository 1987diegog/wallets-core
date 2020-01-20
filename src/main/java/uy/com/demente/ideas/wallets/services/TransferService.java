package uy.com.demente.ideas.wallets.services;

import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uy.com.demente.ideas.wallets.exceptions.*;
import uy.com.demente.ideas.wallets.repository.ITransferRepository;
import uy.com.demente.ideas.wallets.repository.IWalletRepository;
import uy.com.demente.ideas.wallets.factorys.BOFactory;
import uy.com.demente.ideas.wallets.model.Transfer;
import uy.com.demente.ideas.wallets.model.TypeCoin;
import uy.com.demente.ideas.wallets.model.Wallet;
import uy.com.demente.ideas.wallets.dtos.ListTransfersDTO;
import uy.com.demente.ideas.wallets.dtos.TransferDTO;
import uy.com.demente.ideas.wallets.utils.DateUtil;
import uy.com.demente.ideas.wallets.factorys.DTOFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 1987diegog
 */
@Service
@Transactional(readOnly = true)
public class TransferService {

    Logger logger = LogManager.getLogger(TransferService.class);

    private final ITransferRepository transferRepository;
    private final IWalletRepository walletRepository;

    public TransferService(ITransferRepository transferRepository, IWalletRepository walletRepository) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
    }

    /**
     * @param transferDTO
     * @return
     * @throws WalletNotFoundException
     * @throws InsufficientBalanceException
     * @throws TypeCoinException
     * @throws WalletMatchesException
     */
    @Transactional
    public TransferDTO createTransfer(TransferDTO transferDTO)
            throws WalletNotFoundException, InsufficientBalanceException, TypeCoinException,
            WalletMatchesException, BussinesServiceException {

        try {

            logger.info("[CREATE_TRANSFER] - Origin wallet with hash: " + transferDTO.getOriginWallet() //
                    + ", destination wallet with hash: " + transferDTO.getDestinationWallet() + ", amount to transfer: "
                    + transferDTO.getAmount());

            Wallet originWallet = this.getWalletByHash(transferDTO.getOriginWallet());
            logger.info("[CREATE_TRANSFER] - The original wallet was obtained successfully...");

            Wallet destinationWallet = this.getWalletByHash(transferDTO.getDestinationWallet());
            logger.info("[CREATE_TRANSFER] - The destination wallet was obtained successfully...");

            // Validate if is possible the transfer
            this.validateTransfer(originWallet, destinationWallet, transferDTO.getAmount(),
                    transferDTO.getTypeCoin());
            logger.info("[CREATE_TRANSFER] - The validation was successful...");

            Transfer transfer = this.makeTransfer(originWallet, destinationWallet, transferDTO);
            logger.info("[CREATE_TRANSFER] - The creation of the transfer was successful...");


            return DTOFactory.create(transfer);

        } catch (WalletNotFoundException | WalletMatchesException |
                TypeCoinException | InsufficientBalanceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("[CREATE_TRANSFER] [ERROR] - When trying to create the object using factoryDTO", e);
            throw new BussinesServiceException("When trying to create the object using factoryDTO", e);
        }
    }

    /**
     * @param origin
     * @param destination
     * @param amount
     * @param typeCoin
     * @throws WalletMatchesException
     * @throws TypeCoinException
     * @throws InsufficientBalanceException
     */
    public void validateTransfer(Wallet origin, Wallet destination, BigDecimal amount, String typeCoin)
            throws WalletMatchesException, TypeCoinException, InsufficientBalanceException {

        /////////////////////////////////////////////////
        ////////////////// - WALLETS - //////////////////
        /////////////////////////////////////////////////

        if (origin.getIdWallet().equals(destination.getIdWallet()) == false) {
            logger.info("[VALIDATE_TRANSFER] - The origin and destination wallets " +
                    "are different, proceed...");
        } else {
            logger.info("[VALIDATE_TRANSFER] - The origin wallet is equal to the destination " +
                    "wallet, the transfer makes no sense");
            throw new WalletMatchesException("The origin wallet is equal to the destination " +
                    "wallet, the transfer makes no sense");
        }

        /////////////////////////////////////////////////
        ///////////////// - TYPE COIN - /////////////////
        /////////////////////////////////////////////////

        if(TypeCoin.get(typeCoin) == null) {
            logger.info("[VALIDATE_TRANSFER] - The type of currency does not exist");
            throw new TypeCoinException("The type of currency does not exist");
        }

        if (origin.getTypeCoin().equals(destination.getTypeCoin())) {
            logger.info("[VALIDATE_TRANSFER] - The type of currency is the same, proceed...");
        } else {
            // ------------------------------------
            // ----- this.conversionCurrency(); --------
            // ------------------------------------
            logger.info("[VALIDATE_TRANSFER] - The type of currency is different");
            throw new TypeCoinException("The type of currency is different");
        }

        /////////////////////////////////////////////////
        ////////////////// - AMOUNT - ///////////////////
        /////////////////////////////////////////////////

        // Check if the balance of the original wallet is greater than or equal to the
        // one you want to transfer ...
        if (origin.getBalance().compareTo(amount) >= 0) {
            logger.info("[VALIDATE_TRANSFER] - The original wallet has the same " +
                    "or more balance as requested in the transfer, proceed...");
        } else {
            logger.info("[VALIDATE_TRANSFER] - The balance to be transferred is greater " +
                    "than balance of the original wallet");
            throw new InsufficientBalanceException("The balance to be transferred is greater " +
                    "than balance of the original wallet");
        }
    }

    /**
     *
     */
    public void conversionCurrency(BigDecimal amount, TypeCoin fromCoin, TypeCoin toCoin) {
        // TODO: If contemplated, a currency transformation could be made, for example
        // from PAGACOIN to DOLLARS and then from DOLLARS to BITCOIN
    }

    /**
     * @param origin
     * @param destination
     * @param transferDTO
     * @return
     * @throws BussinesServiceException
     */
    public Transfer makeTransfer(Wallet origin, Wallet destination, TransferDTO transferDTO)
            throws BussinesServiceException {

        try {

            logger.info("[MAKE_TRANSFER] - Balance origin wallet before transfer: " + origin.getBalance());
            BigDecimal origenWalletBalanceAfterTranfer = origin.getBalance().subtract(transferDTO.getAmount());
            origin.setBalance(origenWalletBalanceAfterTranfer);
            logger.info("[MAKE_TRANSFER] - Balance origin wallet after transfer: " + origin.getBalance());

            logger.info("[MAKE_TRANSFER] - Balance destination wallet before transfer: " + destination.getBalance());
            BigDecimal destinationWalletBalanceAfterTranfer = destination.getBalance().add(transferDTO.getAmount());
            destination.setBalance(destinationWalletBalanceAfterTranfer);
            logger.info("[MAKE_TRANSFER] - Balance destination wallet before transfer: " + destination.getBalance());

            Transfer transfer = BOFactory.create(transferDTO, origin, destination);
            Transfer newTransfer = this.transferRepository.save(transfer);
            logger.info("[MAKE_TRANSFER] - The transfer was saved correctly, the associated id was: "
                    + newTransfer.getIdTransfer());

            return newTransfer;

        } catch (Exception e) {
            logger.error("[MAKE_TRANSFER] [ERROR] An error occurred while trying to create the transfer", e);
            throw new BussinesServiceException("An error occurred while trying to create the transfer", e);
        }
    }

    /**
     * @param transferDTO
     * @return
     * @throws TransferNotFoundException
     * @throws BussinesServiceException
     */
    @Transactional
    public TransferDTO update(TransferDTO transferDTO) throws TransferNotFoundException, BussinesServiceException {
        logger.info("[TRANSFER_UPDATE] - Start, modifying transfer...");

        try {
            Optional<Transfer> transferToUpdate = transferRepository.findById(transferDTO.getIdTransfer());
            if (transferToUpdate.isPresent()) {
                Transfer transfer = BOFactory.modify(transferToUpdate.get(), transferDTO);
                return DTOFactory.create(transferRepository.save(transfer));
            } else {
                throw new TransferNotFoundException("Transfer not found, id: " + transferDTO.getIdTransfer());
            }
        } catch (TransferNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("[TRANSFER_UPDATE] [ERROR] - An error occurred while trying to" +
                    " update the transfer", e);
            throw new BussinesServiceException("An error occurred while trying to update the transfer", e);
        }
    }

    /**
     * @param id
     * @throws NotFoundException
     * @throws TransferNotFoundException
     * @throws BussinesServiceException
     */
    @Transactional
    public void delete(Long id) throws TransferNotFoundException, BussinesServiceException {
        logger.info("[TRANSFER_DELETE] - Start, removing transfer...");
        try {
            Optional<Transfer> optional = this.transferRepository.findById(id);
            if (optional.isPresent()) {
                this.transferRepository.delete(optional.get());
            } else {
                throw new TransferNotFoundException("Transfer not found, id: " + id);
            }
        } catch (TransferNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("[TRANSFER_DELETE] [ERROR] - An error occurred while trying to" +
                    " delete the transfer", e);
            throw new BussinesServiceException("An error occurred while trying to delete the transfer", e);
        }
    }

    ///////////////////////////////////////////////////////////////////
    ///////////////////////////// QUERIES /////////////////////////////
    /////////////////////////////////////////////////////////////////

    // All methods that are not annotated with @Transactional
    // will be treated as a read mode transaction

    /**
     * @param id
     * @return
     * @throws TransferNotFoundException
     * @throws BussinesServiceException
     */
    public TransferDTO findById(Long id) throws TransferNotFoundException, BussinesServiceException {
        logger.info("[TRANSFER_FIND_BY_ID] - Start, searching transfer...");
        try {
            Optional<Transfer> optional = this.transferRepository.findById(id);
            if (optional.isPresent()) {
                return DTOFactory.create(optional.get());
            } else {
                throw new TransferNotFoundException("Transfer not found, id: " + id);
            }
        } catch (TransferNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("[TRANSFER_FIND_BY_ID] [ERROR] - An error occurred while trying to get " +
                    "the transfer with id: " + id, e);
            throw new BussinesServiceException("An error occurred while trying to get " +
                    "the transfer with id: " + id, e);
        }
    }

    /**
     * @param hash
     * @return
     * @throws TransferNotFoundException
     * @throws BussinesServiceException
     */
    private Wallet getWalletByHash(String hash) throws WalletNotFoundException, BussinesServiceException {
        try {
            Wallet wallet = this.walletRepository.findByHash(hash);
            if (wallet == null) {
                logger.info("[GET_WALLET_BY_HASH] [ERROR] - Wallet not found, hash: " + hash);
                throw new WalletNotFoundException("Wallet not found, hash: " + hash);
            }
            return wallet;
        } catch (WalletNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("[GET_WALLET_BY_HASH] [ERROR] - An error occurred while trying to get " +
                    "the wallet with hash: " + hash, e);
            throw new BussinesServiceException("An error occurred while trying to get " +
                    "the wallet with hash: " + hash, e);
        }
    }

    /**
     * @param fromTimestamp
     * @param toTimestamp
     * @param originWallet
     * @return
     * @throws WalletNotFoundException
     * @throws BussinesServiceException
     */
    public ListTransfersDTO findByFilter(Date fromTimestamp, Date toTimestamp, Optional<String> originWallet)
            throws WalletNotFoundException, BussinesServiceException {
        logger.info("[TRANSFER_FIND_BY_FILTER] - Start, searching transfer...");
        try {
            // Set 00:00:00
            Date fromDate = DateUtil.getDateFrom(fromTimestamp);
            // Set 23:59:59
            Date toDate = DateUtil.getDateTo(toTimestamp);
            List<Transfer> listTransfer = this.getTransferByFilter(fromDate, toDate, originWallet);
            // I determine what query to apply...
            ListTransfersDTO listDTO = new ListTransfersDTO();
            if (listTransfer != null && listTransfer.isEmpty() == false) {
                logger.info(
                        "[TRANSFER_FIND_BY_FILTER] - The amount of transfers obtained was: " + listTransfer.size());
                List<TransferDTO> listTransfersDTO = DTOFactory.getListTransfers(listTransfer);
                listDTO.setTransfers(listTransfersDTO);
            } else {
                logger.info("[TRANSFER_FIND_BY_FILTER] - No transfers were obtained.");
            }
            return listDTO;
        } catch (WalletNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("[TRANSFER_FIND_BY_FILTER] [ERROR] An error occurred while trying "
                    + "to get the transfers by filter", e);
            throw new BussinesServiceException("An error occurred while trying to get the transfers by filter", e);
        }
    }

    /***
     *
     * @param fromDate
     * @param toDate
     * @param originWallet
     * @return
     * @throws WalletNotFoundException
     * @throws BussinesServiceException
     */
    private List<Transfer> getTransferByFilter(Date fromDate, Date toDate, Optional<String> originWallet)
            throws WalletNotFoundException, BussinesServiceException {
        try {
            List<Transfer> listTransfer = new ArrayList<Transfer>();
            if (originWallet.isPresent()) {
                logger.info("[TRANSFER_FIND_BY_FILTER] - Query params - from timestamp: " + fromDate.toString()
                        + ", to timestamp: " + toDate.toString() + ", hash origin wallet: " + originWallet.get());
                Wallet wallet = walletRepository.findByHash(originWallet.get());
                if (wallet != null) {
                    listTransfer = this.transferRepository.findByFilter(fromDate, toDate, wallet);
                } else {
                    logger.info("[TRANSFER_FIND_BY_FILTER] - Hash wallet not found: " + originWallet.get());
                    // return empty list...
                    throw new WalletNotFoundException("Hash wallet not found: " + originWallet.get());
                }
            } else {
                logger.info("[TRANSFER_FIND_BY_FILTER] - Query params - from timestamp: " + fromDate.toString()
                        + ", to timestamp: " + toDate.toString());
//                listTransfer = this.transferRepository.findByFilter(fromDate, toDate);
                listTransfer = this.transferRepository.findByCreatedAtBetween(fromDate, toDate);
            }
            return listTransfer;
        } catch (WalletNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("[TRANSFER_FIND_BY_FILTER] [ERROR] An error occurred while trying "
                    + "to get the transfers by filter", e);
            throw new BussinesServiceException("An error occurred while trying to get the transfers by filter", e);
        }
    }
}
