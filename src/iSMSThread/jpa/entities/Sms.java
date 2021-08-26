
package iSMSThread.jpa.entities;

/**
 *
 * @author Néhémie
 */

public class Sms {

	private Integer idSMS;

	private String sender;

	private String destinataire;

	private String contenu;

	private double cout;

	private boolean etat;
	
	private Integer operateur;
	
	private Integer mode;
	
	private Integer rstate;

	private Client clientidClient;

	public Sms() {
	}

	public Sms(Integer idSMS) {
		this.idSMS = idSMS;
	}

	public Sms(Integer idSMS, String destinataire) {
		this.idSMS = idSMS;
		this.destinataire = destinataire;
	}

	public Integer getIdSMS() {

		return idSMS;
	}

	public void setIdSMS(Integer idSMS) {

		this.idSMS = idSMS;
	}

	public String getSender() {

		return sender;
	}

	public void setSender(String sender) {

		this.sender = sender;
	}

	public String getDestinataire() {

		return destinataire;
	}

	public void setDestinataire(String destinataire) {

		this.destinataire = destinataire;
	}

	public String getContenu() {

		return contenu;
	}

	public void setContenu(String contenu) {

		this.contenu = contenu;
	}

	public double getCout() {

		return cout;
	}

	public void setCout(double cout) {

		this.cout = cout;
	}

	public boolean isEtat() {

		return etat;
	}

	public void setEtat(boolean etat) {

		this.etat = etat;
	}
	
	
 

	public Integer getOperateur() {
		return operateur;
	}

	public void setOperateur(Integer operateur) {
		this.operateur = operateur;
	}

	public Client getClientidClient() {

		return clientidClient;
	}

	public void setClientidClient(Client clientidClient) {

		this.clientidClient = clientidClient;
	}
	
	

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}
	
	
	

	public Integer getRstate() {
		return rstate;
	}

	public void setRstate(Integer rstate) {
		this.rstate = rstate;
	}

	@Override
	public String toString() {

		return "\tSms [idSMS=" + idSMS + ", sender=" + sender + ", destinataire=" + destinataire + ", contenu="
				+ contenu + ", cout=" + cout + ", etat=" + etat + ", clientidClient=" + clientidClient + "]";
	}

}
