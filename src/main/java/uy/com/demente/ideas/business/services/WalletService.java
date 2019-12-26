package uy.com.demente.ideas.business.services;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javassist.NotFoundException;
import uy.com.demente.ideas.business.exceptions.UserNotFoundException;
import uy.com.demente.ideas.business.repository.IUserRepository;
import uy.com.demente.ideas.business.repository.IWalletRepository;
import uy.com.demente.ideas.dto.ListWalletsDTO;
import uy.com.demente.ideas.dto.WalletDTO;
import uy.com.demente.ideas.model.TypesCoins;
import uy.com.demente.ideas.model.User;
import uy.com.demente.ideas.model.Wallet;
import uy.com.demente.ideas.view.resources.factory.BOFactory;
import uy.com.demente.ideas.view.resources.factory.DTOFactory;

/**
 * @author 1987diegog
 */
@Service
@Transactional(readOnly = true)
public class WalletService {

	Logger logger = LogManager.getLogger(WalletService.class);

	private final IWalletRepository walletRepository;
	private final IUserRepository userRepository;

	public WalletService(IWalletRepository walletRepository, IUserRepository userRepository) {
		this.walletRepository = walletRepository;
		this.userRepository = userRepository;
	}

	/**
	 * @param walletDTO
	 * @return
	 * @throws UserNotFoundException
	 */
	@Transactional
	public WalletDTO create(WalletDTO walletDTO) throws UserNotFoundException {

		Optional<User> user = userRepository.findById(walletDTO.getIdUser());

		if (user.isPresent()) {

			Wallet wallet = BOFactory.create(walletDTO, user.get());
			return DTOFactory.create(this.walletRepository.save(wallet));

		} else {
			logger.error("[CREATE_WALLET] [ERROR] - User with id: " + walletDTO.getIdUser() + " not found");
			throw new UserNotFoundException("User with id: " + walletDTO.getIdUser() + " not found\"");
		}
	}

	/**
	 * 
	 * @param walletDTO
	 * @return
	 * @throws NotFoundException
	 */
	@Transactional
	public WalletDTO update(WalletDTO walletDTO) throws NotFoundException {

		Optional<Wallet> walletToUpdate = walletRepository.findById(walletDTO.getIdWallet());

		if (walletToUpdate.isPresent()) {

			Wallet wallet = walletToUpdate.get();
			wallet.setBalance(walletDTO.getBalance());
			wallet.setName(walletDTO.getName());
			wallet.setTypeCoin(TypesCoins.get(walletDTO.getTypeCoin()));

			return DTOFactory.create(walletRepository.save(wallet));

		} else {
			throw new NotFoundException("Wallet not found, id: " + walletDTO.getIdWallet());
		}
	}

	/**
	 * 
	 * @param wallet
	 * @throws NotFoundException
	 */
	@Transactional
	public void delete(Long id) throws NotFoundException {

		Optional<Wallet> optional = this.walletRepository.findById(id);
		if (optional.isPresent()) {
			this.walletRepository.delete(optional.get());
		} else {
			throw new NotFoundException("Wallet not found, id: " + id);
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
	 */
	public WalletDTO findById(Long id) throws NotFoundException {

		Optional<Wallet> optional = this.walletRepository.findById(id);
		if (optional.isPresent()) {
			return DTOFactory.create(optional.get());
		} else {
			throw new NotFoundException("Wallet not found, id: " + id);
		}
	}

	/**
	 * 
	 * @param hash
	 * @return
	 * @throws NotFoundException
	 */
	public WalletDTO findByHash(String hash) throws NotFoundException {

		Wallet wallet = this.walletRepository.findByHash(hash);

		if (wallet != null) {
			return DTOFactory.create(wallet);
		} else {
			throw new NotFoundException("Wallet not found, hash: " + hash);
		}
	}

	/**
	 * 
	 * @return
	 */
	public ListWalletsDTO findAll() {

		List<WalletDTO> listWallets = DTOFactory.getListWallets(this.walletRepository.findAll());

		ListWalletsDTO listDTO = new ListWalletsDTO();
		if (listWallets != null && listWallets.isEmpty() == false) {
			listDTO.setWallets(listWallets);
		}

		return listDTO;

	}

	/**
	 * 
	 * @param idUser
	 * @return
	 */
	public ListWalletsDTO findByUser(Long idUser) {

		Optional<User> optionalUser = this.userRepository.findById(idUser);
		ListWalletsDTO listDTO = new ListWalletsDTO();
		if (optionalUser.isPresent()) {

			List<WalletDTO> listWallets = DTOFactory
					.getListWallets(this.walletRepository.findByUser(optionalUser.get()));

			if (listWallets != null && listWallets.isEmpty() == false) {
				listDTO.setWallets(listWallets);
			}
		}

		return listDTO;
	}

}
