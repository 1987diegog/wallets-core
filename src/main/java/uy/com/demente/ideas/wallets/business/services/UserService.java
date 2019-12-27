package uy.com.demente.ideas.wallets.business.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javassist.NotFoundException;
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

	private final IUserRepository userRepository;

	public UserService(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * @param userDTO
	 * @return
	 */
	@Transactional
	public UserDTO create(UserDTO userDTO) {
		User user = BOFactory.create(userDTO);
		return DTOFactory.create(this.userRepository.save(user));
	}

	/**
	 * 
	 * @param userDTO
	 * @return
	 * @throws UserNotFoundException(
	 */
	@Transactional
	public UserDTO update(UserDTO userDTO) throws UserNotFoundException {

		Optional<User> userToUpdate = userRepository.findById(userDTO.getIdUser());

		if (userToUpdate.isPresent()) {

			User user = userToUpdate.get();
			user.setAge(userDTO.getAge());
			user.setCellphone(userDTO.getCellphone());
			user.setEmail(userDTO.getEmail());
			user.setLastName(userDTO.getLastName());
			user.setUsername(userDTO.getUsername());

			return DTOFactory.create(userRepository.save(user));

		} else {
			throw new UserNotFoundException("User not found, id: " + userDTO.getIdUser());
		}
	}

	/**
	 * 
	 * @param id
	 * @throws NotFoundException
	 */
	@Transactional
	public void delete(Long id) throws UserNotFoundException {

		Optional<User> optional = this.userRepository.findById(id);
		if (optional.isPresent()) {
			this.userRepository.delete(optional.get());
		} else {
			throw new UserNotFoundException("User not found, id: " + id);
		}
	}

	//////////////////////////////////////////////////////////////////
	///////////////////////////// QUERIES ////////////////////////////
	/////////////////////////////////////////////////////////////////

	// All methods that are not annotated with @Transactional
	// will be treated as a read mode transaction

	/**
	 * 
	 * @param id
	 * @return
	 * @throws UserNotFoundException
	 */
	public UserDTO findById(Long id) throws UserNotFoundException {

		Optional<User> optional = this.userRepository.findById(id);
		if (optional.isPresent()) {
			return DTOFactory.create(optional.get());
		} else {
			throw new UserNotFoundException("User not found, id: " + id);
		}
	}

	/**
	 * 
	 * @param email
	 * @return
	 * @throws UserNotFoundException(
	 */
	public UserDTO findByEmail(String email) throws UserNotFoundException {

		User user = this.userRepository.findByEmail(email);
		if (user != null) {
			UserDTO response = DTOFactory.create(user);
			return response;
		} else {
			throw new UserNotFoundException("User not found, email: " + email);
		}
	}

	/**
	 * @return
	 */
	public ListUsersDTO findAll() {

		List<UserDTO> listUsers = DTOFactory.getListUsers(this.userRepository.findAll());

		ListUsersDTO listDTO = new ListUsersDTO();
		if (listUsers != null && listUsers.isEmpty() == false) {
			listDTO.setUsers(listUsers);
		}

		return listDTO;
	}
}
