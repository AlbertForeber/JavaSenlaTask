package com.senla.app.config;

import com.senla.app.config.converters.StringToEnumConverterFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("com.senla.app.controller")
@EnableAspectJAutoProxy // Включаем поддержку @Aspect
public class WebConfiguration implements WebMvcConfigurer {

    // Регистрация фабрики конвертеров
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToEnumConverterFactory());
    }
}
