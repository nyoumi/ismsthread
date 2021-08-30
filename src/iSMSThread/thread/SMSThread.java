
package iSMSThread.thread;
 
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import iSMSThread.jdbc.Requete;
import iSMSThread.jpa.entities.Sms;
import iSMSThread.utils.*;

public class SMSThread extends Thread { 
	private final Requete req = new Requete();

	private int idClient;
	private int mode;

	private List<Sms> smsAEnvoyer = new ArrayList<Sms>();
	private final int interval =200;
	private int debut=0;
	private int fin= interval;
	private boolean flag=true;
	private Long heure;
	
	public SMSThread() {  
	}

	public SMSThread(String nom, int idClient, int mode) {
		super(nom + idClient);
		this.idClient = idClient; 
		this.mode = mode; 
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
		    if(flag){
		    this.setSmsAEnvoyer(null);
			try {
		    	//System.out.println("Is good client  :"+this.idClient);
		    	debut=0;
		    	fin= interval;
		    this.setSmsAEnvoyer(this.listeSMS());
		    if(smsAEnvoyer!=null)
		    if(smsAEnvoyer.size()<= interval){
				for (Sms sms : smsAEnvoyer) {
					String[] prefixesMTN = new String[] {"+23767","23767"};
					for (String prefixe:prefixesMTN
						 ) {
						if(sms.getDestinataire().startsWith(prefixe)){
							this.envoyerBGSMS(sms);
							if (sms.isEtat())
								req.envoiReussi(sms, true);
							System.out.println("\n" + sms.toString() + " SENT By MTN ? ===> " + sms.isEtat());
						}else{
							if (APIGETSender3.envoyerSMS(sms)) {
								req.envoiReussi(sms, true);
								System.out.println("\n" + sms.toString() + " SENT BY sms.etech-keys.com ===> " + sms.isEtat());
							}
						}

					}


					//System.out.println("debut "+debut+" fin "+fin+" SMSs : "+i);
					/*
					if (sms.getOperateur() == 0) {
						this.envoyerSMS(sms);
						if (sms.isEtat())
							req.envoiReussi(sms, true);
						System.out.println("\n" + sms.toString() + " SENT By NEXTTEL? ===> " + sms.isEtat());
					} else if (sms.getOperateur() == 4) {
						this.envoyerBGSMS(sms);
						if (sms.isEtat())
							req.envoiReussi(sms, true);
						System.out.println("\n" + sms.toString() + " SENT By MTN ? ===> " + sms.isEtat());
					} else if (sms.getOperateur() == 3) {
						if (APIGETSender3.envoyerSMS(sms)) {
							req.envoiReussi(sms, true);
							System.out.println("\n" + sms.toString() + " SENT BY sms.etech-keys.com ===> " + sms.isEtat());
						}
					} else if (sms.getOperateur() == 1) {
						if (APIGETSender2.envoyerSMS(sms)) {
							req.envoiReussi(sms, true);
							System.out.println("\n" + sms.toString() + " SENT BY cheapglobalsms? ===> " + sms.isEtat());
						}
					} else if (sms.getOperateur() == 2) {
						if (APIGETSender.envoyerSMS(sms)) {
							req.envoiReussi(sms, true);
							System.out.println("\n" + sms.toString() + " SENT BY 1s2u? ===> " + sms.isEtat());
						}
					}
					*/
				}

		    }else{
		    	flag=false;
		    	//System.out.println(" Ok envoi multiple");
		    	heure=Calendar.getInstance().getTime().getTime();
		    	int nber= smsAEnvoyer.size()/ interval;
		    	int reste= smsAEnvoyer.size()% interval;

		    	for(int j=0; j < nber; j++){
		    		/*		    		System.out.println(smsAEnvoyer.subList(debut, fin));
		    		System.out.println(j);
		    		System.out.println("interval : ["+debut+" - "+(debut+5)+"]");*/
		    		new Thread(){
	    	        	  private int ldebut=debut;
	    	        	  private int lFin=fin;
		    	          public void run() {
		    	  			for (int i = ldebut; i <lFin  ; i++) {
		    	  				// System.out.println("debut "+ldebut+" fin "+lFin+" SMS : "+i);

		    					    Sms sms = smsAEnvoyer.get(i);
		    						if(sms.getOperateur()==0){
		    						envoyerSMS(sms);
		    						if (sms.isEtat())
		    							req.envoiReussi(sms, true);
		    						    System.out.println("\n" + sms.toString() + " SENT By NEXTTEL? ===> " + sms.isEtat());
		    						}else if(sms.getOperateur()==4){
		    							envoyerBGSMS(sms);
		    							if (sms.isEtat())
		    								req.envoiReussi(sms, true);
		    							    System.out.println("\n" + sms.toString() + " SENT By MTN ? ===> " + sms.isEtat());
		    					    }else if(sms.getOperateur()==3){
		    							boolean val;
										try {
											val = APIGETSender3.envoyerSMS(sms);
			    							if(val){
			    								req.envoiReussi(sms, true);
			    								System.out.println("\n" + sms.toString() + " SENT BY sms.etech-keys.com ? ===> " + sms.isEtat());
			    							}
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
		    						}else if(sms.getOperateur()==1){
		    							boolean val;
										try {
											val = APIGETSender2.envoyerSMS(sms);
			    							if(val){
			    								req.envoiReussi(sms, true);
			    								System.out.println("\n" + sms.toString() + " SENT BY cheapglobalsms? ===> " + sms.isEtat());
			    							}
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
		    						}else if(sms.getOperateur()==2){
		    							try {
											if(APIGETSender.envoyerSMS(sms)){
												req.envoiReussi(sms, true);
												System.out.println("\n" + sms.toString() + " SENT BY 1s2u? ===> " + sms.isEtat());
											}
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
		    						}
		    						if(flag) break;
		    				}

		    	          }
		    	      }.start();
		    	      incrementBonnes();

		    	}

		    	if((smsAEnvoyer.size()% interval)>0){
	    		new Thread(){
   	        	 private int lDebut= smsAEnvoyer.size()-(smsAEnvoyer.size()% interval);
   	        	 private int lFin= smsAEnvoyer.size();
	    	          public void run() {
	    	  			for (int i = lDebut; i < lFin; i++) {
	    	  				//System.out.println("debut "+lDebut+" fin "+lFin+" SMS : "+i);
	    					Sms sms = smsAEnvoyer.get(i);
	    						if(sms.getOperateur()==0){
	    						envoyerSMS(sms);
	    						if (sms.isEtat())
	    							req.envoiReussi(sms, true);
	    						    System.out.println("\n" + sms.toString() + " SENT By NEXTTEL? ===> " + sms.isEtat());
	    						}else if(sms.getOperateur()==4){
	    							envoyerBGSMS(sms);
	    							if (sms.isEtat())
	    								req.envoiReussi(sms, true);
	    							    System.out.println("\n" + sms.toString() + " SENT By MTN ? ===> " + sms.isEtat());
	    					    }else if(sms.getOperateur()==3){
	    							boolean val;
									try {
										val = APIGETSender3.envoyerSMS(sms);
		    							if(val){
		    								req.envoiReussi(sms, true);
		    								System.out.println("\n" + sms.toString() + " SENT BY sms.etech-keys.com  ===> " + sms.isEtat());
		    							}
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
	    						}else if(sms.getOperateur()==1){
	    							boolean val;
									try {
										val = APIGETSender2.envoyerSMS(sms);
		    							if(val){
		    								req.envoiReussi(sms, true);
		    								System.out.println("\n" + sms.toString() + " SENT BY cheapglobalsms? ===> " + sms.isEtat());
		    							}
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
	    						}else if(sms.getOperateur()==2){
	    							try {
										if(APIGETSender.envoyerSMS(sms)){
											req.envoiReussi(sms, true);
											System.out.println("\n" + sms.toString() + " SENT BY 1s2u? ===> " + sms.isEtat());
										}
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
	    						}
	    						if(flag) break;
	    				}
	    	          }
	    	      }.start();
		    	}

			}
			} catch (Exception e) {
				System.out.println("Exception "+e.getMessage());
			}


			try {

				System.out.println("\n" + this.getName() + " ===> sleeping state\n");
				sleep(1000);
				System.out.println(this.getName() + " ===> active state\n");

				//this.setSmsAEnvoyer(null);

			} catch (NullPointerException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(!flag)
		if((Calendar.getInstance().getTime().getTime()-heure)>160000){
			//System.out.println(" Okkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkhhgghghghghghghghghghghghghghghghghghghghoiiooooooooooooooookkkkkkkk");
			flag=true;
			try {
				sleep(3000);
			} catch (NullPointerException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	}
	
	public synchronized void incrementBonnes(){
	      debut=debut+ interval;
	      fin=fin+ interval;
/*	      System.out.println("debut : "+debut);
	      System.out.println("fin : "+fin);*/
	}
	
 

	public List<Sms> listeSMS() {
		ResultSet result=null;
		List<Sms> smsAEnvoyer = new ArrayList<Sms>();
		if(mode==2){
		  result = req.listeSMSAEnvoyer(idClient, false, "ATTENTE");
		} else{
			 result = req.listeSMSAEnvoyerByMode(false, 1);	
		}

		try {
			if(result!=null)
			while (result.next()) {

				int idSms = result.getInt(1);
				String senderID = result.getString("sender");
				String destinataire = result.getString("destinataire");
				String contenu = result.getString("contenu");
				Integer operateur=result.getInt("operateur");  
				Integer mode=result.getInt("mode");  
				contenu = contenu.replaceAll("[\r\n]+", " ");
				contenu = contenu.replaceAll("[èéêë]","e");
				contenu = contenu.replaceAll("[œ]","oe");
				contenu = contenu.replaceAll("[Œ]","OE");
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
				
				
				senderID = senderID.replaceAll("[\r\n]+", " ");
				senderID = senderID.replaceAll("[èéêë]","e");
				senderID = senderID.replaceAll("[àáâãäå]","a");
				senderID = senderID.replaceAll("[òóôõöø]","o");
				senderID = senderID.replaceAll("[ìíîï]","i");
				senderID = senderID.replaceAll("[ùúûü]","u");
				senderID = senderID.replaceAll("[ÿ]","y");
				senderID = senderID.replaceAll("[ç]","c");
				senderID = senderID.replaceAll("[Ç]","C");
				senderID = senderID.replaceAll("[°]","-");
				senderID = senderID.replaceAll("[Ñ]","N");
				senderID = senderID.replaceAll("[ÙÚÛÜ]","U");
				senderID = senderID.replaceAll("[ÌÍÎÏ]","I");
				senderID = senderID.replaceAll("[ÈÉÊË]","E");
				senderID = senderID.replaceAll("[ÒÓÔÕÖØ]","O");
				senderID = senderID.replaceAll("[ÀÁÂÃÄÅ]","A");
				senderID = senderID.replaceAll("[ÀÁÂÃÄÅ]","A");
				senderID = senderID.replaceAll("\"","'");
				senderID = senderID.replaceAll("\\u00a0","");
				senderID = senderID.replaceAll("(?<=\\d) +(?=\\d)", "");
				senderID = senderID.replaceAll("&","%26");
				
				
				//System.err.println(contenu);
				Sms sms = new Sms();
				sms.setIdSMS(idSms);
				sms.setSender(senderID);
				sms.setDestinataire(destinataire);
				sms.setContenu(contenu);
				sms.setOperateur(operateur);
				sms.setMode(mode);
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
		boolean envoye = false;

		String[] params = { sender, destiniation, message };

		envoye = SMPPSender.main(params);
		sms.setEtat(envoye);

	}
	
	public void envoyerBGSMS(Sms sms) {

		String sender = sms.getSender();
		String destiniation = sms.getDestinataire();
		String message = sms.getContenu();
		boolean envoye = false;

		String[] params = { sender, destiniation, message };

		try {
			envoye = MtnSender4.envoyerSMS(sms);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.err.println("BG response ============================================ "+envoye);
		sms.setEtat(envoye);

	}

}
