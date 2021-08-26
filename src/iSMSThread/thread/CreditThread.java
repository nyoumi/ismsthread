
package iSMSThread.thread;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit; 

import iSMSThread.jdbc.Requete;
import iSMSThread.jpa.entities.Credit;

public class CreditThread extends Thread { 
	private static Requete req = new Requete();

	private static List<Credit> creditsValides = new ArrayList<Credit>();

	public CreditThread(String nom) {
		super(nom); 
	}

	public List<Credit> getCreditsValides() {

		return CreditThread.creditsValides;
	}

	public void setCreditsValides(List<Credit> creditsValides) {

		CreditThread.creditsValides = creditsValides;
	}

	public void run() {

		System.out.println(this.getName() + " ==> active state\n");

		while (true) {
		  try{
			this.setCreditsValides(this.listeCreditsVal());
            
			Date now=new Date();
			if(creditsValides!=null)
			for (int i = 0; i < creditsValides.size(); i++) {

				Credit credit = creditsValides.get(i);

				int validite =  this.getDifferenceDays(credit.getDateAchat(), now);
				if(credit.getValiditeInitiale()-validite>0) validite=credit.getValiditeInitiale()-validite; else validite=0;
				boolean decremente = false; 

				 decremente = req.decrementerCredit(credit, validite);

			     System.out.println(credit.toString() + "actual val=" + validite + "; DONE? ==> " + decremente);

				 

			}
			 }catch (Exception e) {
				 System.out.println(e.getMessage());	 
			     e.printStackTrace(); 
		    }
			try {

				System.out.println("\n" + this.getName() + " ==> sleeping state\n");
				sleep(360000);
				System.out.println(this.getName() + " ==> active state\n");

				this.setCreditsValides(null);

			} catch (NullPointerException | InterruptedException e) {
				System.out.println(e.getMessage());
				System.out.println(e.getMessage());
				e.printStackTrace();
			}

		}
	}

	public List<Credit> listeCreditsVal() {

		List<Credit> creditsValides = new ArrayList<Credit>();

		ResultSet result = req.listeCreditsValide();

		try {
			if(result!=null)
			while (result.next()) {

				int idCredit = result.getInt(1);
				int validite = result.getInt(2);
				Date dat = result.getDate(3);

				Credit credit = new Credit();
				credit.setIdCredit(idCredit);
				credit.setValidite(validite);
				credit.setDateAchat(dat);
				credit.setValiditeInitiale(result.getInt(4));

				creditsValides.add(credit);

			} 
		} catch (Exception e) {
			System.out.println( e.getMessage());
			e.printStackTrace();
		}finally 
		{ 
		 try { 
			   req.closeConnection();
		       result.close();
			 } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			 }   
		 }

		return creditsValides;
	}
	
	public int getDifferenceDays(Date d1, Date d2) {
	    Long diff = d2.getTime() - d1.getTime();
	    Long day=TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	    return day.intValue();
	}

}
