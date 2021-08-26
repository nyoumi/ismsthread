
package iSMSThread.thread;
 
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; 

import iSMSThread.jdbc.Requete;
import iSMSThread.jpa.entities.Sms;
import iSMSThread.utils.APIGETSender; 
import iSMSThread.utils.SMPPSender;
import java.net.URI;
public class Good extends Thread { 
	private Requete req = new Requete();

	private int idClient;

	private List<Sms> smsAEnvoyer = new ArrayList<Sms>();
	
	public Good() {  
	}

	public Good(String nom, int idClient) {
		super(nom + idClient);
		this.idClient = idClient;  
	}

	public List<Sms> getSmsAEnvoyer() {

		return smsAEnvoyer;
	}

	public void setSmsAEnvoyer(List<Sms> smsAEnvoyer) {

		this.smsAEnvoyer = smsAEnvoyer;
	}

	/**
	 * Redefinition de la methode Run de Thread
	 */
	public void run() {

		System.out.println(this.getName() + " ==> active state\n");

		while (true) {
			try {
		    this.setSmsAEnvoyer(this.listeSMS()); 
		    if(smsAEnvoyer!=null)
			for (int i = 0; i < smsAEnvoyer.size(); i++) {

				Sms sms = smsAEnvoyer.get(i);   
					if(sms.getOperateur()==0){ 
					this.envoyerSMS(sms);
					if (sms.isEtat())
						req.envoiReussi(sms, true); 
					    System.out.println("\n" + sms.toString() + " SENT By NEXTTEL? ===> " + sms.isEtat());
					}else if(sms.getOperateur()==1){
						if(APIGETSender.envoyerSMS(sms)){
							req.envoiReussi(sms, true); 
							System.out.println("\n" + sms.toString() + " SENT BY 1s2u? ===> " + sms.isEtat());
						}		
					} 
			}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} 
			
			
			try {

				System.out.println("\n" + this.getName() + " ===> sleeping state\n");
				sleep(1000);
				System.out.println(this.getName() + " ===> active state\n");

				this.setSmsAEnvoyer(null);

			} catch (NullPointerException | InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public List<Sms> listeSMS() {

		List<Sms> smsAEnvoyer = new ArrayList<Sms>();

		ResultSet result = req.listeSMSAEnvoyer(idClient, false);

		try {
			if(result!=null)
			while (result.next()) {

				int idSms = result.getInt(1);
				String senderID = result.getString("sender");
				String destinataire = result.getString("destinataire");
				String contenu = result.getString("contenu");
				Integer operateur=result.getInt("operateur");  
				contenu = contenu.replaceAll("[\r\n]+", " ");
				contenu = contenu.replaceAll("[èéêë]","e");
				contenu = contenu.replaceAll("[àáâãäå]","a");
				contenu = contenu.replaceAll("[òóôõöø]","o");
				contenu = contenu.replaceAll("[ìíîï]","i");
				contenu = contenu.replaceAll("[ùúûü]","u");
				contenu = contenu.replaceAll("[ÿ]","y");
				contenu = contenu.replaceAll("[ç]","c");
				contenu = contenu.replaceAll("[Ç]","C");
				contenu = contenu.replaceAll("[°]","-");
				contenu = contenu.replaceAll("[Ñ]","N");
				contenu = contenu.replaceAll("[ÙÚÛÜ]","U");
				contenu = contenu.replaceAll("[ÌÍÎÏ]","I");
				contenu = contenu.replaceAll("[ÈÉÊË]","E");
				contenu = contenu.replaceAll("[ÒÓÔÕÖØ]","O");
				contenu = contenu.replaceAll("[ÀÁÂÃÄÅ]","A");
				contenu = contenu.replaceAll("[ÀÁÂÃÄÅ]","A");
				contenu = contenu.replaceAll("\"","'");
				contenu = contenu.replaceAll("\\u00a0","");
                contenu = contenu.replaceAll("\\u009D"," ");
				contenu = contenu.replaceAll("(?<=\\d) +(?=\\d)", "");
				contenu = contenu.replaceAll("&","%26");
				//contenu = contenu.replaceAll("+","plus");
				contenu = contenu.replaceAll("&thinsp;"," ");
                contenu = contenu.replaceAll("&ensp;"," ");
                contenu = contenu.replaceAll("&emsp;"," ");
                contenu = contenu.replaceAll("&#09;"," ");
				contenu = contenu.replaceAll("%","%25");
				contenu = contenu.replaceAll("#","%23");
				contenu = contenu.replaceAll("=","%3D");
                contenu = contenu.replaceAll("Â"," ");
				//System.err.println(contenu);
                contenu = URLEncoder.encode(contenu, "UTF-8");
				Sms sms = new Sms();
				sms.setIdSMS(idSms);
				sms.setSender(senderID);
				sms.setDestinataire(destinataire);
				sms.setContenu(contenu);
				sms.setOperateur(operateur);
				//System.err.println(contenu);
				smsAEnvoyer.add(sms);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally
		{ 
			 try { 
			       result.close();
				   req.closeConnection();
				 } catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				 }   
			 }

		return smsAEnvoyer;
	}

	public void envoyerSMS(Sms sms) {

		String sender = sms.getSender();
		String destiniation = sms.getDestinataire();
		String message = sms.getContenu();
		Boolean envoye = false;

		String params[] = { sender, destiniation, message };

		envoye = SMPPSender.main(params);
		sms.setEtat(envoye);

	}

}
