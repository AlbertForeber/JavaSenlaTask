package com.senla.app.task.utils;

import com.senla.annotation.db_qualifiers.Hibernate;
import jakarta.annotation.PreDestroy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

// Концептуально остался Singleton
@Component
@Hibernate
@DependsOn("liquibase")
public final class HibernateUtil {

    private static final Logger logger = LogManager.getLogger(HibernateUtil.class);

    private SessionFactory sessionFactory;
    private Session session = null;

    public HibernateUtil(
            @Value("${db.url}")        String url,
            @Value("${db.username}")   String username,
            @Value("${db.password}")   String password
    ) {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            configuration.setProperty("hibernate.connection.url", url);
            configuration.setProperty("hibernate.connection.username", username);
            configuration.setProperty("hibernate.connection.password", password);

            sessionFactory =  configuration.buildSessionFactory();
        } catch (Exception e) {
            logger.error("Ошибка Hibernate {}", e.getMessage());
            System.err.println("КРИТИЧЕСКАЯ ОШИБКА: Ошибка Hibernate (" + e.getMessage() + ")");
            System.exit(1);
        }
    }

    public Session getSession() throws HibernateException {
        if (session == null || !session.isOpen()) {
            System.out.println(Colors.BLUE + "СЕССИЯ ОТКРЫТА" + Colors.RESET);
            session = sessionFactory.openSession();
        }
        return session;
    }

    public void closeSession() {
        if (session != null && session.isOpen()) {
            try {
                System.out.println(Colors.BLUE + "СЕССИЯ ЗАКРЫТА" + Colors.RESET);
                session.close();
            } catch (HibernateException ignored) { }
        }
    }

    @PreDestroy
    public void shutdown() {
        sessionFactory.close();
        System.out.println(Colors.BLUE + "SESSION FACTORY ЗАКРЫТА" + Colors.RESET);
    }
}
