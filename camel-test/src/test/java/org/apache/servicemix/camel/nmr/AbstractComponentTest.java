package org.apache.servicemix.camel.nmr;

import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.servicemix.executors.ExecutorFactory;
import org.apache.servicemix.executors.impl.ExecutorConfig;
import org.apache.servicemix.executors.impl.ExecutorFactoryImpl;
import org.apache.servicemix.nmr.api.Channel;
import org.apache.servicemix.nmr.api.Endpoint;
import org.apache.servicemix.nmr.api.Exchange;
import org.apache.servicemix.nmr.api.event.ExchangeListener;
import org.apache.servicemix.nmr.api.service.ServiceHelper;
import org.apache.servicemix.nmr.core.InternalEndpointWrapper;
import org.apache.servicemix.nmr.core.ServiceMix;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractComponentTest extends CamelTestSupport implements ExchangeListener {

    private ServiceMix nmr;
    private ServiceMixComponent component;
    private Channel channel;

    @Override
    public void setUp() throws Exception {
        nmr = new ServiceMix();
        nmr.setExecutorFactory(createExecutorFactory());
        nmr.init();

        nmr.getListenerRegistry().register(this, ServiceHelper.createMap());

        component = new ServiceMixComponent();
        component.setNmr(nmr);

        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        for (ServiceMixProducer producer : findEndpoints(ServiceMixProducer.class)) {
            if (producer.getContinuations().size() > 0) {
                // let's wait for a moment to give the last exchanges the time to get Done
                Thread.sleep(500);
            }
            assertEquals("There should be no more pending Camel exchanges in the producer endpoints",
                         0, producer.getContinuations().size());
        }

        nmr.shutdown();
        super.tearDown();
    }

    private <E extends Endpoint> List<E> findEndpoints(Class<E> type) {
        List<E> result = new LinkedList<E>();

        for (Endpoint endpoint : nmr.getEndpointRegistry().getServices()) {
            if (endpoint instanceof InternalEndpointWrapper) {
                InternalEndpointWrapper wrapper = (InternalEndpointWrapper) endpoint;
                if (type.isAssignableFrom(wrapper.getEndpoint().getClass())) {
                    result.add(type.cast(wrapper.getEndpoint()));
                }
            }
        }
        return result;
    }

    /*
     * Create the ExecutorFactory for the unit test
     * based on the default configuration used in ServiceMix 4
     */
    protected ExecutorFactory createExecutorFactory() {
        ExecutorFactoryImpl factory = new ExecutorFactoryImpl();

        ExecutorConfig config = factory.getDefaultConfig();
        config.setCorePoolSize(1);
        config.setMaximumPoolSize(16);
        config.setQueueSize(0);
        config.setBypassIfSynchronous(true);

        return factory;
    };

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry registry = super.createRegistry();
        registry.bind("nmr", component);
        return registry;
    }

    /**
     * Get a client channel to access the NMR used for testing
     *
     * @return the client channel
     */
    protected Channel getChannel() {
        if (channel == null) {
            channel = nmr.createChannel();
        }

        return channel;
    }

    public void exchangeSent(Exchange exchange) {
        // graciously do nothing
    }

    public void exchangeDelivered(Exchange exchange) {
        // graciously do nothing
    }

    public void exchangeFailed(Exchange exchange) {
        // graciously do nothing
    }
}
