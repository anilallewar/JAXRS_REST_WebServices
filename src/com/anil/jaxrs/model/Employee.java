package com.anil.jaxrs.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Employee")
// If you want you can define the order in which the fields are written
// Optional
@XmlType(propOrder = { "employeeId", "name", "designation", "department",
		"addresses" })
@Entity
@Table(name = "Employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employeeId", nullable = false)
	private long employeeId;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "designation", nullable = false, length = 128)
	private String designation;

	/**
	 * fetch = FetchType.EAGER - Will get the addresses associated for each
	 * request separately as the mapping is on single entity basis. So you would
	 * have 1 select statement to the ADDRESS table fired for each Network
	 * Request Id
	 * 
	 * fetch = FetchType.LAZY - Will fetch data only from the EMPLOYEE table
	 * 
	 * You don't have to (must not) define any physical mapping in the mappedBy
	 * side.
	 * 
	 * CascadeType - PERSIST => Saves the new entity to database REMOVE =>
	 * Delete the entity from database
	 * 
	 * The inverse side of a bidirectional relationship must refer to its owning
	 * side by using the mappedBy element of the @OneToOne, @OneToMany, or @ManyToMany
	 * annotation.
	 */
	@OneToMany(mappedBy = "associatedEmployee", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Address.class)
	private Set<Address> addresses = null;

	@Column(name = "department", length = 128)
	private String department;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the designation
	 */
	@XmlElement(name = "Designation")
	public String getDesignation() {
		return designation;
	}

	/**
	 * @param designation
	 *            the designation to set
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * @return the employeeId
	 */
	@XmlAttribute(name = "Id")
	public long getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId
	 *            the employeeId to set
	 */
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the address
	 */
	// XmLElementWrapper generates a wrapper element around XML representation
	@XmlElementWrapper(name = "Address_List")
	@XmlElement(name = "Address")
	public Set<Address> getAddresses() {
		return addresses;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void addAddress(Address address) {
		if (this.addresses == null) {
			this.addresses = new HashSet<Address>(0);
		}
		this.addresses.add(address);
	}

	/**
	 * @param addresses
	 *            the addresses to set
	 */
	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	/**
	 * @param department
	 *            the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the department
	 */
	// Not to be shown in XML
	@XmlElement(name = "Department")
	public String getDepartment() {
		return department;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Id: ").append(this.employeeId).append(" -Name: ").append(
				this.name).append(" -Designation: ").append(this.designation)
				.append(" -Department: ").append(this.department).append(
						" -Addresses: ").append(this.addresses);

		return sb.toString();
	}
}
