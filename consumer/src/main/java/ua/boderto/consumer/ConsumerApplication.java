package ua.boderto.consumer;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableRabbit
public class ConsumerApplication {

    public static final String MIRRORED_QUEUE = "example.queue";

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    public Queue exampleQueue() {
        return new Queue(MIRRORED_QUEUE, true);
    }
}
