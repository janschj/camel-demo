package myGroupId;

import org.apache.camel.util.component.ApiMethodPropertiesHelper;

/**
 * Singleton {@link ApiMethodPropertiesHelper} for GoogleCalendar component.
 */
public final class HelloWorldPropertiesHelper extends ApiMethodPropertiesHelper<HelloWorldConfiguration> {

    private static HelloWorldPropertiesHelper helper;

    private HelloWorldPropertiesHelper() {
        super(HelloWorldConfiguration.class, HelloWorldConstants.PROPERTY_PREFIX);
    }
    public static HelloWorldPropertiesHelper getHelper() {
        if (helper == null) {
            helper = new HelloWorldPropertiesHelper();
        }
        return helper;
    }
}
