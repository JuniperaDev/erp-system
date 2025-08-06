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

import java.lang.reflect.Method;

public class DomainEventHandlerMethod {

    private final Object target;
    private final Method method;
    private final String eventType;

    public DomainEventHandlerMethod(Object target, Method method, String eventType) {
        this.target = target;
        this.method = method;
        this.eventType = eventType;
    }

    public void handle(DomainEvent event) throws Exception {
        method.invoke(target, event);
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public String getEventType() {
        return eventType;
    }
}
