/**
 * 
 */

package iSMSThread.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import iSMSThread.jpa.entities.Client;
import iSMSThread.jpa.entities.Credit;
import iSMSThread.jpa.entities.Sms;

/**
 * @author gorba
 *
 */
public class Requete {

	//JDBC jdbc = new JDBC("1pl@n$SA", "iplans", "jdbc:mysql://localhost:5785/ismsdb");
	JDBC jdbc = new JDBC("system", "administrator", "jdbc:mysql://localhost:5785/ismsdb");

	// ===============GESTION DES SMS

	/**
	 * selection des tous les sms dans la BD
	 * 
	 * @return
	 */
	public ResultSet listeSMS(int idClient) {

		String sql = "SELECT * FROM Sms WHERE Client_idClient = " + idClient + ";";

		return jdbc.executeQuery(sql);
	}
	
	
	public ResultSet listeSMSByMode(Integer mode) {

		String sql = "SELECT * FROM Sms WHERE mode = " + mode + ";";

		return jdbc.executeQuery(sql);
	}

	//
	/**
	 * recupere la valeur du numero du conge d'un reparateur
	 * 
	 * @param etat
	 * @return Calendar.getInstance().getTime()
	 */
	public ResultSet listeSMSAEnvoyer(int idClient, boolean etat) {

		String sql = "SELECT idSms, sender, destinataire, contenu, operateur, mode, rstate FROM Sms WHERE etat = " + etat
				+ " AND rstate = 0 AND Client_idClient = " + idClient + " AND NOW() >= dateEnvoi;";

		return jdbc.executeQuery(sql);
	}
	
	public ResultSet listeSMSAEnvoyerByMode(boolean etat, int mode) {

		String sql = "SELECT idSms, sender, destinataire, contenu, operateur, mode, rstate FROM Sms WHERE etat = " + etat
				+ " AND rstate = 0 AND mode ="+mode+" AND NOW() >= dateEnvoi;";

		return jdbc.executeQuery(sql);
	}

	/**
	 * modifie la valeur de l'etat d'envoi du SMS
	 * 
	 * @param sms
	 * @param etat
	 * @return
	 */
	public boolean envoiReussi(Sms sms, boolean etat) {

		String sql = "UPDATE Sms SET etat = " + etat + " WHERE idSMS = " + sms.getIdSMS() + ";";

		if (jdbc.executeUpdate(sql) > 0)
			return true;
		else
			return false;
	}
	
	
	public boolean echecEnvoi(Sms sms, int code) {

		String sql = "UPDATE Sms SET rstate = " + code + " WHERE idSMS = " + sms.getIdSMS() + ";";

		if (jdbc.executeUpdate(sql) > 0)
			return true;
		else
			return false;
	}

	// ===============GESTION DES CREDITS

	public ResultSet listeCreditsValide() {

		String sql = "SELECT idCredit, validite, dateAchat, validiteInitiale FROM Credit WHERE validite > " + 0 + ";";

		return jdbc.executeQuery(sql);
	}

	public boolean decrementerCredit(Credit credit, int validite) {

		String sql = "UPDATE Credit SET validite = " + validite + " WHERE idCredit = " + credit.getIdCredit() + ";";

		if (jdbc.executeUpdate(sql) > 0)
			return true;
		else
			return false;
	}

	// ==========================GESTION DES CLIENTS

	public ResultSet listeClients() {

		String sql = "SELECT idClient FROM Client ;";

		return jdbc.executeQuery(sql);
	}
	
	
	public ResultSet listeClientsByType(Integer type) {

		String sql = "SELECT idClient, type FROM Client WHERE type="+type+";";

		return jdbc.executeQuery(sql);
	}

	public ResultSet listeClientsThread(boolean thread) {

		String sql = "SELECT idClient, type FROM Client WHERE thread = " + thread + ";";

		return jdbc.executeQuery(sql);
	}

	public boolean aUnThread(Client client, boolean thread) {

		String sql = "UPDATE Client SET thread = " + thread + " WHERE idClient = " + client.getIdClient() + ";";

		if (jdbc.executeUpdate(sql) > 0)
			return true;
		else
			return false;
	}
	
	  public void closeConnection() throws SQLException {
		   jdbc.closeConnection(); 
	  }
	  
	  
	  //===========================================Partenaire============================================================
		public boolean updateDevise(Float usdeur, Float usdxof) {

			String sql = "UPDATE partenaire SET usdeur = " +usdeur+ ", usdxof ="+usdxof+", dollarcost="+usdxof+";";

			if (jdbc.executeUpdate(sql) > 0)
				return true;
			else
				return false;
		}

}
