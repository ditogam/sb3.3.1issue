package org.example.testsb;

import java.io.Serializable;

import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authorization.AuthorizationEventPublisher;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.SpringAuthorizationEventPublisher;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.authorization.method.PreAuthorizeAuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.ROLE_INFRASTRUCTURE;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Service
    public static class PermissionEvaluatorTest {

        public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
            return false;
        }

        public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
            return false;
        }
    }

    @EnableWebSecurity
    @EnableMethodSecurity(
        securedEnabled = true,
        proxyTargetClass = true
    )
    @RequiredArgsConstructor
    @Configuration
    public static class SecurityConfig {

        @Configuration
        @Order(2)
        @RequiredArgsConstructor
        public static class OAuthSecurityConfig {
            private final PermissionEvaluatorTest permissionEvaluatorTest;

            @Bean
            public PermissionEvaluator permissionEvaluator() {
                return new PermissionEvaluator() {
                    @Override
                    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
                        return permissionEvaluatorTest.hasPermission(authentication, targetDomainObject, permission);
                    }

                    @Override
                    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
                        return permissionEvaluatorTest.hasPermission(authentication, targetId, targetType, permission);
                    }
                };
            }

            @Bean
            public AuthorizationEventPublisher authorizationEventPublisher(ApplicationEventPublisher publisher) {
                return new SpringAuthorizationEventPublisher(publisher);
            }

            @Bean
            public MethodSecurityExpressionHandler methodSecurityExpressionHandler(PermissionEvaluator permissionEvaluator) {
                DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
                handler.setPermissionEvaluator(permissionEvaluator);

                return handler;
            }

            @Bean
            public AuthorizationManager<MethodInvocation> authorizationManager(
                MethodSecurityExpressionHandler methodSecurityExpressionHandler) {
                PreAuthorizeAuthorizationManager preAuthorizeAuthorizationManager = new PreAuthorizeAuthorizationManager();
                preAuthorizeAuthorizationManager.setExpressionHandler(methodSecurityExpressionHandler);

                return (authentication, object) -> null;
            }

            @Bean
            @Role(ROLE_INFRASTRUCTURE)
            public Advisor authorizationManagerBeforeMethodInterception(AuthorizationManager<MethodInvocation> authorizationManager,
                                                                        AuthorizationEventPublisher publisher) {
                AuthorizationManagerBeforeMethodInterceptor authorizationManagerBeforeMethodInterceptor =
                    AuthorizationManagerBeforeMethodInterceptor.preAuthorize(authorizationManager);
                authorizationManagerBeforeMethodInterceptor.setAuthorizationEventPublisher(publisher);

                return authorizationManagerBeforeMethodInterceptor;
            }
        }
    }
}
