package com.senla.app.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement // Для работы @Transactional
@PropertySource("classpath:application.properties") // Иначе конфигурация не работает
@ComponentScan(
        basePackages = "com.senla.app",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = RestController.class)
        }
)
public class RootConfiguration {

// PlatformTransactionManager - центральный интерфейс управления транзакциями
// абстрагирует различия между разными технологиями доступа к данным
    @Bean
    public PlatformTransactionManager transactionManager(SessionFactory factory) {
        return new HibernateTransactionManager(factory); // Управляет сессиями
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();

        factory.setConfigLocation(new ClassPathResource("hibernate.cfg.xml"));
        factory.setDataSource(dataSource);

        return factory;
    }
}
