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
import org.json.JSONException;
import org.json.JSONObject;

import iSMSThread.jdbc.Requete;
import iSMSThread.jpa.entities.Sms;

public class UtilClass {
	private static Requete req = new Requete();
	static String CURRENCY_LAYER_ACCESS_KEY="535e3668e0b498a0a39b9f707600140d";
    static String currencies = "EUR,XOF";
    static String source = "USD";
    static String format= "1";
    static String url = "http://apilayer.net/api/live?access_key="+CURRENCY_LAYER_ACCESS_KEY+"&currencies="+currencies+"&source="+source+"&format="+format;
	
	
	public static boolean isNumber(String string) {
	    try {
	        Long.parseLong(string);
	    } catch (Exception e) {
	        return false;
	    }
	    return true;
	}

	public static void updateDevise( )  { 
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
			            JSONObject obj = new JSONObject(chunk);
			            if(obj.has("success")){
			            	if(obj.getBoolean("success")){
			            		JSONObject quotes=(JSONObject) obj.get("quotes");
			            		req.updateDevise(Float.valueOf(quotes.getString("USDEUR")), Float.valueOf(quotes.getString("USDXOF")));
			            	}
			            } 
			            //System.out.println("Sender : "+sms.getSender()+"; destinatiaire : "+sms.getDestinataire()+"; Code retour : "+chunk);
			            //System.out.println("flag : "+flag);
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
	}

}
