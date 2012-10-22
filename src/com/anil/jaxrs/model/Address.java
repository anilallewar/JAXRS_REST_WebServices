package com.anil.jaxrs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "Address")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "addressId", nullable = false)
	private long addressId;

	@Column(name = "address1", nullable = false, length = 255)
	private String address1;

	@Column(name = "address2", length = 255)
	private String address2;

	@Column(name = "city", length = 255)
	private String city;

	@Column(name = "state", length = 128)
	private String state;

	@Column(name = "country", length = 128)
	private String country;

	@Column(name = "zipcode")
	private int zipCode;

	/**
	 * @return the addressId
	 */
	public long getAddressId() {
		return addressId;
	}

	/**
	 * @param addressId
	 *            the addressId to set
	 */
	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}

	/**
	 * We need to mark this column as @XmlTransient as we use this column only
	 * to satisfy the ORM relationship; this attribute actually is NOT required
	 * in the Address class
	 * 
	 * @return the associatedEmployee
	 */
	@XmlTransient
	public Employee getAssociatedEmployee() {
		return associatedEmployee;
	}

	/**
	 * @param associatedEmployee
	 *            the associatedEmployee to set
	 */
	public void setAssociatedEmployee(Employee associatedEmployee) {
		this.associatedEmployee = associatedEmployee;
	}

	/**
	 * The many side of many-to-one bidirectional relationships must not define
	 * the mappedBy element. The many side is always the owning side of the
	 * relationship.
	 */
	@ManyToOne(targetEntity = Employee.class)
	@JoinColumn(name = "employeeId", nullable = false)
	private Employee associatedEmployee;

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1
	 *            the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2
	 *            the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the zipCode
	 */
	public int getZipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode
	 *            the zipCode to set
	 */
	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Address1: ").append(this.address1).append(" -Address2: ")
				.append(this.address2).append(" -City: ").append(this.city)
				.append(" -Country: ").append(this.country).append(" -State: ")
				.append(this.state).append(" -Zip: ").append(this.zipCode);

		return sb.toString();
	}
}
