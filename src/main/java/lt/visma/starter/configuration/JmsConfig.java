package lt.visma.starter.configuration;

import lt.visma.starter.repository.PaymentQueueEntryRepository;
import lt.visma.starter.service.impl.JmsErrorHandlerService;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.Destination;

@Configuration
@EnableJms
public class JmsConfig {
    @Value("${activemq.broker-url}")
    private String brokerUrl;

    @Value("${jms.paymentQueueDestination}")
    private String paymentQueueDestination;

    @Value("${jms.transactionQueueDestination}")
    private String transactionQueueDestination;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setTrustAllPackages(true);

        return connectionFactory;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory());
        cachingConnectionFactory.setSessionCacheSize(10);

        return cachingConnectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(cachingConnectionFactory());

        return template;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("@type");
        return converter;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(PaymentQueueEntryRepository paymentQueueEntryRepository) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(cachingConnectionFactory());
        factory.setErrorHandler(new JmsErrorHandlerService(paymentQueueEntryRepository));

        return factory;
    }

    @Bean
    public Destination paymentQueueDestination() {
        return new ActiveMQQueue(paymentQueueDestination);
    }

    @Bean
    public Destination transactionQueueDestination() {
        return new ActiveMQQueue(transactionQueueDestination);
    }
}
