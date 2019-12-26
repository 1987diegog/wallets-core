package uy.com.demente.ideas.business.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javassist.NotFoundException;
import uy.com.demente.ideas.business.repository.IUserRepository;
import uy.com.demente.ideas.dto.ListUsersDTO;
import uy.com.demente.ideas.dto.UserDTO;
import uy.com.demente.ideas.model.User;
import uy.com.demente.ideas.view.resources.factory.BOFactory;
import uy.com.demente.ideas.view.resources.factory.DTOFactory;

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
	 * @param id
	 * @param userDTO
	 * @return
	 * @throws NotFoundException
	 */
	@Transactional
	public UserDTO update(UserDTO userDTO) throws NotFoundException {

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
			throw new NotFoundException("User not found, id: " + userDTO.getIdUser());
		}
	}

	/**
	 * 
	 * @param id
	 * @throws NotFoundException
	 */
	@Transactional
	public void delete(Long id) throws NotFoundException {

		Optional<User> optional = this.userRepository.findById(id);
		if (optional.isPresent()) {
			this.userRepository.delete(optional.get());
		} else {
			throw new NotFoundException("User not found, id: " + id);
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
	 * @throws NotFoundException
	 */
	public UserDTO findById(Long id) throws NotFoundException {

		Optional<User> optional = this.userRepository.findById(id);
		if (optional.isPresent()) {
			return DTOFactory.create(optional.get());
		} else {
			throw new NotFoundException("User not found, id: " + id);
		}
	}

	/**
	 * 
	 * @param email
	 * @return
	 * @throws NotFoundException
	 */
	public UserDTO findByEmail(String email) throws NotFoundException {

		User user = this.userRepository.findByEmail(email);
		if (user != null) {
			UserDTO response = DTOFactory.create(user);
			return response;
		} else {
			throw new NotFoundException("User not found, email: " + email);
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
