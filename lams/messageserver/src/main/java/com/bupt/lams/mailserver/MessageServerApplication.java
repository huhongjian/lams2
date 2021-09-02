package com.bupt.lams.mailserver;

import com.bupt.lams.model.MessageConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MessageServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageServerApplication.class, args);
    }

    @Bean
    Queue queue() {
        return new Queue(MessageConstants.MAIL_QUEUE_NAME);
    }
}
