
package iSMSThread.jpa.entities;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Néhémie
 */

public class Client {

	private Integer idClient;

	private String noms;

	private String prenoms;

	private String email;

	private String telephone;

	private String userID;

	private String password;
	
	private Integer operateur;
	
	private Integer type;

	private List<Sms> smsList;

	public Client() {
	}

	public Client(Integer idClient) {
		this.idClient = idClient;
	}

	public Client(Integer idClient, String noms, String email, String telephone, String userID, String password) {
		this.idClient = idClient;
		this.noms = noms;
		this.email = email;
		this.telephone = telephone;
		this.userID = userID;
		this.password = password;
	}

	public Integer getIdClient() {

		return idClient;
	}

	public void setIdClient(Integer idClient) {

		this.idClient = idClient;
	}

	public String getNoms() {

		return noms;
	}

	public void setNoms(String noms) {

		this.noms = noms;
	}

	public String getPrenoms() {

		return prenoms;
	}

	public void setPrenoms(String prenoms) {

		this.prenoms = prenoms;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public String getTelephone() {

		return telephone;
	}

	public void setTelephone(String telephone) {

		this.telephone = telephone;
	}

	public String getUserID() {

		return userID;
	}

	public void setUserID(String userID) {

		this.userID = userID;
	}

	public String getPassword() {

		return password;
	}

	public void setPassword(String password) {

		this.password = password;
	}
	
	

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@XmlTransient
	public List<Sms> getSmsList() {

		return smsList;
	}

	public void setSmsList(List<Sms> smsList) {

		this.smsList = smsList;
	}
	
	
	
 
	public Integer getOperateur() {
		return operateur;
	}

	public void setOperateur(Integer operateur) {
		this.operateur = operateur;
	}

	@Override
	public String toString() {

		return "Client [idClient=" + idClient + ", noms=" + noms + ", prenoms=" + prenoms + ", email=" + email
				+ ", telephone=" + telephone + ", userID=" + userID + ", password=" + password + ", smsList=" + smsList
				+ "]";
	}

}
