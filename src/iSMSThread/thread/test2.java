
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

public class test2 extends Thread { 
	private Requete req = new Requete();

	private int idClient;

	private List<Sms> smsAEnvoyer = new ArrayList<Sms>();
	private int debut=0;
	private int fin=5;
	private int nberThraed=0;
	private int compteur=0;
	
	public test2() {  
	}

	public test2(String nom, int idClient) {
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
			System.out.println("compteur : "+compteur); 
			System.out.println("nberThraed : "+nberThraed); 
		    if(compteur>=nberThraed){
			try {
		    	System.out.println("Is good"); 
		    	compteur=0;
		    	nberThraed=0;
		    	debut=0;
		    	fin=5;
		    this.setSmsAEnvoyer(this.listeSMS()); 
		    if(smsAEnvoyer!=null)
		    if(smsAEnvoyer.size()<=5){
		    	nberThraed=1;
			for (int i = 0; i < smsAEnvoyer.size(); i++) {
				System.out.println("debut "+debut+" fin "+fin+" SMSs : "+i);
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
		    	int nber= smsAEnvoyer.size()/5;
		    	int reste= smsAEnvoyer.size()%5;
		    	if(reste==0) nberThraed=nber; else nberThraed=nber+1;
		    	
		    	for(int j=0; j < nber; j++){
		    		/*		    		System.out.println(smsAEnvoyer.subList(debut, fin));
		    		System.out.println(j);
		    		System.out.println("interval : ["+debut+" - "+(debut+5)+"]");*/
		    		new Thread(){
	    	        	  private int ldebut=debut;
	    	        	  private int lFin=fin; 
		    	          public void run() { 
		    	  			for (int i = ldebut; i <lFin  ; i++) { 
		    	  				 System.out.println("debut "+ldebut+" fin "+lFin+" SMS : "+i);
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
		    	      incrementBonnes();
		    	     
		    	} 
		    	
		    	if((smsAEnvoyer.size()%5)>0){
	    		new Thread(){
   	        	 private int lDebut= smsAEnvoyer.size()-(smsAEnvoyer.size()%5);
   	        	 private int lFin= smsAEnvoyer.size();
	    	          public void run() {  
	    	  			for (int i = lDebut; i < lFin; i++) {
	    	  				System.out.println("debut "+lDebut+" fin "+lFin+" SMS : "+i);
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

	}
	
	public synchronized void incrementBonnes(){
	      debut=debut+5;
	      fin=fin+5; 
/*	      System.out.println("debut : "+debut);
	      System.out.println("fin : "+fin);*/
	}
	
	public synchronized void incrementCompteur(){
		compteur++;
/*		System.out.println("compteur : "+compteur);*/
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
