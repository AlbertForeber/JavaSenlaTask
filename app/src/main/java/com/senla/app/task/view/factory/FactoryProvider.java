package com.senla.app.task.view.factory;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FactoryProvider {

    private final Map<String, MenuFactory> factories;

    public FactoryProvider(Map<String, MenuFactory> factories) {
        this.factories = factories;
    }

    public MenuFactory getFactory(String factoryName) {
        return factories.get(factoryName);
    }
}
