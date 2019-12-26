package uy.com.demente.ideas.view.resources.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;

import uy.com.demente.ideas.business.exceptions.DTOFactoryException;
import uy.com.demente.ideas.dto.TransferDTO;
import uy.com.demente.ideas.dto.UserDTO;
import uy.com.demente.ideas.dto.WalletDTO;
import uy.com.demente.ideas.model.Transfer;
import uy.com.demente.ideas.model.User;
import uy.com.demente.ideas.model.Wallet;

/**
 * @author 1987diegog
 */
public class DTOFactory {

	static Logger logger = LogManager.getLogger(DTOFactory.class);

	/////////////////////////////////////////////////////////////
	/////////////////////////// USER ////////////////////////////
	/////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param listUsers
	 * @return
	 */
	public static List<UserDTO> getListUsers(List<User> listUsers) {

		List<UserDTO> listUsersDTO = null;
		if (listUsers != null) {

			listUsersDTO = listUsers.stream().map(DTOFactory::create) //
					.collect(Collectors.toCollection(ArrayList::new));
		}

		logger.info("[GET_LIST_USERS_DTO] DTOFactory Successful");

		return listUsersDTO;
	}

	/**
	 * @param user
	 * @return
	 */
	public static UserDTO create(User user) {

		UserDTO userDTO = null;
		if (user != null) {
			userDTO = new UserDTO();
			BeanUtils.copyProperties(user, userDTO);
		}

		return userDTO;
	}

	/////////////////////////////////////////////////////////////
	///////////////////////// WALLET ////////////////////////////
	/////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param listWallets
	 * @return
	 */
	public static List<WalletDTO> getListWallets(List<Wallet> listWallets) {

		List<WalletDTO> listWalletsDTO = null;
		if (listWallets != null) {

			listWalletsDTO = listWallets.stream().map(DTOFactory::create)
					.collect(Collectors.toCollection(ArrayList::new));
		}

		logger.info("[GET_LIST_WALLETS_DTO] DTOFactory Successful");

		return listWalletsDTO;
	}

	/**
	 * 
	 * @param wallet
	 * @return
	 */
	public static WalletDTO create(Wallet wallet) {

		WalletDTO walletDTO = null;

		if (wallet != null) {
			walletDTO = new WalletDTO();
			BeanUtils.copyProperties(wallet, walletDTO);
			walletDTO.setTypeCoin(wallet.getTypeCoin().name());
			walletDTO.setIdUser(wallet.getUser().getIdUser());
		}

		return walletDTO;
	}

	/////////////////////////////////////////////////////////////
	///////////////////////// TRANSFER //////////////////////////
	/////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param listTransfers
	 * @return
	 */
	public static List<TransferDTO> getListTransfers(List<Transfer> listTransfers) {

		List<TransferDTO> listTransferDTO = null;
		if (listTransfers != null) {

			listTransferDTO = listTransfers.stream().map(DTOFactory::create)
					.collect(Collectors.toCollection(ArrayList::new));
		}

		logger.info("[GET_LIST_TRANSFERS_DTO] DTOFactory Successful");

		return listTransferDTO;
	}

	/**
	 * 
	 * @param transfer
	 * @return
	 * @throws DTOFactoryException
	 */
	public static TransferDTO create(Transfer transfer) {

		TransferDTO transferDTO = null;

		if (transfer != null) {

			transferDTO = new TransferDTO();
			BeanUtils.copyProperties(transfer, transferDTO);

			////// -- ADD DATA -- //////
			transferDTO.setTypeCoin(transfer.getTypeCoin().name());
			transferDTO.setOriginWallet(transfer.getOriginWallet().getHash());
			transferDTO.setDestinationWallet(transfer.getDestinationWallet().getHash());
		}

		return transferDTO;
	}
}
