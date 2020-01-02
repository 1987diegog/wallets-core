package uy.com.demente.ideas.wallets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * @author 1987diegog
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EntityListeners(AuditingEntityListener.class)
public abstract class Person implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "NAME", length = 50)
	private String name;

	@Column(name = "LASTNAME", length = 50)
	private String lastName;

	@Column(name = "AGE")
	private int age;

	@Column(name = "CELLPHONE", length = 50)
	private String cellphone;

	@Column(name = "EMAIL", unique = true, length = 50)
	private String email;


	// In our Note model we have annotated createdAt and updatedAt fields with @CreatedDate and @LastModifiedDate
	// annotations respectively. Now, what we want is that these fields should automatically get populated
	// whenever we create or update an entity. To achieve this, we need to do two things -

	// 1. Add Spring Data JPA’s AuditingEntityListener to the domain model @EntityListeners(AuditingEntityListener.class).
	// 2. Enable JPA Auditing in the main application (@EnableJpaAuditing)

	@Column(name="CREATED", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column(name="UPDATED", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;

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

	@Override
	public String toString() {

		return "[" + "\n name=" + name + "\n lastName=" + lastName + "\n  age=" + age + "\n cellPhone=" + cellphone
				+ "\n email=" + email + "\n]";
	}
}
