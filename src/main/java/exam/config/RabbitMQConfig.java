package exam.config;

import exam.util.StatementType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
@Slf4j
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Value("${rabbit.queue.address}")
    private String queueAddress;

    @Value("${rabbit.queue.bank-account}")
    private String queueBankAccount;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPort(port);
        connectionFactory.setHost(host);
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setExchange(exchange);
        return rabbitTemplate;
    }

    @Bean
    public Queue recoveryQueue() {
        return new Queue(queueAddress);
    }

    @Bean
    public Queue academicLeaveQueue() {
        return new Queue(queueBankAccount);
    }

//    @Bean
//    public DirectExchange directExchange(){
//        return new DirectExchange(exchange);
//    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("documents_exchange");
    }

    @Bean
    public Binding recoveryBinding(){
        return BindingBuilder.bind(recoveryQueue()).to(directExchange()).with(StatementType.RECOVERY.name());
    }

    @Bean
    public Binding academicLeaveBinding(){
        return BindingBuilder.bind(academicLeaveQueue()).to(directExchange()).with(StatementType.ACADEMIC_LEAVE.name());
    }
}
