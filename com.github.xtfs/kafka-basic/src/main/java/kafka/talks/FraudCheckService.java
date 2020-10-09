package kafka.talks;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

public class FraudCheckService {

    public static void main(String[] args) throws InterruptedException {
        Logger logger = LoggerFactory.getLogger(FraudCheckService.class.getName());

        Consumer consumerConfig = new Consumer("new_order_received", "fraud_check_group");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig.getProperties());

        consumer.subscribe(Collections.singletonList(consumerConfig.topic));

        Producer producer = new Producer();

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, String> record : records) {
                logger.info("Pedido [" + record.value() + "] verificado com sucesso");
                producer.produce("order_verified", record.value());
            }
        }
    }
}
