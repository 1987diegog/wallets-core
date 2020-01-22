package uy.com.demente.ideas.wallets.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uy.com.demente.ideas.wallets.exceptions.BussinesServiceException;
import uy.com.demente.ideas.wallets.exceptions.UserNotFoundException;
import uy.com.demente.ideas.wallets.exceptions.WalletNotFoundException;
import uy.com.demente.ideas.wallets.repository.IUserRepository;
import uy.com.demente.ideas.wallets.repository.IWalletRepository;
import uy.com.demente.ideas.wallets.model.User;
import uy.com.demente.ideas.wallets.model.Wallet;
import uy.com.demente.ideas.wallets.dtos.ListWalletsDTO;
import uy.com.demente.ideas.wallets.dtos.WalletDTO;
import uy.com.demente.ideas.wallets.factorys.BOFactory;
import uy.com.demente.ideas.wallets.factorys.DTOFactory;

import java.util.List;
import java.util.Optional;

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
     * @throws BussinesServiceException
     */
    @Transactional
    public WalletDTO create(WalletDTO walletDTO) throws UserNotFoundException, BussinesServiceException {
        logger.info("[WALLET_CREATE] - Start, creating wallet...");
        try {
            Optional<User> user = userRepository.findById(walletDTO.getIdUser());
            // Before creating the wallet, verify that the user exists
            if (user.isPresent()) {
                Wallet wallet = BOFactory.create(walletDTO, user.get());
                return DTOFactory.create(this.walletRepository.save(wallet));
            } else {
                throw new UserNotFoundException("User not found, id: " + walletDTO.getIdUser());
            }
        } catch (UserNotFoundException e) {
            logger.info("[WALLET_CREATE] [NOT_FOUND] - User not found, id: " + walletDTO.getIdUser());
            throw e;
        } catch (Exception e) {
            logger.error("[WALLET_CREATE] [ERROR] - An error occurred while trying to create a wallet", e);
            throw new BussinesServiceException("An error occurred while trying to create a wallet", e);
        }
    }

    /**
     * @param walletDTO
     * @return
     * @throws WalletNotFoundException
     * @throws BussinesServiceException
     */
    @Transactional
    public WalletDTO update(WalletDTO walletDTO) throws WalletNotFoundException, BussinesServiceException {
        logger.info("[WALLET_UPDATE] - Start, modifying wallet...");
        try {
            Optional<Wallet> walletToUpdate = walletRepository.findById(walletDTO.getIdWallet());
            if (walletToUpdate.isPresent()) {
                Wallet wallet = BOFactory.modify(walletToUpdate.get(), walletDTO);
                return DTOFactory.create(walletRepository.save(wallet));
            } else {
                throw new WalletNotFoundException("Wallet not found, id: " + walletDTO.getIdWallet());
            }
        } catch (WalletNotFoundException e) {
            logger.info("[WALLET_UPDATE] [NOT_FOUND] - Wallet not found, id: " + walletDTO.getIdWallet());
            throw e;
        } catch (Exception e) {
            logger.error("[WALLET_UPDATE] [ERROR] - An error occurred while trying to update a wallet data", e);
            throw new BussinesServiceException("An error occurred while trying to update a wallet data", e);
        }
    }

    /**
     * @param id
     * @throws WalletNotFoundException
     * @throws BussinesServiceException
     */
    @Transactional
    public void delete(Long id) throws WalletNotFoundException, BussinesServiceException {
        logger.info("[WALLET_DELETE] - Start, removing wallet...");
        try {
            Optional<Wallet> optional = this.walletRepository.findById(id);
            if (optional.isPresent()) {
                this.walletRepository.delete(optional.get());
            } else {
                throw new WalletNotFoundException("Wallet not found, id: " + id);
            }
        } catch (WalletNotFoundException e) {
            logger.info("[WALLET_DELETE] [NOT_FOUND] - Wallet not found, id: " + id);
            throw e;
        } catch (Exception e) {
            logger.error("[WALLET_DELETE] [ERROR] - An error occurred while trying to delete a wallet, id: " + id, e);
            throw new BussinesServiceException("An error occurred while trying to delete a wallet, id: " + id, e);
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
     * @throws WalletNotFoundException
     * @throws BussinesServiceException
     */
    public WalletDTO findById(Long id) throws WalletNotFoundException, BussinesServiceException {
        logger.info("[WALLET_FIND_BY_ID] - Start, searching wallet...");
        try {
            Optional<Wallet> optional = this.walletRepository.findById(id);
            if (optional.isPresent()) {
                return DTOFactory.create(optional.get());
            } else {
                throw new WalletNotFoundException("Wallet not found, id: " + id);
            }
        } catch (WalletNotFoundException e) {
            logger.info("[WALLET_FIND_BY_ID] [NOT_FOUND] - Wallet not found, id: " + id);
            throw e;
        } catch (Exception e) {
            logger.error("[WALLET_FIND_BY_ID] [ERROR] - An error occurred while trying to find " +
                    "a wallet by id: " + id, e);
            throw new BussinesServiceException("An error occurred while trying to find a wallet by id: " + id, e);
        }
    }

    /**
     * @param hash
     * @return
     * @throws WalletNotFoundException
     * @throws BussinesServiceException
     */
    public WalletDTO findByHash(String hash) throws WalletNotFoundException, BussinesServiceException {

        logger.info("[WALLET_FIND_BY_HASH] - Start, searching wallet...");
        return this.walletRepository.findByHash(hash).map(DTOFactory::create)
                .orElseThrow(() -> {
                    logger.info("[WALLET_FIND_BY_HASH] [NOT_FOUND] - Wallet not found, hash: " + hash);
                    return new WalletNotFoundException("Wallet not found, hash: " + hash);
                });
    }

    /**
     * @return
     * @throws BussinesServiceException
     */
    public ListWalletsDTO findAll() throws BussinesServiceException {
        logger.info("[WALLET_FIND_ALL] - Start, searching wallets...");
        try {
            List<WalletDTO> listWallets = DTOFactory.getListWallets(this.walletRepository.findAll());
            ListWalletsDTO listDTO = new ListWalletsDTO();
            if (listWallets != null) {
                logger.info("[WALLET_FIND_ALL] - List wallets size: " + listWallets.size());
                listDTO.setWallets(listWallets);
            }
            return listDTO;
        } catch (Exception e) {
            logger.error("[WALLET_FIND_ALL] [ERROR] - An error occurred while trying to find all wallets", e);
            throw new BussinesServiceException("An error occurred while trying to find all wallets", e);
        }
    }

    /**
     * @param idUser
     * @return
     * @throws UserNotFoundException
     * @throws BussinesServiceException
     */
    public ListWalletsDTO findByUser(Long idUser) throws UserNotFoundException, BussinesServiceException {
        logger.info("[WALLET_FIND_ALL_BY_USER] - Start, searching wallets...");
        try {
            ListWalletsDTO listDTO = new ListWalletsDTO();
            Optional<User> optionalUser = this.userRepository.findById(idUser);
            // Before search wallets, verify that the user exists
            if (optionalUser.isPresent()) {
                List<WalletDTO> listWallets = DTOFactory
                        .getListWallets(this.walletRepository.findByUser(optionalUser.get()));
                if (listWallets != null) {
                    logger.info("[WALLET_FIND_ALL_BY_USER] - List wallets size: " + listWallets.size());
                    listDTO.setWallets(listWallets);
                }
            } else {
                throw new UserNotFoundException("User not found, id: " + idUser);
            }
            return listDTO;
        } catch (UserNotFoundException e) {
            logger.info("[WALLET_FIND_ALL_BY_USER] [NOT_FOUND] - User not found, id: " + idUser);
            throw e;
        } catch (Exception e) {
            logger.error("[WALLET_FIND_ALL_BY_USER] [ERROR] - An error occurred while trying to find all" +
                    " wallets by user, idUser: " + idUser, e);
            throw new BussinesServiceException("An error occurred while trying to find all wallets by " +
                    "user, idUser: " + idUser, e);
        }
    }
}
