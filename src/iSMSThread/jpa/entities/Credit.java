
package iSMSThread.jpa.entities;

import java.util.Date;

/**
 * The persistent class for the credit database table.
 * 
 */

public class Credit {

	private int idCredit;

	private Date dateAchat;

	private int valeur;

	private int valeurInitiale;

	private int validite;

	private int validiteInitiale;

	private Client client;
	
	private Integer operateur;

	public Credit() {
	}

	public Credit(int idCredit) {
		this.idCredit = idCredit;
	}

	public Credit(int idCredit, Date dateAchat, int valeur, int valeurInitiale, int validite, int validiteInitiale,
			Client client) {
		super();
		this.idCredit = idCredit;
		this.dateAchat = dateAchat;
		this.valeur = valeur;
		this.valeurInitiale = valeurInitiale;
		this.validite = validite;
		this.validiteInitiale = validiteInitiale;
		this.client = client;
	}

	public int getIdCredit() {

		return this.idCredit;
	}

	public void setIdCredit(int idCredit) {

		this.idCredit = idCredit;
	}

	public Date getDateAchat() {

		return this.dateAchat;
	}

	public void setDateAchat(Date dateAchat) {

		this.dateAchat = dateAchat;
	}

	public int getValeur() {

		return this.valeur;
	}

	public void setValeur(int valeur) {

		this.valeur = valeur;
	}

	public int getValeurInitiale() {

		return this.valeurInitiale;
	}

	public void setValeurInitiale(int valeurInitiale) {

		this.valeurInitiale = valeurInitiale;
	}

	public int getValidite() {

		return this.validite;
	}

	public void setValidite(int validite) {

		this.validite = validite;
	}

	public int getValiditeInitiale() {

		return this.validiteInitiale;
	}

	public void setValiditeInitiale(int validiteInitiale) {

		this.validiteInitiale = validiteInitiale;
	}
	
 

	public Integer getOperateur() {
		return operateur;
	}

	public void setOperateur(Integer operateur) {
		this.operateur = operateur;
	}

	public Client getClient() {

		return this.client;
	}

	public void setClient(Client client) {

		this.client = client;
	}

	@Override
	public String toString() {

		return "\tCredit [idCredit=" + idCredit + ", validite=" + validite + "]";
	}

}
