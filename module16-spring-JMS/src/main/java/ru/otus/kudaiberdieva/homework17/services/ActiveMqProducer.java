package ru.otus.kudaiberdieva.homework17.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ObjectMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ru.otus.kudaiberdieva.homework17.dto.StudentDto;
import ru.otus.kudaiberdieva.homework17.config.ActiveMqConfig;
import ru.otus.kudaiberdieva.homework17.dto.OrderDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class ActiveMqProducer {
    private static final Logger logger = LoggerFactory.getLogger(ActiveMqProducer.class);

    private static final int TEXT = 0;
    private static final int SERIALIZABLE = 1;
    private static final int MESSAGE_CREATOR = 2;

    private final JmsTemplate jmsTemplate;
    private final Random random = new Random();
    private final ObjectMapper objectMapper;

    public ActiveMqProducer(@Qualifier (ActiveMqConfig.JMS_TEMPLATE) JmsTemplate jmsTemplate,
                            ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedRate = 5000)
    public void convertAndSend() {
        String messageContent;
        switch (random.nextInt(3)) {
            case TEXT:
                messageContent = createTextMessage();
                jmsTemplate.convertAndSend(ActiveMqConfig.DESTINATION_NAME, messageContent);
                logger.info("Sent text message: {}", messageContent);
                break;
            case SERIALIZABLE:
                Serializable serializableMessage = createSerializableMessage();
                jmsTemplate.convertAndSend(ActiveMqConfig.DESTINATION_NAME, serializableMessage);
                logger.info("Sent serializable message: {}", serializableMessage);
                break;
            case MESSAGE_CREATOR:
                jmsTemplate.send(ActiveMqConfig.DESTINATION_NAME, createMessageCreatorMessage());
                logger.info("Sent message using MessageCreator");
                break;
            default:
                logger.info("ignore random");
        }
    }

    private String createTextMessage() {
        int id = random.nextInt(10);
        return "text-message:" + id;
    }

    private Serializable createSerializableMessage() {
        int id = random.nextInt(10);
        String firstName = "oleg_" + id;
        String lastName = "pavlov_" + id;

        return StudentDto.builder()
                         .firstName(firstName)
                         .lastName(lastName)
                         .email(firstName + '.' + lastName + "@yandex.ru")
                         .counts(List.of(1, 2, 3))
                         .dateOfBirth(LocalDate.now())
                         .build();
    }

    private MessageCreator createMessageCreatorMessage() {
        int id = random.nextInt(10);
        OrderDto order = OrderDto.builder()
                                 .title("auto_" + id)
                                 .value(id * id)
                                 .build();

        return session -> {
            try {
                ObjectMessage objectMessage = session.createObjectMessage();
                objectMessage.setStringProperty(ActiveMqConfig.CLASS_NAME, OrderDto.class.getName());
                objectMessage.setObject(objectMapper.writeValueAsString(order));
                return objectMessage;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

}
