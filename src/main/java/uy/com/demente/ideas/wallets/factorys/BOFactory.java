package uy.com.demente.ideas.wallets.factorys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;

import uy.com.demente.ideas.wallets.model.*;
import uy.com.demente.ideas.wallets.model.response.TransferDTO;
import uy.com.demente.ideas.wallets.model.response.UserDTO;
import uy.com.demente.ideas.wallets.model.response.WalletDTO;
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
     * @param userDTO
     * @return
     */
    public static User create(UserDTO userDTO) {

        logger.info("[CREATE_USER_BO] Start create user BOFactory...");

        User user = null;

        if (userDTO != null) {
            user = new User();
            BeanUtils.copyProperties(userDTO, user);

            //////////////////////////////////////////////////
            // Additional attributes or data types to adapt //
            //////////////////////////////////////////////////
            user.setStatus(Status.get(userDTO.getStatus()));
        }

        logger.info("[CREATE_USER_BO] Create user BOFactory Successful");

        return user;
    }

    /**
     * @param user
     * @param userDTO
     * @return
     */
    public static User modify(User user, UserDTO userDTO) {

        logger.info("[MODIFY_USER_BO] Start modify user BOFactory...");

        if (user != null && userDTO != null) {
            user.setName(userDTO.getName());
            user.setLastName(userDTO.getLastName());
            user.setUsername(userDTO.getUsername());
            user.setAge(userDTO.getAge());
            user.setCellphone(userDTO.getCellphone());
            user.setEmail(userDTO.getEmail());
            user.setStatus(Status.get(userDTO.getStatus()));
        }

        logger.info("[MODIFY_USER_BO] Modify user BOFactory Successful");

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

        logger.info("[CREATE_WALLET_BO] Start create wallet BOFactory...");

        Wallet wallet = null;

        if (walletDTO != null) {
            wallet = new Wallet();
            BeanUtils.copyProperties(walletDTO, wallet);

            //////////////////////////////////////////////////
            // Additional attributes or data types to adapt //
            //////////////////////////////////////////////////
            wallet.setUser(user);
            wallet.setTypeCoin(TypesCoins.get(walletDTO.getTypeCoin()));
            wallet.setHash(Utils.generateHash());
        }

        logger.info("[CREATE_WALLET_BO] Create wallet BOFactory Successful");

        return wallet;
    }

    /**
     * @param wallet
     * @param walletDTO
     * @return
     */
    public static Wallet modify(Wallet wallet, WalletDTO walletDTO) {

        logger.info("[MODIFY_WALLET_BO] Start modify wallet BOFactory...");

        if (wallet != null && walletDTO != null) {
            wallet.setBalance(walletDTO.getBalance());
            wallet.setName(walletDTO.getName());
            wallet.setTypeCoin(TypesCoins.get(walletDTO.getTypeCoin()));
        }

        logger.info("[MODIFY_WALLET_BO] Modify wallet BOFactory Successful");

        return wallet;
    }

    /////////////////////////////////////////////////////////////
    //////////////////////// TRANSFER ///////////////////////////
    /////////////////////////////////////////////////////////////

    /**
     * @param transferDTO
     * @return
     */
    public static Transfer create(TransferDTO transferDTO, Wallet originWallet, Wallet destinationWallet) {

        logger.info("[CREATE_TRANSFER_BO] Start create transfer BOFactory...");

        Transfer transfer = null;

        if (transferDTO != null) {
            transfer = new Transfer();
            BeanUtils.copyProperties(transferDTO, transfer);

            //////////////////////////////////////////////////
            // Additional attributes or data types to adapt //
            //////////////////////////////////////////////////
            transfer.setOriginWallet(originWallet);
            transfer.setDestinationWallet(destinationWallet);
            transfer.setTypeCoin(TypesCoins.get(transferDTO.getTypeCoin()));
            transfer.setCreatedAt(new Date());
        }

        logger.info("[CREATE_TRANSFER_BO] Create transfer BOFactory Successful");

        return transfer;
    }

    public static Transfer modify(Transfer transfer, TransferDTO transferDTO) {

        logger.info("[MODIFY_TRANSFER_BO] Start modify transfer BOFactory...");

        if (transfer != null && transferDTO != null) {
            transfer.setAmount(transferDTO.getAmount());
            transfer.setCreatedAt(transferDTO.getCreatedAt());
            transfer.setAdminName(transferDTO.getAdminName());
            transfer.setTypeCoin(TypesCoins.get(transferDTO.getTypeCoin()));
        }

        logger.info("[MODIFY_TRANSFER_BO] Modify transfer BOFactory Successful");

        return transfer;
    }
}
