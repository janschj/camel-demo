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

import java.util.Map;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.util.component.AbstractApiEndpoint;
import org.apache.camel.util.component.ApiMethod;
import org.apache.camel.util.component.ApiMethodPropertiesHelper;

/**
 * Represents a HelloWorld endpoint.
 */
@UriEndpoint(firstVersion = "2.15.0", scheme = "google-calendar", title = "Google Calendar", syntax = "google-calendar:apiName/methodName", consumerClass = HelloWorldConsumer.class, consumerPrefix = "consumer", label = "api,cloud")
public class HelloWorldEndpoint extends AbstractApiEndpoint<HelloWorldApiName, HelloWorldConfiguration> {

	@UriParam
    private HelloWorldConfiguration configuration;

	  private Object apiProxy;

	public HelloWorldEndpoint(String uri, HelloWorldComponent component, HelloWorldApiName apiName, String methodName,
			HelloWorldConfiguration endpointConfiguration) {
		super(uri, component, apiName, methodName, HelloWorldApiCollection.getCollection()
				.getHelper(apiName),
				endpointConfiguration);
		this.configuration = endpointConfiguration;
	}

	public Producer createProducer() throws Exception {
		return new HelloWorldProducer(this);
	}

	public Consumer createConsumer(Processor processor) throws Exception {
		return new HelloWorldConsumer(this, processor);
	}

	public boolean isSingleton() {
		return true;
	}

	@Override
	protected void afterConfigureProperties() {
	       switch (apiName) {
	        case API1:
	            apiProxy = getClient().method1();
	            break;                
	        case API2:
	            apiProxy = getClient().method2();
	            break;                
	        default:
	            throw new IllegalArgumentException("Invalid API name " + apiName);
	        }
	}

    public HelloWorldClient getClient() {
        return ((HelloWorldComponent)getComponent()).getClient(configuration);
    }

	@Override
	public Object getApiProxy(ApiMethod method, Map<String, Object> args) {
        return apiProxy;	
        }

	@Override
	protected ApiMethodPropertiesHelper<HelloWorldConfiguration> getPropertiesHelper() {
        return HelloWorldPropertiesHelper.getHelper();
	}

	@Override
	protected String getThreadProfileName() {
        return HelloWorldConstants.THREAD_PROFILE_NAME;	}
}
