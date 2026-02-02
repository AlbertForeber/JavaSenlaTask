package com.senla.app.task.db;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfiguration {

    // Для автоматического срабатывания при новых миграциях
    // Подгружается Spring-ом
    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
         SpringLiquibase liquibase = new SpringLiquibase();

         // Источник данных
        liquibase.setDataSource(dataSource);

        // Указываем путь к главному changelog
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.yaml");

        return liquibase;
    }
}
