
package iSMSThread.thread;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; 

import iSMSThread.jdbc.Requete;
import iSMSThread.jpa.entities.Client;

public class ClientThread extends Thread { 

	private Requete req = new Requete();

	// Clients n'ayant pas de thread
	private List<Client> clients = new ArrayList<Client>();

	public ClientThread(String nom) {
		super(nom); 
	}

	public List<Client> getClients() {

		return clients;
	}

	public void setClients(List<Client> clients) {

		this.clients = clients;
	}

	public void run() {

		// verifie s'il y a de nouveaux clients toutes les 30s
		// recupere leurs id, et cree des threads (des SMSThread) pour ces
		// clients

		System.out.println(this.getName() + " ==> active state\n");

		while (true) {
		 try{
			this.setClients(this.listeClients());
			if(clients!=null)
			for (int i = 0; i < clients.size(); i++) {

				Client client = clients.get(i);

				SMSThread smsThread = new SMSThread("SMS deamon for client : ", client.getIdClient(),1);
				smsThread.start();

				req.aUnThread(client, true);

			}
			 } catch (Exception e) { 
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			try {

				System.out.println("\n" + this.getName() + " ==> sleeping state\n");
				sleep(500);
				System.out.println(this.getName() + " ==> active state\n");

				this.setClients(null);

			} catch (NullPointerException | InterruptedException e) {
				System.out.println(e.getMessage());
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}

	}

	public List<Client> listeClients() {

		List<Client> clients = new ArrayList<Client>();

		ResultSet result = req.listeClientsThread(false);

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
			System.out.println( e.getMessage());
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
