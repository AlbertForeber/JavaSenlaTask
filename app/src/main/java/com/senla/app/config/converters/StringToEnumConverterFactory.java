package com.senla.app.config.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

// Фабрика, чтобы в RequestParams можно было писать lower-case
public class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter<>(targetType);
    }

    private static class StringToEnumConverter<T extends Enum> implements Converter<String, T> {
        private final Class<T> enumType;

        protected StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T convert(String source) {
            if (source.isBlank()) return null;
            return (T) Enum.valueOf(this.enumType, source.trim().toUpperCase());
        }
    }
}
