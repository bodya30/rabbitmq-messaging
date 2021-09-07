package ua.boderto.publisher;

import com.revinate.guava.util.concurrent.RateLimiter;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@ManagedResource(objectName = "rabbitMQ:name=headerPublisher")
@Component
public class HeaderPublisher implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = Logger.getLogger("HeaderPublisher");
    private static final int MESSAGES_PER_SECOND = 10;
    private static final String EXCHANGE = "simple.header.exchange";

    private static volatile boolean enablePublishing;
    private static volatile boolean turnOff;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        RateLimiter rateLimiter = RateLimiter.create(MESSAGES_PER_SECOND);
        long messageCounter = 1;

        while (!turnOff) {
            if (enablePublishing) {
                rateLimiter.acquire();
                sendMessage(messageCounter);
                messageCounter++;
            }
        }
    }

    private void sendMessage(long messageCounter) {
        Message message = MessageBuilder.withBody(String.format("Message #%d", messageCounter).getBytes())
                .setHeader("index", String.valueOf(messageCounter % 10))
                .build();
        rabbitTemplate.convertAndSend(EXCHANGE, "", message, msg -> {
            msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
            return msg;
        });
        LOGGER.info("SENT: " + message);
    }

    @ManagedAttribute
    public static boolean isEnablePublishing() {
        return enablePublishing;
    }

    @ManagedAttribute
    public static void setEnablePublishing(boolean enablePublishing) {
        HeaderPublisher.enablePublishing = enablePublishing;
    }

    @ManagedAttribute
    public static boolean isTurnOff() {
        return turnOff;
    }

    @ManagedAttribute
    public static void setTurnOff(boolean turnOff) {
        HeaderPublisher.turnOff = turnOff;
    }
}
