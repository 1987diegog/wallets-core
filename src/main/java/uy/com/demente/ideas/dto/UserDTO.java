package uy.com.demente.ideas.dto;

/**
 * @author 1987diegog
 */
public class UserDTO extends PersonDTO {

	private static final long serialVersionUID = 1L;

	private Long idUser;
	private String username;
	private String status;

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
