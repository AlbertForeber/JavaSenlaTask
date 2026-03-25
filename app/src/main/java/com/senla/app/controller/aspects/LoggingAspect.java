package com.senla.app.controller.aspects;

import com.senla.annotation.LoggingOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

// Аспект для красивого логгирования начала/окончания/ошибки операции
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    @Around("@annotation(loggingOperation)")
    public Object loggingExecution(ProceedingJoinPoint joinPoint, LoggingOperation loggingOperation) throws Throwable {
        String operationName = loggingOperation.value();

        logger.info("Старт операции: '{}'", operationName);

        try {
            Object result = joinPoint.proceed();
            logger.info("Операция '{}' завершена", operationName);

            return result;
        } catch (Throwable e) {
            logger.error("Ошибка операции '{}': {}", operationName, e.getMessage());
            throw e; // Пробрасываем дальше, чтобы обработать
        }
    }
}
