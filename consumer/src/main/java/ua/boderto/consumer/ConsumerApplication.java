package ua.boderto.consumer;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.logging.Logger;

@SpringBootApplication
@EnableRabbit
public class ConsumerApplication {

    private static final Logger LOGGER = Logger.getLogger("ConsumerApplication");
    private static final String QUEUE = "example.queue";

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @RabbitListener(queues = QUEUE)
    public void onMessage(String message) {
        LOGGER.info("RECEIVED: " + message);
    }

    @Bean
    public Queue exampleQueue() {
        return new Queue(QUEUE, true);
    }
}
