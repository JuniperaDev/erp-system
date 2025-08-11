package io.github.erp.domain.events;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class DomainEventHandlerScanner implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(DomainEventHandlerScanner.class);

    private final DomainEventProcessor domainEventProcessor;

    public DomainEventHandlerScanner(DomainEventProcessor domainEventProcessor) {
        this.domainEventProcessor = domainEventProcessor;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        
        if (beanClass.isAnnotationPresent(DomainEventHandler.class)) {
            Method[] methods = beanClass.getDeclaredMethods();
            
            for (Method method : methods) {
                if (method.isAnnotationPresent(DomainEventHandlerMethodAnnotation.class)) {
                    DomainEventHandlerMethodAnnotation annotation = method.getAnnotation(DomainEventHandlerMethodAnnotation.class);
                    String eventType = annotation.eventType();
                    
                    log.info("Registering domain event handler: {} for event type: {}", 
                            method.getName(), eventType);
                    
                    DomainEventHandlerMethodWrapper wrapper = new DomainEventHandlerMethodWrapper(
                        bean, method, eventType, annotation.order());
                    
                    domainEventProcessor.registerHandler(eventType, wrapper);
                }
            }
        }
        
        return bean;
    }
}
