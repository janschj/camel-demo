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

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.TypeConverter;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.apache.camel.util.IntrospectionSupport;
import org.apache.camel.util.component.AbstractApiConsumer;

/**
 * The HelloWorld consumer.
 */
public class HelloWorldConsumer extends AbstractApiConsumer<HelloWorldApiName, HelloWorldConfiguration> {
 
    private final HelloWorldEndpoint endpoint;

    public HelloWorldConsumer(HelloWorldEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    protected Object doInvokeMethod(Map<String, Object> properties) throws RuntimeCamelException {
        AbstractHelloWorldRequest request = (AbstractHelloWorldRequest) super.doInvokeMethod(properties);
        try {
            TypeConverter typeConverter = getEndpoint().getCamelContext().getTypeConverter();
            for (Entry<String, Object> p : properties.entrySet()) {
                IntrospectionSupport.setProperty(typeConverter, request, p.getKey(), p.getValue());
            }
            return request.execute();
        } catch (Exception e) {
            throw new RuntimeCamelException(e);
        }
    }    
}
