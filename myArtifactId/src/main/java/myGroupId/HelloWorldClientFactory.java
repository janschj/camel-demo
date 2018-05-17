package myGroupId;

import java.util.Collection;

public interface HelloWorldClientFactory {

    HelloWorldClient makeClient(String clientId, String clientSecret, Collection<String> scopes,
                        String applicationName, String refreshToken, String accessToken,
                        String emailAddress, String p12FileName, String user);

}