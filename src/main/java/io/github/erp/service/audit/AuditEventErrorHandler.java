package io.github.erp.service.audit;

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

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class AuditEventErrorHandler implements ConsumerAwareListenerErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(AuditEventErrorHandler.class);

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
        log.error("Error in audit event consumer: {}", exception.getMessage(), exception);
        
        if (message.getPayload() instanceof ConsumerRecord) {
            ConsumerRecord<?, ?> record = (ConsumerRecord<?, ?>) message.getPayload();
            log.error("Failed to process record from topic: {} partition: {} offset: {}", 
                    record.topic(), record.partition(), record.offset());
            
            if (shouldRetry(exception)) {
                log.info("Retrying failed audit event processing");
                throw exception;
            } else {
                log.warn("Sending audit event to dead letter queue");
                sendToDeadLetterQueue(record);
            }
        }
        
        return null;
    }

    private boolean shouldRetry(Exception exception) {
        return exception.getCause() instanceof org.springframework.dao.TransientDataAccessException ||
               exception.getCause() instanceof java.net.ConnectException;
    }

    private void sendToDeadLetterQueue(ConsumerRecord<?, ?> record) {
        log.info("Sending record to dead letter queue: topic={} partition={} offset={}", 
                record.topic(), record.partition(), record.offset());
    }
}
