package com.senla;

import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import ch.qos.logback.classic.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.senla")
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@EnableKafka
public class Application {

    public static void main(String[] args) {
        // Для изменения уровня лоширования
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Application.class);
    }
}
