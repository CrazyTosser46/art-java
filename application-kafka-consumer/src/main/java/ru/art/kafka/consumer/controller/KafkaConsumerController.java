/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.kafka.consumer.controller;

import lombok.experimental.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.*;
import ru.art.kafka.consumer.configuration.*;
import ru.art.kafka.consumer.exception.*;
import ru.art.kafka.consumer.model.*;
import ru.art.kafka.consumer.specification.*;
import static java.lang.String.*;
import static java.util.Objects.isNull;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.kafka.consumer.constants.KafkaConsumerModuleConstants.*;
import static ru.art.kafka.consumer.module.KafkaConsumerModule.*;
import static ru.art.service.ServiceController.*;
import java.util.*;
import java.util.concurrent.*;

@UtilityClass
public class KafkaConsumerController {
    private static volatile ExecutorService POOL;

    public static void startKafkaConsumer(String serviceId) {
        KafkaConsumerModuleConfiguration moduleConfiguration = kafkaConsumerModule();
        if (isEmpty(moduleConfiguration.getKafkaConsumerConfiguration())) {
            return;
        }
        submitKafkaConsumer(moduleConfiguration.getKafkaConsumerConfiguration(), serviceId);
    }

    public static void stopKafkaConsumer(String serviceId) {
        ManagedKafkaConsumer managedKafkaConsumer = kafkaConsumerModuleState().getKafkaConsumers().get(serviceId);
        if (isNull(managedKafkaConsumer)) {
            return;
        }
        KafkaConsumer<?, ?> consumer = managedKafkaConsumer.getConsumer();
        consumer.close();
        kafkaConsumerModuleState().getKafkaConsumers().put(serviceId, ManagedKafkaConsumer.builder()
                .consumer(consumer)
                .stopped(true)
                .build());
    }

    public static void restartKafkaConsumer(String serviceId) {
        stopKafkaConsumer(serviceId);
        startKafkaConsumer(serviceId);
    }

    private static void submitKafkaConsumer(KafkaConsumerConfiguration configuration, String serviceId) {
        if (isNull(POOL)) {
            POOL = new ForkJoinPool(configuration.getExecutorPoolSize());
        }
        POOL.submit(() -> startKafkaConsumer(configuration, serviceId));
    }

    private static void startKafkaConsumer(KafkaConsumerConfiguration configuration, String serviceId) {
        List<KafkaConsumerServiceSpecification> kafkaConsumerServiceSpecifications = kafkaConsumerServices();
        Deserializer<?> keyDeserializer = configuration.getKeyDeserializer();
        Deserializer<?> valueDeserializer = configuration.getValueDeserializer();
        KafkaConsumer<?, ?> consumer = new KafkaConsumer<>(createProperties(configuration), keyDeserializer, valueDeserializer);
        consumer.subscribe(configuration.getTopics());
        kafkaConsumerModuleState().getKafkaConsumers().put(serviceId, ManagedKafkaConsumer.builder().consumer(consumer).build());
        try {
            while (!kafkaConsumerModuleState().getKafkaConsumers().get(serviceId).isStopped()) {
                ConsumerRecords<?, ?> poll = consumer.poll(configuration.getPollTimeout());
                for (ConsumerRecord<?, ?> record : poll) {
                    kafkaConsumerServiceSpecifications.stream()
                            .filter(specification -> specification.getServiceId().equals(serviceId))
                            .forEach(specification -> executeServiceMethod(serviceId, record.topic(), record));
                }
            }
        } finally {
            consumer.close();
        }
    }

    private static Properties createProperties(KafkaConsumerConfiguration configuration) {
        if (isEmpty(configuration.getClientId())) {
            throw new KafkaConsumerModuleException(CLIENT_ID_IS_EMPTY);
        }
        if (isEmpty(configuration.getGroupId())) {
            throw new KafkaConsumerModuleException(GROUP_ID_IS_EMPTY);
        }
        if (isEmpty(configuration.getBrokers())) {
            throw new KafkaConsumerModuleException(BROKERS_ARE_EMPTY);
        }
        Properties properties = new Properties();
        properties.put(BOOTSTRAP_SERVERS_CONFIG, join(COMMA, configuration.getBrokers()));
        properties.put(GROUP_ID_CONFIG, configuration.getGroupId());
        properties.put(CLIENT_ID_CONFIG, configuration.getClientId());
        properties.putAll(configuration.getAdditionalProperties());
        return properties;
    }
}
