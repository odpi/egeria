package org.apache.atlas.virtualiser;


import org.apache.atlas.virtualiser.ffdc.VirtualiserCheckedException;
import org.apache.atlas.virtualiser.kafka.KafkaVirtualiserConsumer;
import org.apache.atlas.virtualiser.util.PropertiesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Virtualizer {

    public static void main(String[] args) {
        PropertiesHelper.loadProperties();
        KafkaVirtualiser kafkaVirtualiser = new KafkaVirtualiser();
        kafkaVirtualiser.listenToIVOMAS();
    }
}
