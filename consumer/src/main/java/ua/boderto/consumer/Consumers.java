package ua.boderto.consumer;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

import static ua.boderto.consumer.ConsumerApplication.MIRRORED_QUEUE;

@Component
public class Consumers {

    private static final Logger LOGGER = Logger.getLogger("Consumers");
    private static final String SIMPLE_QUEUE = "simple.queue";
    private static final String HEADER_EXCHANGE = "simple.header.exchange";

    // Consumer uses durable mirrored queue with persisted messages and direct exchange
    @RabbitListener(queues = MIRRORED_QUEUE)
    public void onMessage(String messageBody) {
        LOGGER.info("RECEIVED: " + messageBody);
    }

    // Consumer uses durable simple queue with non-persisted messages and header exchange
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = SIMPLE_QUEUE, durable = "true"),
            exchange = @Exchange(value = HEADER_EXCHANGE, type = ExchangeTypes.HEADERS),
            arguments = {@Argument(name = "x-match", value = "any"),
                    @Argument(name = "index", value = "4")})
    )
    public void handleMessage(Message message) {
        LOGGER.info("RECEIVED: " + message);
    }
}
