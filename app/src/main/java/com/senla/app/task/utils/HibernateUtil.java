package com.senla.app.task.utils;

import com.senla.annotation.ConfigProperty;
import com.senla.annotation_processor.ConfigProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {

    private static final Logger logger = LogManager.getLogger(HibernateUtil.class);
    private static HibernateUtil INSTANCE = null;

    @ConfigProperty(propertyName = "db_url")
    private String url = "";

    @ConfigProperty(propertyName = "db_username")
    private String username = "";

    @ConfigProperty(propertyName = "db_password")
    private String password = "";

    private SessionFactory sessionFactory;
    private static Session session = null;

    HibernateUtil() { }

    private static HibernateUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HibernateUtil();
            ConfigProcessor.applyConfig(INSTANCE);

            try {
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");

                configuration.setProperty("hibernate.connection.url", INSTANCE.url);
                configuration.setProperty("hibernate.connection.username", INSTANCE.username);
                configuration.setProperty("hibernate.connection.password", INSTANCE.password);

                INSTANCE.sessionFactory =  configuration.buildSessionFactory();
            } catch (Exception e) {
                logger.error("Ошибка Hibernate {}", e.getMessage());
                System.err.println("КРИТИЧЕСКАЯ ОШИБКА: Ошибка Hibernate " + e.getMessage());
            }
        }
        return INSTANCE;
    }

    public static Session getSession() throws HibernateException {
        if (session == null || !session.isOpen()) {
            System.out.println(Colors.BLUE + "СЕССИЯ ОТКРЫТА" + Colors.RESET);
            session = getInstance().sessionFactory.openSession();
        }
        return session;
    }

    public static void closeSession() {
        if (session != null && session.isOpen()) {
            try {
                System.out.println(Colors.BLUE + "СЕССИЯ ЗАКРЫТА" + Colors.RESET);
                session.close();
            } catch (HibernateException ignored) { }
        }
    }

    public static void shutdown() {
        if (INSTANCE != null) {
            getInstance().sessionFactory.close();
        }
    }
}
