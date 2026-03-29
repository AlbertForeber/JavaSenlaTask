package com.senla;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.senla")
@PropertySource("classpath:application.properties")
@EnableScheduling
@EnableTransactionManagement
public class Application {
    public static void main(String[] args) {
        // Cоздаем контекст -> запускается сканирование
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Application.class);

        while (true) {}
    }
}
