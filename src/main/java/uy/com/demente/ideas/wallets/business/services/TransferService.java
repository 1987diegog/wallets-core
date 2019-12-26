package uy.com.demente.ideas.wallets.business.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javassist.NotFoundException;
import uy.com.demente.ideas.wallets.business.exceptions.*;
import uy.com.demente.ideas.wallets.business.repository.ITransferRepository;
import uy.com.demente.ideas.wallets.business.repository.IWalletRepository;
import uy.com.demente.ideas.wallets.dto.ListTransfersDTO;
import uy.com.demente.ideas.wallets.dto.TransferDTO;
import uy.com.demente.ideas.wallets.model.Transfer;
import uy.com.demente.ideas.wallets.model.TypesCoins;
import uy.com.demente.ideas.wallets.model.Wallet;
import uy.com.demente.ideas.wallets.util.DateUtils;
import uy.com.demente.ideas.wallets.view.resources.factory.BOFactory;
import uy.com.demente.ideas.wallets.view.resources.factory.DTOFactory;

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
	 * 
	 * @param transferDTO
	 * @return
	 * @throws WalletNotFoundException
	 * @throws InsufficientBalanceException
	 * @throws TypeCoinDontMatchesException
	 * @throws WalletMatchesException
	 * @throws MakeTransferException
	 * @throws DTOFactoryException
	 */
	@Transactional
	public TransferDTO createTransfer(TransferDTO transferDTO)
			throws WalletNotFoundException, InsufficientBalanceException, TypeCoinDontMatchesException,
			WalletMatchesException, MakeTransferException, DTOFactoryException {

		logger.info("[CREATE_TRANSFER] - Origin wallet with hash: " + transferDTO.getOriginWallet() //
				+ ", destination wallet with hash: " + transferDTO.getDestinationWallet() + ", amount to transfer: "
				+ transferDTO.getAmount());

		Wallet originWallet = this.getWalletByHash(transferDTO.getOriginWallet());
		logger.info("[CREATE_TRANSFER] - The original wallet was obtained successfully...");

		Wallet destinationWallet = this.getWalletByHash(transferDTO.getDestinationWallet());
		logger.info("[CREATE_TRANSFER] - The destination wallet was obtained successfully...");

		// Validate if is possible the transfer
		this.validateTransfer(originWallet, destinationWallet, transferDTO.getAmount());
		logger.info("[CREATE_TRANSFER] - The validation was successful...");

		Transfer transfer = this.makeTransfer(originWallet, destinationWallet, transferDTO);
		logger.info("[CREATE_TRANSFER] - The creation of the transfer was successful...");

		try {

			return DTOFactory.create(transfer);

		} catch (Exception e) {
			logger.error("[CREATE_TRANSFER] [ERROR] - When trying to create the object using factoryDTO", e);
			throw new DTOFactoryException("When trying to create the object using factoryDTO", e);
		}
	}

	/**
	 * 
	 * @param origin
	 * @param destination
	 * @param amount
	 * @throws WalletMatchesException
	 * @throws TypeCoinDontMatchesException
	 * @throws InsufficientBalanceException
	 */
	public void validateTransfer(Wallet origin, Wallet destination, BigDecimal amount)
			throws WalletMatchesException, TypeCoinDontMatchesException, InsufficientBalanceException {

		/////////////////////////////////////////////////
		////////////////// - WALLETS - //////////////////
		/////////////////////////////////////////////////

		if (origin.getIdWallet().equals(destination.getIdWallet()) == false) {
			logger.info("[VALIDATE_TRANSFER] - The origin and destination wallets are different, proceed...");
		} else {
			logger.info(
					"[VALIDATE_TRANSFER] - The origin wallet is equal to the destination wallet, the transaction makes no sense");
			throw new WalletMatchesException(
					"The origin wallet is equal to the destination wallet, the transaction makes no sense");
		}

		/////////////////////////////////////////////////
		///////////////// - TYPE COIN - /////////////////
		/////////////////////////////////////////////////

		if (origin.getTypeCoin().equals(destination.getTypeCoin())) {
			logger.info("[VALIDATE_TRANSFER] - The type of currency is the same, proceed...");
		} else {
			// ------------------------------------
			// ----- this.conversionCurrency(); --------
			// ------------------------------------
			logger.info("[VALIDATE_TRANSFER] - The type of currency is different");
			throw new TypeCoinDontMatchesException("The type of currency is different");
		}

		/////////////////////////////////////////////////
		////////////////// - AMOUNT - ///////////////////
		/////////////////////////////////////////////////

		// Check if the balance of the original wallet is greater than or equal to the
		// one you want to transfer ...
		if (origin.getBalance().compareTo(amount) >= 0) {
			logger.info(
					"[VALIDATE_TRANSFER] - The original wallet has the same or more balance as requested in the transfer, proceed...");
		} else {
			logger.info(
					"[VALIDATE_TRANSFER] - The balance to be transferred is greater than that of the original wallet");
			throw new InsufficientBalanceException(
					"The balance to be transferred is greater than that of the original wallet");
		}
	}

	/**
	 */
	public void conversionCurrency(BigDecimal amount, TypesCoins fromCoin, TypesCoins toCoin) {
		// TODO: If contemplated, a currency transformation could be made, for example
		// from PAGACOIN to DOLLARS and then from DOLLARS to BITCOIN
	}

	/**
	 * 
	 * @param origin
	 * @param destination
	 * @param transferDTO
	 * @return
	 * @throws MakeTransferException
	 */
	public Transfer makeTransfer(Wallet origin, Wallet destination, TransferDTO transferDTO)
			throws MakeTransferException {

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
			throw new MakeTransferException("An error occurred while trying to create the transfer", e);
		}
	}

	/**
	 * 
	 * @param transferDTO
	 * @return
	 * @throws NotFoundException
	 */
	@Transactional
	public TransferDTO update(TransferDTO transferDTO) throws NotFoundException {

		Optional<Transfer> transferToUpdate = transferRepository.findById(transferDTO.getIdTransfer());

		if (transferToUpdate.isPresent()) {

			Transfer transfer = transferToUpdate.get();

			transfer.setAmount(transferDTO.getAmount());
			transfer.setTimestamp(transferDTO.getTimestamp());
			transfer.setAdminName(transferDTO.getAdminName());
			transfer.setTypeCoin(TypesCoins.get(transferDTO.getTypeCoin()));

			return DTOFactory.create(transferRepository.save(transfer));

		} else {
			throw new NotFoundException("Transfer not found, id: " + transferDTO.getIdTransfer());
		}
	}

	/**
	 * 
	 * @param id
	 * @throws NotFoundException
	 * @throws BussinesServiceException
	 * @throws TransactionNotFoundException
	 */
	@Transactional
	public void delete(Long id) throws BussinesServiceException, TransactionNotFoundException {

		Optional<Transfer> optional = this.transferRepository.findById(id);
		if (optional.isPresent()) {
			try {
				this.transferRepository.delete(optional.get());
			} catch (Exception e) {
				logger.error("[TRANSFER_DELETE] [ERROR] - An error occurred while trying to delete the transaction", e);
				throw new BussinesServiceException("An error occurred while trying to delete the transaction", e);
			}
		} else {
			throw new TransactionNotFoundException("Transaction not found");
		}
	}

	///////////////////////////////////////////////////////////////////
	///////////////////////////// QUERIES /////////////////////////////
	/////////////////////////////////////////////////////////////////

	// All methods that are not annotated with @Transactional
	// will be treated as a read mode transaction

	/**
	 * 
	 * @param id
	 * @return
	 * @throws NotFoundException
	 * @throws BussinesServiceException
	 * @throws TransactionNotFoundException
	 */
	public TransferDTO findById(Long id) throws BussinesServiceException, TransactionNotFoundException {

		Optional<Transfer> optional = this.transferRepository.findById(id);
		if (optional.isPresent()) {
			try {
				return DTOFactory.create(optional.get());
			} catch (Exception e) {
				logger.error("[TRANSFER_FIND_BY_ID] [ERROR] - An error occurred while "
						+ "trying to get the transaction with id: " + id, e);
				throw new BussinesServiceException(
						"An error occurred while trying to get the transaction with id: " + id, e);
			}
		} else {
			throw new TransactionNotFoundException("Transaction not found");
		}
	}

	/**
	 * 
	 * @param hash
	 * @return
	 * @throws WalletNotFoundException
	 */
	private Wallet getWalletByHash(String hash) throws WalletNotFoundException {

		Wallet wallet = this.walletRepository.findByHash(hash);

		if (wallet == null) {
			logger.info("[GET_WALLET_BY_HASH] [ERROR] - Hash wallet not found: " + hash);
			throw new WalletNotFoundException("Hash wallet not found: " + hash);
		}

		return wallet;
	}

	/**
	 * 
	 * @param fromTimestamp
	 * @param toTimestamp
	 * @param originWallet
	 * @return
	 * @throws BussinesServiceException
	 * @throws WalletNotFoundException
	 */
	public ListTransfersDTO findByFilter(Date fromTimestamp, Date toTimestamp, Optional<String> originWallet)
			throws BussinesServiceException, WalletNotFoundException {

		try {

			// Set 00:00:00
			Date fromDate = DateUtils.getDateFrom(fromTimestamp);
			// Set 23:59:59
			Date toDate = DateUtils.getDateTo(toTimestamp);

			List<Transfer> listTransfer = getTransferByFilet(fromDate, toDate, originWallet);

			// I determine what query to apply...
			ListTransfersDTO listDTO = new ListTransfersDTO();
			if (listTransfer != null && listTransfer.isEmpty() == false) {
				logger.info(
						"[TRANSFER_FIND_BY_FILTER] - The amount of transactions obtained was: " + listTransfer.size());
				List<TransferDTO> listTransfersDTO = DTOFactory.getListTransfers(listTransfer);
				listDTO.setTransfers(listTransfersDTO);

			} else {
				logger.info("[TRANSFER_FIND_ALL] - No transactions were obtained.");
			}

			return listDTO;

		} catch (WalletNotFoundException e) {
			throw new WalletNotFoundException(e);
		} catch (Exception e) {
			logger.error("[TRANSFER_FIND_BY_FILTER] [ERROR] An error occurred while trying "
					+ "to get the transactions by filter", e);
			throw new BussinesServiceException("An error occurred while trying to get the transactions by filter", e);
		}
	}

	/***
	 * 
	 * @param fromDate
	 * @param toDate
	 * @param originWallet
	 * @return
	 * @throws WalletNotFoundException
	 */
	private List<Transfer> getTransferByFilet(Date fromDate, Date toDate, Optional<String> originWallet)
			throws WalletNotFoundException {

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
			listTransfer = this.transferRepository.findByFilter(fromDate, toDate);
		}

		return listTransfer;
	}
}
