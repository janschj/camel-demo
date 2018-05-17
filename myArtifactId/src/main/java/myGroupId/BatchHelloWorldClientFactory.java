package myGroupId;

import java.io.File;
import java.util.Collection;

import org.apache.camel.RuntimeCamelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import myGroupId.x.Credential;

public class BatchHelloWorldClientFactory implements HelloWorldClientFactory {

    private static final Logger LOG = LoggerFactory.getLogger(BatchHelloWorldClientFactory.class);
//    private NetHttpTransport transport;
 //   private JacksonFactory jsonFactory;

    public BatchHelloWorldClientFactory() {
//        this.transport = new NetHttpTransport();
//        this.jsonFactory = new JacksonFactory();
    }

    @Override
    public HelloWorldClient makeClient(String clientId, String clientSecret,
                               Collection<String> scopes, String applicationName, String refreshToken,
                               String accessToken, String emailAddress, String p12FileName, String user) {
 
    	boolean serviceAccount = false;
    	/*
         if emailAddress and p12FileName values are present, assume Google Service Account
        if (null != emailAddress && !"".equals(emailAddress) && null != p12FileName && !"".equals(p12FileName)) {
            serviceAccount = true;
        }
        if (!serviceAccount && (clientId == null || clientSecret == null)) {
            throw new IllegalArgumentException("clientId and clientSecret are required to create Google Calendar client.");
        }

        try {
            Credential credential;
            if (serviceAccount) {
                credential = authorizeServiceAccount(emailAddress, p12FileName, scopes, user);
            } else {
                credential = authorize(clientId, clientSecret, scopes);
                if (refreshToken != null && !"".equals(refreshToken)) {
                    credential.setRefreshToken(refreshToken);
                }
                if (accessToken != null && !"".equals(accessToken)) {
                    credential.setAccessToken(accessToken);
                }
            }
            return new Calendar.Builder(transport, jsonFactory, credential).setApplicationName(applicationName).build();
        } catch (Exception e) {
            throw new RuntimeCamelException("Could not create Google Calendar client.", e);
        }
        */
    	return null;
    }

    // Authorizes the installed application to access user's protected data.
    private Credential authorize(String clientId, String clientSecret, Collection<String> scopes) throws Exception {
        // authorize
    	/*
        return new HelloWorldCredential.Builder()
                .setJsonFactory(jsonFactory)
                .setTransport(transport)
                .setClientSecrets(clientId, clientSecret)
                .build();
                */
    	return null;
    }

    private Credential authorizeServiceAccount(String emailAddress, String p12FileName, Collection<String> scopes, String user) throws Exception {
    	/*
    	HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        // set the service account user when provided
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(emailAddress)
                .setServiceAccountPrivateKeyFromP12File(new File(p12FileName))
                .setServiceAccountScopes(scopes)
                .setServiceAccountUser(user)
                .build();
        return credential;
        */
    	return null;
    }
}