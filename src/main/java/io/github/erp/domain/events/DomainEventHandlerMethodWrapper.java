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

import java.lang.reflect.Method;

public class DomainEventHandlerMethodWrapper {

    private static final Logger log = LoggerFactory.getLogger(DomainEventHandlerMethodWrapper.class);

    private final Object target;
    private final Method method;
    private final String eventType;
    private final int order;

    public DomainEventHandlerMethodWrapper(Object target, Method method, String eventType, int order) {
        this.target = target;
        this.method = method;
        this.eventType = eventType;
        this.order = order;
        this.method.setAccessible(true);
    }

    public void handle(DomainEvent event) {
        try {
            log.debug("Invoking handler method: {} for event: {}", method.getName(), event.getEventId());
            method.invoke(target, event);
        } catch (Exception e) {
            log.error("Failed to invoke handler method: {} for event: {}", method.getName(), event.getEventId(), e);
            throw new RuntimeException("Handler method invocation failed", e);
        }
    }

    public String getEventType() {
        return eventType;
    }

    public int getOrder() {
        return order;
    }

    public String getMethodName() {
        return method.getName();
    }

    public Class<?> getTargetClass() {
        return target.getClass();
    }
}
