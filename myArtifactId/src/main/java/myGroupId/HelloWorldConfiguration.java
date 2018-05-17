package myGroupId;

import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;
import org.apache.camel.spi.UriPath;

/**
 * Component configuration for GoogleCalendar component.
 */
@UriParams
public class HelloWorldConfiguration {

    @UriPath
    @Metadata(required = "true")
    private HelloWorldApiName apiName;

    @UriPath(enums = "calendarImport,clear,delete,get,insert,instances,list,move,patch,query,quickAdd,stop,update,watch")
    @Metadata(required = "true")
    private String methodName;


    @UriParam
    private String scopes;

    @UriParam
    private String clientId;

    @UriParam
    private String emailAddress;

    @UriParam
    private String clientSecret;

    @UriParam
    private String accessToken;

    @UriParam
    private String refreshToken;

    @UriParam
    private String applicationName;

    @UriParam
    private String p12FileName;

    @UriParam
    private String user;

    public HelloWorldApiName getApiName() {
        return apiName;
    }

    /**
     * What kind of operation to perform
     */
    public void setApiName(HelloWorldApiName apiName) {
        this.apiName = apiName;
    }

    public String getMethodName() {
        return methodName;
    }

    /**
     * What sub operation to use for the selected operation
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClientId() {
        return clientId;
    }

    /**
     * Client ID of the calendar application
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * The emailAddress of the Google Service Account.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * Client secret of the calendar application
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    /**
     * OAuth 2 access token. This typically expires after an hour so refreshToken is recommended for long term usage.
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * OAuth 2 refresh token. Using this, the Google Calendar component can obtain a new accessToken whenever the current one expires - a necessity if the application is long-lived.
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Google calendar application name. Example would be "camel-google-calendar/1.0"
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getScopes() {
        return scopes;
    }

    /**
     * Specifies the level of permissions you want a calendar application to have to a user account.
     * You can separate multiple scopes by comma.
     * See https://developers.google.com/google-apps/calendar/auth for more info.
     */
    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getP12FileName() {
        return p12FileName;
    }

    /**
     * The name of the p12 file which has the private key to use with the Google Service Account.
     */
    public void setP12FileName(String p12FileName) {
        this.p12FileName = p12FileName;
    }

    public String getUser() {
        return user;
    }

    /**
     * The email address of the user the application is trying to impersonate in the service account flow
     */
    public void setUser(String user) {
        this.user = user;
    }

}
