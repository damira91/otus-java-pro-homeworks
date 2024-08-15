package ru.otus.kudaiberdieva.homework17.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import ru.otus.kudaiberdieva.homework17.config.ActiveMqConfig;

import java.io.Serializable;

@Service
@RequiredArgsConstructor
public class ActiveMqListener {
    private static final Logger logger = LoggerFactory.getLogger(ActiveMqListener.class);

    private final ObjectMapper objectMapper;

    @JmsListener(destination = ActiveMqConfig.DESTINATION_NAME
            , containerFactory = ActiveMqConfig.JMS_LISTENER_CONTAINER_FACTORY)
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage)
                onTextMessage((TextMessage) message);
            else if (message instanceof ObjectMessage)
                onObjectMessage((ObjectMessage) message);
            else
                throw new IllegalArgumentException("Message Error");
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void onTextMessage(TextMessage message) throws JMSException {
        String msg = message.getText();
        System.out.format("[rabbit-mq: text] : %s\n", msg);
    }

    private void onObjectMessage(ObjectMessage message)
            throws JMSException, ClassNotFoundException, JsonProcessingException {
        String className = message.getStringProperty(ActiveMqConfig.CLASS_NAME);

        if (className == null)
            onSerializableObjectMessage(message);
        else
            onCustomObjectMessage(Class.forName(className), message);
    }

    private static void onSerializableObjectMessage(ObjectMessage message) throws JMSException {
        Serializable obj = message.getObject();
        System.out.format("[rabbit-mq: serializable] : %s\n", obj);
    }

    private void onCustomObjectMessage(Class<?> cls, ObjectMessage message)
            throws JMSException, JsonProcessingException {
        String json = String.valueOf(message.getObject());
        Object obj = objectMapper.readValue(json, cls);
        logger.info("[rabbit-mq: %s] : %s\n", cls.getSimpleName(), obj);
    }
}
