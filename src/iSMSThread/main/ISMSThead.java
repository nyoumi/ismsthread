
package iSMSThread.main;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import iSMSThread.jdbc.Requete;
import iSMSThread.jpa.entities.Client;
import iSMSThread.thread.ClientThread;
import iSMSThread.thread.CreditThread;
import iSMSThread.thread.SMSThread;
import iSMSThread.utils.UtilClass;
import sun.reflect.generics.tree.Tree;

public class ISMSThead { 
	private Requete req = new Requete();

	// Clients n'ayant pas de thread
	private static List<Client> clients = new ArrayList<Client>();
	private static SMSThread arrayThread[];
	private static SMSThread smsThreadAll;
	private static Long heure; 

	public List<Client> getClients() {

		return clients;
	}

	public void setClients(List<Client> clients) {

		this.clients = clients;
	}
	
	public Requete getReq(){
		return req;
	}

	public static void main(String[] args) { 
		ISMSThead ismsthread=new ISMSThead();
		try {
			
		    smsThreadAll = new SMSThread("SMS deamon for client : ", 0, 1);
			smsThreadAll.start(); 
			
			ismsthread.setClients(ismsthread.listeClients()); 
			// cree autant de thread qu'il y a de client pour l'envoi des SMS
			// chaque client a droit a un thread
			  clients=ismsthread.getClients();
			  arrayThread=new SMSThread[clients.size()];
			for (int i = 0; i <clients.size() ; i++) {
	
				Client client = ismsthread.getClients().get(i);
	
				arrayThread[i] = new SMSThread("SMS deamon for client : ", client.getIdClient(), 2);
				arrayThread[i].start();
	
				ismsthread.getReq().aUnThread(client, true);
			}
	
			// cree et lance le thread qui se charge de decrementer la validite des
			// credits tous les 24h
			CreditThread creditThread = new CreditThread("Credit deamon");
			creditThread.start();
			UtilClass.updateDevise();
			heure=Calendar.getInstance().getTime().getTime();
			
			while(true){
				if((Calendar.getInstance().getTime().getTime()-heure)>3600000){
				  System.err.println(" ===> Checking biging..................................................................");
				  heure=Calendar.getInstance().getTime().getTime();
                  if(!smsThreadAll.isAlive()){ 
                	  System.err.println(" ===> Checking OK All"); 
          		    smsThreadAll = new SMSThread("SMS deamon for client : ", 0, 1);
        			smsThreadAll.start();  
                  }
                  for (int i = 0; i <clients.size() ; i++) {
                	  if(!arrayThread[i].isAlive()){
                		  System.err.println(" ===> Checking OK array"); 
          				  arrayThread[i] = new SMSThread("SMS deamon for client : ", clients.get(i).getIdClient(), 2);
        				  arrayThread[i].start();
                	  }
                  }
                  UtilClass.updateDevise();
                  System.err.println(" ===> Checking end.....................................................................");
				}	
			}
	
			// cree et lance e thread qui verifie l'existence de nouveau client dans
			// la base de donnees
			//ClientThread clientThread = new ClientThread("New Client deamon");
			//clientThread.start();
		} catch (Exception e) {
			System.err.println(e.getMessage());
	    }
	}

	public List<Client> listeClients() {

		List<Client> clients = new ArrayList<Client>();

		ResultSet result = req.listeClientsByType(2);

		try {
		    if(result!=null)
			while (result.next()) {

				int idClient = result.getInt(1);
				int type =	result.getInt("type");
				Client client = new Client();
				client.setIdClient(idClient);
				client.setType(type);
				clients.add(client);
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
				   //etc. 
			}


		return clients;
	}

}
