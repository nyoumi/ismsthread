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

public class APIGETSender { 
	private static Requete req = new Requete();
	private static String host="http://1s2u.com/sms/sendsms/sendsms.asp?";
	private static String ip="154.72.149.42";//	154.72.148.130
	private static String user="ngueka";
	private static String pwd="ngueka";
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
		    String url=host+"username="+user+"&password="+pwd+"&mno="+sms.getDestinataire()+"&msg="+sms.getContenu().replace(" ", "%20")+"&Sid="+sms.getSender().replace(" ", "%20")+"&fl="+flash+"&mt="+typeSMS+"&ipcl="+ip;
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url); 
			CloseableHttpResponse response;
			try {
				  response = httpclient.execute(httpget);
			      System.out.println("2------------------------------------2");
			      System.out.println(response.getStatusLine());
			      System.out.println("2------------------------------------2");
			      
			      // Get hold of the response entity
			      HttpEntity entity = response.getEntity();

			      // If the response does not enclose an entity, there is no need
			      // to bother about connection release
			      byte[] buffer = new byte[1024];
			      if (entity != null) {
			        InputStream inputStream = entity.getContent();
			        try {
			          int bytesRead = 0;
			          BufferedInputStream bis = new BufferedInputStream(inputStream);
			          while ((bytesRead = bis.read(buffer)) != -1) {
			            String chunk = new String(buffer, 0, bytesRead);
			            if((chunk.length()>6) && (chunk.length()<25)) flag=true; else{req.echecEnvoi(sms,1);} ;
			            System.out.println("Sender : "+sms.getSender()+"; destinatiaire : "+sms.getDestinataire()+"; Code retour : "+chunk);
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