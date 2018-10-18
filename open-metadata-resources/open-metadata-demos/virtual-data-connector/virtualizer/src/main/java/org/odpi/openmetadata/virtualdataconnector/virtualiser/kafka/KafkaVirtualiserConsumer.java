/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.virtualiser.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import kafka.utils.ShutdownableThread;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.odpi.openmetadata.accessservices.informationview.events.ColumnContextEvent;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.gaian.GaianQueryConstructor;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.views.ViewsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * KafkaVirtualiserConsumer is used to listen to Information View OMAS Out topic
 * and it will parse the json file sent out by Information View OMAS and ship the json
 * to ViewsConstructor and GaianQueryConstructor.
 */

public class KafkaVirtualiserConsumer extends ShutdownableThread {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final AtomicBoolean shouldRun = new AtomicBoolean(false);
    private final Logger log = LoggerFactory.getLogger(KafkaVirtualiserConsumer.class);
    private Consumer consumer;
    private GaianQueryConstructor gaianQueryConstructor;
    private ViewsConstructor viewsConstructor;

    public KafkaVirtualiserConsumer(String name, Consumer consumer, GaianQueryConstructor gaianQueryConstructor, ViewsConstructor viewsConstructor) {
        super(name, false);
        this.consumer = consumer;
        this.gaianQueryConstructor = gaianQueryConstructor;
        this.viewsConstructor = viewsConstructor;
    }

    @Override
    public void doWork() {

        final String methodName = "doWork";
        shouldRun.set(true);

        try {
            while (shouldRun.get()) {
                try {
                    List<ColumnContextEvent> messages = receive(consumer, 2000);//TODO timeout should be configurable

                    for (ColumnContextEvent event : messages) {
                        Map<String, String> views = gaianQueryConstructor.notifyGaian(event);
                        viewsConstructor.notifyIVOMAS(event, views);
                    }
                } catch (Exception e) {
                    log.error("Exception processing received event.", e);
                }
            }
        } finally {
            if (consumer != null) {
                log.info("closing consumer");
                consumer.close();
            }

        }
    }

    public List<ColumnContextEvent> receive(Consumer consumer, long timeoutMilliSeconds) {

        List<ColumnContextEvent> messages = new ArrayList<>();
        ConsumerRecords<Long, String> records = consumer.poll(timeoutMilliSeconds);
        ColumnContextEvent columnContextEvent;

        if (records != null) {
            for (ConsumerRecord<Long, String> record : records) {

                log.debug("Received Message topic ={}, partition ={}, offset = {}, key = {}, value = {}",
                        record.topic(), record.partition(), record.offset(), record.key(), record.value());

                try {
                    columnContextEvent = MAPPER.readValue(record.value(), ColumnContextEvent.class);
                } catch (Exception e) {
                    log.info("Virtualizer can only handle valid ColumnContextEvent, ignoring message from kafka");
                    columnContextEvent = null;
                }

                if (isValid(columnContextEvent)) {
                    log.info("Event to process {}", columnContextEvent);
                    messages.add(columnContextEvent);
                }else{
                    log.info("Virtualizer can only handle valid ColumnContextEvent, ignoring message from kafka");
                }
            }
        }

        return messages;

    }

    private boolean isValid(ColumnContextEvent columnContextEvent) {
        //TODO once event beans are updated in IV OMAS, should use validation from bean definition instead of this method
        return columnContextEvent != null && columnContextEvent.getConnectionDetails() != null && columnContextEvent.getTableContext() != null && columnContextEvent.getTableColumns() != null;
    }


}