package uy.com.demente.ideas.wallets.model.response;

import java.io.Serializable;

/**
 * @author 1987diegog
 */
public abstract class PersonDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String lastName;
	private int age;
	private String cellphone;
	private String email;

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
