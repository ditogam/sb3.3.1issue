///*
// * Developed by Remoto Pro
// * https://www.remotopro.io
// * Copyright (c) 2021-2024.
// */
//package org.example.testsb;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.DeserializationConfig;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
//import com.fasterxml.jackson.databind.introspect.Annotated;
//import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.http.converter.json.SpringHandlerInstantiator;
//
//@Configuration
//class ObjectMapperConfig {
//
//    @Bean
//    @Primary
//    public ObjectMapper objectMapper(HandlerInstantiator handlerInstantiator) {
//        ObjectMapper mapper = new ObjectMapper();
//        setDefaultOptions(handlerInstantiator, mapper);
//
//        return mapper;
//    }
//
//    @Bean(name = "auditableObjectMapper")
//    public ObjectMapper auditableObjectMapper(HandlerInstantiator handlerInstantiator) {
//        ObjectMapper mapper = new ObjectMapper();
//
//        setDefaultOptions(handlerInstantiator, mapper);
//
//        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        return mapper;
//    }
//
//    @Bean(name = "openSearchObjectMapper")
//    public ObjectMapper openSearchObjectMapper(HandlerInstantiator handlerInstantiator) {
//        ObjectMapper mapper = new ObjectMapper();
//
//        setDefaultOptions(handlerInstantiator, mapper);
//
//        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        return mapper;
//    }
//
//    private void setDefaultOptions(HandlerInstantiator handlerInstantiator, ObjectMapper mapper) {
//        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));
//        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
//        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME));
//        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));
//
//        mapper.registerModule(new Jdk8Module());
//        mapper.registerModule(javaTimeModule);
//
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
//        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
//        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
//        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, true);
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        mapper.setHandlerInstantiator(handlerInstantiator);
//    }
//
//    /**
//     * HandlerInstantiator is used for using existing Deserializer bean
//     */
//    @Bean
//    public HandlerInstantiator handlerInstantiator(ApplicationContext applicationContext) {
//        return new SpringHandlerInstantiator(applicationContext.getAutowireCapableBeanFactory()) {
//            @Override
//            public JsonDeserializer<?> deserializerInstance(DeserializationConfig config, Annotated annotated, Class<?> implClass) {
//                try {
//                    return (JsonDeserializer<?>) applicationContext.getBean(implClass);
//                } catch (Exception e) {
//                    return super.deserializerInstance(config, annotated, implClass);
//                }
//            }
//        };
//    }
//}
