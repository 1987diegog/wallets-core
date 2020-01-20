package uy.com.demente.ideas.wallets.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 1987diegog
 */
public class ListUsersDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<UserDTO> users;

	public ListUsersDTO() {
		this.users = new ArrayList<UserDTO>();
	}
	
	public List<UserDTO> getUsers() {
		return users;
	}

	public void setUsers(List<UserDTO> users) {
		this.users = users;
	}
}