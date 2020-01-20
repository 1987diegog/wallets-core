package uy.com.demente.ideas.wallets.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 1987diegog
 */
public abstract class PersonDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Name is mandatory")
	private String name;
	@NotBlank(message = "LastName is mandatory")
	private String lastName;
	@NotBlank(message = "Email is mandatory")
	private String email;
	@NotBlank(message = "Cellphone is mandatory")
	private String cellphone;
	@NotNull(message = "Age is mandatory")
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
