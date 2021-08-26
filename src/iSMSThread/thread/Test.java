
package iSMSThread.thread;
 
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; 

import iSMSThread.jdbc.Requete;
import iSMSThread.jpa.entities.Sms;
import iSMSThread.utils.APIGETSender; 
import iSMSThread.utils.SMPPSender;

public class Test extends Thread { 
	private Requete req = new Requete();

	private int idClient;

	private List<Sms> smsAEnvoyer = new ArrayList<Sms>();
	private int debut=0;
	private int nberThraed=0;
	private int compteur=0;
	
	public Test() {  
	}

	public Test(String nom, int idClient) {
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
		    if(compteur==nberThraed){
		    	compteur=0;
		    	nberThraed=0;
		    	debut=0;
		    this.setSmsAEnvoyer(this.listeSMS()); 
		    if(smsAEnvoyer!=null)
		    if(smsAEnvoyer.size()<=50){
		    	nberThraed=1;
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
			compteur++;
		    }else{
		    	int nber= smsAEnvoyer.size()/50;
		    	int reste= smsAEnvoyer.size()%50;
		    	if(reste==0) nberThraed=nber; else nberThraed=nber+1;
		    	for(int i=0; i < nber; i++){
		    		new Thread(){
		    	          public void run() { 
		    	  			for (int i = debut; i < debut+50; i++) {

		    					Sms sms = smsAEnvoyer.get(i);   
		    						if(sms.getOperateur()==0){ 
		    						envoyerSMS(sms);
		    						if (sms.isEtat())
		    							req.envoiReussi(sms, true); 
		    						    System.out.println("\n" + sms.toString() + " SENT By NEXTTEL? ===> " + sms.isEtat());
		    						}else if(sms.getOperateur()==1){
		    							boolean val;
										try {
											val = APIGETSender.envoyerSMS(sms);
			    							if(val){
			    								req.envoiReussi(sms, true); 
			    								System.out.println("\n" + sms.toString() + " SENT BY 1s2u? ===> " + sms.isEtat());
			    							}
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}		
		    						} 
		    						
		    				}
		    	  			incrementCompteur();
		    	  			
		    	          }
		    	      }.start();
		    	      debut=debut+50; 
		    	} 
		    	if(reste>0){
	    		new Thread(){
	    	          public void run() { 
	    	  			for (int i = debut; i < debut+reste; i++) {

	    					Sms sms = smsAEnvoyer.get(i);   
	    						if(sms.getOperateur()==0){ 
	    						envoyerSMS(sms);
	    						if (sms.isEtat())
	    							req.envoiReussi(sms, true); 
	    						    System.out.println("\n" + sms.toString() + " SENT By NEXTTEL? ===> " + sms.isEtat());
	    						}else if(sms.getOperateur()==1){
	    							boolean val;
									try {
										val = APIGETSender.envoyerSMS(sms);
		    							if(val){
		    								req.envoiReussi(sms, true); 
		    								System.out.println("\n" + sms.toString() + " SENT BY 1s2u? ===> " + sms.isEtat());
		    							}
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}		
	    						} 
	    						
	    				}
	    	  			incrementCompteur();
	    	  			
	    	          }
	    	      }.start();
		    	}
	    	      
		    }
			}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} 
			
			
			try {

				System.out.println("\n" + this.getName() + " ===> sleeping state\n");
				sleep(2000);
				System.out.println(this.getName() + " ===> active state\n");

				this.setSmsAEnvoyer(null);

			} catch (NullPointerException | InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	
	public synchronized void incrementDebut(){
		debut=debut+50; 
	}
	
	public synchronized void incrementCompteur(){
		compteur++;
	}

	public List<Sms> listeSMS() {

		List<Sms> smsAEnvoyer = new ArrayList<Sms>();

		ResultSet result = req.listeSMSAEnvoyer(idClient, false,"ATTENTE");

		try {
			if(result!=null)
			while (result.next()) {

				int idSms = result.getInt(1);
				String senderID = result.getString("sender");
				String destinataire = result.getString("destinataire");
				String contenu = result.getString("contenu");
				Integer operateur=result.getInt("operateur");  
				contenu = contenu.replaceAll("[\r\n]+", " ");
				contenu = contenu.replaceAll("[����]","e");
				contenu = contenu.replaceAll("[������]","a");
				contenu = contenu.replaceAll("[������]","o");
				contenu = contenu.replaceAll("[����]","i");
				contenu = contenu.replaceAll("[����]","u");
				contenu = contenu.replaceAll("[�]","y");
				contenu = contenu.replaceAll("[�]","c");
				contenu = contenu.replaceAll("[�]","C");
				contenu = contenu.replaceAll("[�]","-");
				contenu = contenu.replaceAll("[�]","N");
				contenu = contenu.replaceAll("[����]","U");
				contenu = contenu.replaceAll("[����]","I");
				contenu = contenu.replaceAll("[����]","E");
				contenu = contenu.replaceAll("[������]","O");
				contenu = contenu.replaceAll("[������]","A");
				contenu = contenu.replaceAll("[������]","A");
				contenu = contenu.replaceAll("\"","'");
				contenu = contenu.replaceAll("\\u00a0","");
				contenu = contenu.replaceAll("(?<=\\d) +(?=\\d)", "");
				contenu = contenu.replaceAll("&","%26");
				//contenu = contenu.replaceAll("+","plus");
				contenu = contenu.replaceAll("%","%25");
				contenu = contenu.replaceAll("#","%23");
				contenu = contenu.replaceAll("=","%3D");
				//System.err.println(contenu);
				Sms sms = new Sms();
				sms.setIdSMS(idSms);
				sms.setSender(senderID);
				sms.setDestinataire(destinataire);
				sms.setContenu(contenu);
				sms.setOperateur(operateur);
				//System.err.println(contenu);
				smsAEnvoyer.add(sms);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally 
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
