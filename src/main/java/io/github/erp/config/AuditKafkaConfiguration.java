package io.github.erp.config;

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

import io.github.erp.domain.events.audit.AuditTrailEvent;
import io.github.erp.domain.events.audit.ComplianceAuditEvent;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRetry
@EnableAsync
public class AuditKafkaConfiguration {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Value("${spring.kafka.topics.audit-events.business-events.name:erp.audit.prod.business-events.v1}")
    private String businessEventsTopicName;

    @Value("${spring.kafka.topics.audit-events.compliance-events.name:erp.audit.prod.compliance-events.v1}")
    private String complianceEventsTopicName;

    @Value("${spring.kafka.topics.audit-events.security-events.name:erp.audit.prod.security-events.v1}")
    private String securityEventsTopicName;

    @Value("${spring.kafka.topics.audit-events.system-events.name:erp.audit.prod.system-events.v1}")
    private String systemEventsTopicName;

    @Value("${spring.kafka.topics.audit-events.business-events.partitions:12}")
    private int businessEventsPartitions;

    @Value("${spring.kafka.topics.audit-events.compliance-events.partitions:6}")
    private int complianceEventsPartitions;

    @Value("${spring.kafka.topics.audit-events.security-events.partitions:6}")
    private int securityEventsPartitions;

    @Value("${spring.kafka.topics.audit-events.system-events.partitions:4}")
    private int systemEventsPartitions;

    @Value("${spring.kafka.topics.audit-events.replication-factor:3}")
    private short replicationFactor;

    @Bean
    public KafkaAdmin auditKafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootStrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic businessEventsAuditTopic() {
        return new NewTopic(businessEventsTopicName, businessEventsPartitions, replicationFactor)
            .configs(Map.of(
                "retention.ms", "2592000000",
                "cleanup.policy", "delete",
                "compression.type", "lz4",
                "min.insync.replicas", "2"
            ));
    }

    @Bean
    public NewTopic complianceEventsAuditTopic() {
        return new NewTopic(complianceEventsTopicName, complianceEventsPartitions, replicationFactor)
            .configs(Map.of(
                "retention.ms", "31536000000",
                "cleanup.policy", "delete",
                "compression.type", "lz4",
                "min.insync.replicas", "2"
            ));
    }

    @Bean
    public NewTopic securityEventsAuditTopic() {
        return new NewTopic(securityEventsTopicName, securityEventsPartitions, replicationFactor)
            .configs(Map.of(
                "retention.ms", "7776000000",
                "cleanup.policy", "delete",
                "compression.type", "lz4",
                "min.insync.replicas", "2"
            ));
    }

    @Bean
    public NewTopic systemEventsAuditTopic() {
        return new NewTopic(systemEventsTopicName, systemEventsPartitions, replicationFactor)
            .configs(Map.of(
                "retention.ms", "604800000",
                "cleanup.policy", "delete",
                "compression.type", "lz4",
                "min.insync.replicas", "2"
            ));
    }

    @Bean
    public ProducerFactory<String, AuditTrailEvent> auditTrailEventProducerFactory() {
        Map<String, Object> configProps = new HashMap<>(kafkaProperties.getProducerProps());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, AuditTrailEvent> auditTrailEventKafkaTemplate() {
        return new KafkaTemplate<>(auditTrailEventProducerFactory());
    }

    @Bean
    public ProducerFactory<String, ComplianceAuditEvent> complianceAuditEventProducerFactory() {
        Map<String, Object> configProps = new HashMap<>(kafkaProperties.getProducerProps());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, ComplianceAuditEvent> complianceAuditEventKafkaTemplate() {
        return new KafkaTemplate<>(complianceAuditEventProducerFactory());
    }

    @Bean
    public ConsumerFactory<String, AuditTrailEvent> auditTrailEventConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>(kafkaProperties.getConsumerProps());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "erp-system-audit-trail");
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        configProps.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024);
        configProps.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "io.github.erp.domain.events.audit");
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AuditTrailEvent> auditTrailEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AuditTrailEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(auditTrailEventConsumerFactory());
        factory.setConcurrency(6);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ComplianceAuditEvent> complianceAuditEventConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>(kafkaProperties.getConsumerProps());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "erp-system-compliance-monitoring");
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        configProps.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024);
        configProps.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "io.github.erp.domain.events.audit");
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ComplianceAuditEvent> complianceAuditEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ComplianceAuditEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(complianceAuditEventConsumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    public String getBusinessEventsTopicName() {
        return businessEventsTopicName;
    }

    public String getComplianceEventsTopicName() {
        return complianceEventsTopicName;
    }

    public String getSecurityEventsTopicName() {
        return securityEventsTopicName;
    }

    public String getSystemEventsTopicName() {
        return systemEventsTopicName;
    }
}
