package ru.adk.kafka.consumer.container;

import lombok.Builder;
import lombok.Getter;
import org.apache.kafka.streams.kstream.KStream;
import ru.adk.kafka.consumer.configuration.KafkaStreamConfiguration;

@Getter
@Builder(builderMethodName = "streamContainer", buildMethodName = "assemble")
public class KafkaStreamContainer<KeySerde, ValueSerde> {
    private final KafkaStreamConfiguration<KeySerde, ValueSerde> configuration;
    private final KStream<KeySerde, ValueSerde> stream;
}
