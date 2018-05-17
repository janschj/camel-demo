/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package myGroupId;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.spi.Metadata;
import org.apache.camel.util.component.AbstractApiComponent;

/**
 * Represents the component that manages {@link HelloWorldEndpoint}.
 */
public class HelloWorldComponent extends AbstractApiComponent<HelloWorldApiName, HelloWorldConfiguration, HelloWorldApiCollection> {

    @Metadata(label = "advanced")
    private HelloWorldClient client;
    @Metadata(label = "advanced")
    private HelloWorldClientFactory clientFactory;

    public HelloWorldComponent() {
        super(HelloWorldEndpoint.class, HelloWorldApiName.class, HelloWorldApiCollection.getCollection());
    }

    public HelloWorldComponent(CamelContext context) {
        super(context, HelloWorldEndpoint.class, HelloWorldApiName.class, HelloWorldApiCollection.getCollection());
    }

    @Override
    protected HelloWorldApiName getApiName(String apiNameStr) throws IllegalArgumentException {
        return HelloWorldApiName.fromValue(apiNameStr);
    }

    public HelloWorldClient getClient(HelloWorldConfiguration config) {
        if (client == null) {

            List<String> list = null;
            if (config.getScopes() != null) {
                String[] arr = config.getScopes().split(",");
                list = Arrays.asList(arr);
            }

            client = getClientFactory().makeClient(config.getClientId(),
                    config.getClientSecret(), list,
                    config.getApplicationName(), config.getRefreshToken(),
                    config.getAccessToken(), config.getEmailAddress(),
                    config.getP12FileName(), config.getUser());
        }
        return client;
    }

    public HelloWorldClientFactory getClientFactory() {
        if (clientFactory == null) {
            clientFactory = new BatchHelloWorldClientFactory();
        }
        return clientFactory;
    }

    @Override
    public HelloWorldConfiguration getConfiguration() {
        if (configuration == null) {
            configuration = new HelloWorldConfiguration();
        }
        return super.getConfiguration();
    }

    /**
     * To use the shared configuration
     */
    @Override
    public void setConfiguration(HelloWorldConfiguration configuration) {
        super.setConfiguration(configuration);
    }

    /**
     * To use the GoogleCalendarClientFactory as factory for creating the client.
     * Will by default use {@link BatchGoogleCalendarClientFactory}
     */
    public void setClientFactory(HelloWorldClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    protected Endpoint createEndpoint(String uri, String methodName, HelloWorldApiName apiName,
    		HelloWorldConfiguration endpointConfiguration) {
        endpointConfiguration.setApiName(apiName);
        endpointConfiguration.setMethodName(methodName);
        return new HelloWorldEndpoint(uri, this, apiName, methodName, endpointConfiguration);
    }
}