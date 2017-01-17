package com.RestAPI;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;




public class JavaRestAPITest {
	//connection data
			private static final String clientID="appid";
			private static final String clientSecret="secret key";
			private static final String redirectURI="https://localhost:8443/callback";
			private static String tokenURL=null;
			static final String GRANTSERVICE = "/services/oauth2/token?grant_type=password";
			private static final String environementURL="https://login.salesforce.com";
			private static final String userName="username";
			private static final String password="password";
			private static final String securityToken="securitytoken";
			
			private static String accessToken=null;
			private static String instanceURL=null;

	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		System.out.println("<--------------- Getting a access Token ------------->");
		tokenURL= environementURL+ "/services/oauth2/token";
		HttpClient httpClient=new HttpClient();
		org.apache.http.client.HttpClient httpClient1 = HttpClientBuilder.create().build();
		String loginURL = environementURL +
                GRANTSERVICE +
                "&client_id=" + URLEncoder.encode(clientID,"UTF-8") +
                "&client_secret=" + URLEncoder.encode(clientSecret,"UTF-8") +
                "&username=" + URLEncoder.encode(userName,"UTF-8") +
                "&password=" + URLEncoder.encode(password+securityToken, "UTF-8");
		HttpPost httpPost = new HttpPost(loginURL);
		//System.out.println(loginURL);
		/*PostMethod postMethod=new PostMethod(tokenURL);
		postMethod.addParameter("grant_type","password");
		postMethod.addParameter("client_id", clientID);
		postMethod.addParameter("client_secret", clientSecret);
		postMethod.addParameter("redirect_uri", redirectURI);
		postMethod.addParameter("username", userName);
		postMethod.addParameter("password",password+securityToken);
		postMethod.getPath();*/
		HttpResponse response=null;
		
		
		try {
			//httpClient.executeMethod(postMethod);
			response=httpClient1.execute(httpPost);
			/*JSONObject jsnResponse=new JSONObject(new JSONTokener(new InputStreamReader(postMethod.getResponseBodyAsStream())));
			System.out.println(response.getStatusLine());
			System.out.println(response);
			System.out.println(postMethod.getStatusCode()+postMethod.getStatusText());
			accessToken=jsnResponse.getString("access_token");
			instanceURL=jsnResponse.getString("instance_url");
			System.out.println(accessToken);
			System.out.println(instanceURL);
			//postMethod.ger
			 * *
			 */
			//createAccount(accessToken,instanceURL);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            System.out.println("Error authenticating to Force.com: "+response.getStatusLine().getStatusCode());
            // Error is in EntityUtils.toString(response.getEntity())
            return;
        }
		
		String getResult = null;
        try {
            getResult = EntityUtils.toString(response.getEntity());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
 
        JSONObject jsonObject = null;
        
 
        try {
            jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();
            accessToken = jsonObject.getString("access_token");
            instanceURL = jsonObject.getString("instance_url");
            createAccount(accessToken,instanceURL);
        } catch (JSONException | IOException jsonException) {
            jsonException.printStackTrace();
        }
		
	}
	
	private static String createAccount(String accessToken2,String instanceURL1) throws JSONException, ClientProtocolException, IOException{
		String uri = instanceURL1 + "/services/data/v37.0/sobjects/Account/";
        
		 
        //create the JSON object containing the new lead details.
        JSONObject lead = new JSONObject();
        lead.put("Name", "REST APIss");
        

        System.out.println("JSON for lead record to be inserted:\n" + lead.toString(1));

        //Construct the objects needed for the request
        org.apache.http.client.HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(uri);
        BasicHeader oauthHeader = new BasicHeader("Authorization", "OAuth " + accessToken2) ;
        BasicHeader prettyPrintHeader = new BasicHeader("X-PrettyPrint", "1");
        httpPost.addHeader(oauthHeader);
        httpPost.addHeader(prettyPrintHeader);
        // The message we are going to post
        StringEntity body = new StringEntity(lead.toString(1));
        body.setContentType("application/json");
        httpPost.setEntity(body);

        //Make the request
        HttpResponse response = httpClient.execute(httpPost);

        //Process the results
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode+""+response.getStatusLine());
	return null;
	}

}
