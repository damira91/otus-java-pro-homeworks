package ru.otus.kudaiberdieva.homework17.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;


@Configuration
public class ActiveMqConfig {

    public static final String JMS_TEMPLATE = "activeMqJmsTemplate";
    public static final String JMS_LISTENER_CONTAINER_FACTORY = "activeMqJmsListenerContainerFactory";

    private static final String CONNECTION_FACTORY = "activeMqConnectionFactory";

    public static final String DESTINATION_NAME = "foo";
    public static final String CLASS_NAME = "className";

    @Bean(CONNECTION_FACTORY)
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("tcp://localhost:61616");
        connectionFactory.setUserName("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setTrustAllPackages(true);
        return connectionFactory;
    }

    @Bean(JMS_TEMPLATE)
    public JmsTemplate jmsTemplate(@Qualifier(CONNECTION_FACTORY) ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setReceiveTimeout(10000);
        return jmsTemplate;
    }

    @Bean(JMS_LISTENER_CONTAINER_FACTORY)
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
            @Qualifier(CONNECTION_FACTORY) ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("1-1");
        factory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
        return factory;
    }

    @Bean
    public ObjectMapper objectMapper() {

        return new ObjectMapper();
    }
}
