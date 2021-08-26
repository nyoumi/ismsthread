package iSMSThread.utils;

import iSMSThread.jdbc.Requete;
import iSMSThread.jpa.entities.Sms;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;


public class MtnSender4 {

    private static Requete req = new Requete();
    private static String host="154.72.148.108:8081/apiSendSms/sendSms";
    private static String ip="154.72.148.108";//	154.72.148.130



    public static boolean isNumber(String string) {
        try {
            Long.parseLong(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    public static String formatNumber(String number)
    {
        if(number.length()<10)
            return number;
        else if(number.substring(0, 3).equals("237")){
            return number.substring(3);
        }else return number;
    }

    public static boolean envoyerSMS(Sms sms) throws IOException {
        boolean flag=false;
        String regex = "\\d+";
        String url=host;
                //sms.getSender().replace(" ", "%20")+"&destinataire="+APIGETSender3.formatNumber(sms.getDestinataire())+"&message="+sms.getContenu().replace(" ", "%20");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        ArrayList<String> adresses = new ArrayList<String>();
        adresses.add(sms.getDestinataire());
        JSONArray jsonA = new JSONArray((adresses));

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair ("addresses",jsonA.toString()));
        params.add(new BasicNameValuePair("senderName", sms.getSender()));
        params.add(new BasicNameValuePair("message", sms.getContenu()));

        httpPost.setEntity(new UrlEncodedFormEntity(params));

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response;
        try {
            response = httpclient.execute(httpPost);
            System.out.println("3------------------------------------3");
            System.out.println(response.getStatusLine());
            System.out.println("3------------------------------------3");

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
                        String chunk = new String(buffer, 0, bytesRead , "UTF-8");
                        chunk=chunk.replaceAll("[\r\n]+", "");
                        chunk=chunk.replaceAll("\\s","");
                        char[] chars = chunk.toCharArray();
                        String resp = new String(chars);
                        if(resp.matches(regex)){ flag=true;}else{req.echecEnvoi(sms,1);}
                        System.out.println("Sender : "+sms.getSender()+"; destinatiaire : "+sms.getDestinataire()+"; Code retour : "+resp+" Size : "+resp.length());
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
                    httpPost.abort();
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