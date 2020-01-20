package uy.com.demente.ideas.wallets.factorys;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;

import uy.com.demente.ideas.wallets.dtos.TransferDTO;
import uy.com.demente.ideas.wallets.dtos.UserDTO;
import uy.com.demente.ideas.wallets.dtos.WalletDTO;
import uy.com.demente.ideas.wallets.model.Transfer;
import uy.com.demente.ideas.wallets.model.User;
import uy.com.demente.ideas.wallets.model.Wallet;

/**
 * @author 1987diegog
 */
public class DTOFactory {

    static Logger logger = LogManager.getLogger(DTOFactory.class);

    /////////////////////////////////////////////////////////////
    /////////////////////////// USER ////////////////////////////
    /////////////////////////////////////////////////////////////

    /**
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

            //////////////////////////////////////////////////
            // Additional attributes or data types to adapt //
            //////////////////////////////////////////////////
            userDTO.setStatus(user.getStatus().name());
        }

        return userDTO;
    }

    /////////////////////////////////////////////////////////////
    ///////////////////////// WALLET ////////////////////////////
    /////////////////////////////////////////////////////////////

    /**
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
     * @param wallet
     * @return
     */
    public static WalletDTO create(Wallet wallet) {

        WalletDTO walletDTO = null;

        if (wallet != null) {
            walletDTO = new WalletDTO();
            BeanUtils.copyProperties(wallet, walletDTO);

            //////////////////////////////////////////////////
            // Additional attributes or data types to adapt //
            //////////////////////////////////////////////////
            walletDTO.setIdUser(wallet.getUser().getIdUser());
            walletDTO.setTypeCoin(wallet.getTypeCoin().name());
        }

        return walletDTO;
    }

    /////////////////////////////////////////////////////////////
    ///////////////////////// TRANSFER //////////////////////////
    /////////////////////////////////////////////////////////////

    /**
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
     * @param transfer
     * @return
     */
    public static TransferDTO create(Transfer transfer) {

        TransferDTO transferDTO = null;

        if (transfer != null) {
            transferDTO = new TransferDTO();
            BeanUtils.copyProperties(transfer, transferDTO);

            //////////////////////////////////////////////////
            // Additional attributes or data types to adapt //
            //////////////////////////////////////////////////
            transferDTO.setTypeCoin(transfer.getTypeCoin().name());
            transferDTO.setOriginWallet(transfer.getOriginWallet().getHash());
            transferDTO.setDestinationWallet(transfer.getDestinationWallet().getHash());
        }

        return transferDTO;
    }
}
