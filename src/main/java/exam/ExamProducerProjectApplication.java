package exam;

import exam.config.RabbitMQConfig;
import exam.security.token.RefreshTokenFilter;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaRepositories
@Import(RabbitMQConfig.class)
public class ExamProducerProjectApplication {

    @Autowired
    private final RefreshTokenFilter refreshTokenFilter;


    public ExamProducerProjectApplication(RefreshTokenFilter refreshTokenFilter) {
        this.refreshTokenFilter = refreshTokenFilter;
    }

    @Bean
    FilterRegistrationBean<RefreshTokenFilter> registrationBean() {
        final FilterRegistrationBean<RefreshTokenFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(this.refreshTokenFilter);
        filterFilterRegistrationBean.addUrlPatterns("/refresh");
        return filterFilterRegistrationBean;
    }

    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> container(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPrefetchCount(10);
        factory.setConcurrentConsumers(5);
        return factory;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    public static void main(String[] args) {
        SpringApplication.run(ExamProducerProjectApplication.class, args);
    }

}
