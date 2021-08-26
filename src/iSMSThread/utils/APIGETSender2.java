package iSMSThread.utils;

import java.io.BufferedInputStream; 
import java.io.IOException;
import java.io.InputStream; 
import java.net.ConnectException; 

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import iSMSThread.jdbc.Requete;
import iSMSThread.jpa.entities.Sms;

public class APIGETSender2 { 
	private static Requete req = new Requete();
	private static String host="http://cheapglobalsms.com/api_v1?";
	private static String ip="154.72.149.42";//	154.72.148.130
	private static String user="2205_iplans";
	private static String pwd="IPLANs@2017";
	private static String typeSMS="0";
	private static String flash="0";
	
	
	public static boolean isNumber(String string) {
	    try {
	        Long.parseLong(string);
	    } catch (Exception e) {
	        return false;
	    }
	    return true;
	}

	public static boolean envoyerSMS(Sms sms) throws IOException {
		    boolean flag=false;
		    String url=host+"sub_account="+user+"&sub_account_pass="+pwd+"&action=send_sms&sender_id="+sms.getSender().replace(" ", "%20")+"&message="+sms.getContenu().replace(" ", "%20")+"&recipients="+sms.getDestinataire();
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url); 
			CloseableHttpResponse response;
			try {
				  response = httpclient.execute(httpget);
			      System.out.println("1------------------------------------1");
			      System.out.println(response.getStatusLine());
			      System.out.println("1------------------------------------1");
			      
			      // Get hold of the response entity
			      HttpEntity entity = response.getEntity();

			      // If the response does not enclose an entity, there is no need
			      // to bother about connection release
			      byte[] buffer = new byte[1024];
			      if (entity != null) {
			    	  System.out.println("response : "+entity);
			        InputStream inputStream = entity.getContent();
			        try {
			          int bytesRead = 0;
			          BufferedInputStream bis = new BufferedInputStream(inputStream);
			          while ((bytesRead = bis.read(buffer)) != -1) {
			            String chunk = new String(buffer, 0, bytesRead);
			            if((chunk.contains("batch_id")) && (chunk.contains(":1"))){ flag=true;}else{req.echecEnvoi(sms,1);} 
			            System.out.println("Sender : "+sms.getSender()+"; destinatiaire : "+sms.getDestinataire()+"; Code retour : "+chunk);
			            System.out.println("flag : "+flag);
			          }
			        } catch (IOException ioException) {
			          // In case of an IOException the connection will be released
			          // back to the connection manager automatically
			          ioException.printStackTrace();
			        } catch (RuntimeException runtimeException) {
			          // In case of an unexpected exception you may want to abort
			          // the HTTP request in order to shut down the underlying
			          // connection immediately.
			          httpget.abort();
			          runtimeException.printStackTrace();
			        } finally {
			          // Closing the input stream will trigger connection release
			          try {
			            inputStream.close();
			          } catch (Exception ignore) {
			          }
			        }
			      }
			}catch (ConnectException e) { 
				System.err.println("Problème d'internet :"+ e.getMessage());
				e.printStackTrace();
			} 
			catch (ClientProtocolException e) { 
				e.printStackTrace();
			} catch (IOException e) { 
				e.printStackTrace();
			} 
			return flag;
	}
}