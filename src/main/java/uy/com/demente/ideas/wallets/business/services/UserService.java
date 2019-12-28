package uy.com.demente.ideas.wallets.business.services;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uy.com.demente.ideas.wallets.business.exceptions.BussinesServiceException;
import uy.com.demente.ideas.wallets.business.exceptions.UserNotFoundException;
import uy.com.demente.ideas.wallets.business.repository.IUserRepository;
import uy.com.demente.ideas.wallets.model.response.ListUsersDTO;
import uy.com.demente.ideas.wallets.model.response.UserDTO;
import uy.com.demente.ideas.wallets.model.User;
import uy.com.demente.ideas.wallets.view.resources.factory.BOFactory;
import uy.com.demente.ideas.wallets.view.resources.factory.DTOFactory;

/**
 * @author 1987diegog
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    Logger logger = LogManager.getLogger(UserService.class);
    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param userDTO
     * @return
     */
    @Transactional
    public UserDTO create(UserDTO userDTO) throws BussinesServiceException {
        try {
            User user = BOFactory.create(userDTO);
            return DTOFactory.create(this.userRepository.save(user));
        } catch (Exception e) {
            logger.error("[USER_CREATE] [ERROR] - An error occurred while trying to create a user", e);
            throw new BussinesServiceException("An error occurred while trying to create a user", e);
        }
    }

    /**
     * @param userDTO
     * @return
     * @throws UserNotFoundException
     * @throws BussinesServiceException
     */
    @Transactional
    public UserDTO update(UserDTO userDTO) throws UserNotFoundException, BussinesServiceException {
        try {
            Optional<User> userToUpdate = userRepository.findById(userDTO.getIdUser());
            if (userToUpdate.isPresent()) {
                User user = BOFactory.modify(userToUpdate.get(), userDTO);
                return DTOFactory.create(userRepository.save(user));
            } else {
                throw new UserNotFoundException("User not found, id: " + userDTO.getIdUser());
            }
        } catch (UserNotFoundException e) {
            logger.error("[USER_UPDATE] [NOT_FOUND] - User not found, id: " + userDTO.getIdUser(), e);
            throw e;
        } catch (Exception e) {
            logger.error("[USER_UPDATE] [ERROR] - An error occurred while trying to update a user data", e);
            throw new BussinesServiceException("An error occurred while trying to update a user data", e);
        }
    }

    /**
     * @param id
     * @throws UserNotFoundException
     * @throws BussinesServiceException
     */
    @Transactional
    public void delete(Long id) throws UserNotFoundException, BussinesServiceException {
        try {
            Optional<User> optional = this.userRepository.findById(id);
            if (optional.isPresent()) {
                this.userRepository.delete(optional.get());
            } else {
                throw new UserNotFoundException("User not found, id: " + id);
            }
        } catch (UserNotFoundException e) {
            logger.error("[USER_DELETE] [NOT_FOUND] - User not found, id: " + id, e);
            throw e;
        } catch (Exception e) {
            logger.error("[USER_DELETE] [ERROR] - An error occurred while trying to delete " +
                    "a user, id: " + id, e);
            throw new BussinesServiceException("An error occurred while trying to delete a user, id: " + id, e);
        }
    }

    //////////////////////////////////////////////////////////////////
    ///////////////////////////// QUERIES ////////////////////////////
    /////////////////////////////////////////////////////////////////

    // All methods that are not annotated with @Transactional
    // will be treated as a read mode transaction

    /**
     * @param id
     * @return
     * @throws UserNotFoundException
     * @throws BussinesServiceException
     */
    public UserDTO findById(Long id) throws UserNotFoundException, BussinesServiceException {
        try {
            Optional<User> optional = this.userRepository.findById(id);
            if (optional.isPresent()) {
                return DTOFactory.create(optional.get());
            } else {
                throw new UserNotFoundException("User not found, id: " + id);
            }
        } catch (UserNotFoundException e) {
            logger.error("[USER_FIND_BY_ID] [NOT_FOUND] - User not found, id: " + id, e);
            throw e;
        } catch (Exception e) {
            logger.error("[USER_FIND_BY_ID] [ERROR] - An error occurred while trying to find " +
                    "a user by id: " + id, e);
            throw new BussinesServiceException("An error occurred while trying to find a user by id: " + id, e);
        }
    }

    /**
     * @param email
     * @return
     * @throws UserNotFoundException
     * @throws BussinesServiceException
     */
    public UserDTO findByEmail(String email) throws UserNotFoundException, BussinesServiceException {
        try {
            User user = this.userRepository.findByEmail(email);
            if (user != null) {
                UserDTO response = DTOFactory.create(user);
                return response;
            } else {
                throw new UserNotFoundException("User not found, email: " + email);
            }
        } catch (UserNotFoundException e) {
            logger.error("[USER_FIND_BY_EMAIL] [NOT_FOUND] - User not found, email: " + email, e);
            throw e;
        } catch (Exception e) {
            logger.error("[USER_FIND_BY_EMAIL] [ERROR] - An error occurred while trying to find " +
                    "a user by email: " + email, e);
            throw new BussinesServiceException("An error occurred while trying to find a user by email: " + email, e);
        }
    }

    /**
     * @return
     * @throws BussinesServiceException
     */
    public ListUsersDTO findAll() throws BussinesServiceException {
        try {
            List<UserDTO> listUsers = DTOFactory.getListUsers(this.userRepository.findAll());
            ListUsersDTO listDTO = new ListUsersDTO();
            if (listUsers != null && listUsers.isEmpty() == false) {
                listDTO.setUsers(listUsers);
            }
            return listDTO;
        } catch (Exception e) {
            logger.error("[USER_FIND_BY_EMAIL] [ERROR] - An error occurred while trying to find all users", e);
            throw new BussinesServiceException("An error occurred while trying to find all users", e);
        }
    }
}
