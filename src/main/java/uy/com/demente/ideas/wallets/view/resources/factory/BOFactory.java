package uy.com.demente.ideas.wallets.view.resources.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;

import uy.com.demente.ideas.wallets.model.response.TransferDTO;
import uy.com.demente.ideas.wallets.model.response.UserDTO;
import uy.com.demente.ideas.wallets.model.response.WalletDTO;
import uy.com.demente.ideas.wallets.model.Transfer;
import uy.com.demente.ideas.wallets.model.TypesCoins;
import uy.com.demente.ideas.wallets.model.User;
import uy.com.demente.ideas.wallets.model.Wallet;
import uy.com.demente.ideas.wallets.util.Utils;

/**
 * @author 1987diegog
 */
public class BOFactory {

	static Logger logger = LogManager.getLogger(DTOFactory.class);

	/////////////////////////////////////////////////////////////
	////////////////////////// USER /////////////////////////////
	/////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param listUsersDTO
	 * @return
	 */
	public static List<User> getListUsers(List<UserDTO> listUsersDTO) {

		List<User> listUsers = null;

		if (listUsersDTO != null) {
			listUsers = listUsersDTO.stream().map(BOFactory::create) //
					.collect(Collectors.toCollection(ArrayList::new));
		}

		logger.info("[GET_LIST_USERS_BO] BOFactory Successful");

		return listUsers;
	}

	/**
	 * 
	 * @param userDTO
	 * @return
	 */
	public static User create(UserDTO userDTO) {

		User user = null;

		if (userDTO != null) {
			user = new User();
			BeanUtils.copyProperties(userDTO, user);
		}

		logger.info("[CREATE_USER_BO] BOFactory Successful");

		return user;
	}

	/////////////////////////////////////////////////////////////
	///////////////////////// WALLET ////////////////////////////
	/////////////////////////////////////////////////////////////

	/**
	 * @param walletDTO
	 * @param user
	 * @return
	 */
	public static Wallet create(WalletDTO walletDTO, User user) {

		Wallet wallet = null;

		if (walletDTO != null) {
			wallet = new Wallet();
			BeanUtils.copyProperties(walletDTO, wallet);

			wallet.setTypeCoin(TypesCoins.get(walletDTO.getTypeCoin()));
			wallet.setUser(user);
			wallet.setCreated(new Date());
			wallet.setHash(Utils.generateHash());
		}

		logger.info("[CREATE_WALLET_BO] BOFactory Successful");

		return wallet;
	}

	/////////////////////////////////////////////////////////////
	//////////////////////// TRANSFER ///////////////////////////
	/////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param transferDTO
	 * @return
	 */
	public static Transfer create(TransferDTO transferDTO, Wallet originWallet, Wallet destinationWallet) {

		Transfer transfer = null;

		if (transferDTO != null) {
			transfer = new Transfer();
			BeanUtils.copyProperties(transferDTO, transfer);

			transfer.setOriginWallet(originWallet);
			transfer.setDestinationWallet(destinationWallet);
			transfer.setTypeCoin(TypesCoins.get(transferDTO.getTypeCoin()));
			transfer.setTimestamp(new Date());
		}

		logger.info("[CREATE_TRANSFER_BO] BOFactory Successful");

		return transfer;
	}
}
