/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.virtualiser.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.odpi.openmetadata.accessservices.informationview.events.TableContextEvent;
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

public class KafkaVirtualiserConsumer implements Runnable {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final AtomicBoolean shouldRun = new AtomicBoolean(false);
    private final Logger log = LoggerFactory.getLogger(KafkaVirtualiserConsumer.class);
    private Consumer consumer;
    private GaianQueryConstructor gaianQueryConstructor;
    private ViewsConstructor viewsConstructor;

    public KafkaVirtualiserConsumer(String name, Consumer consumer, GaianQueryConstructor gaianQueryConstructor, ViewsConstructor viewsConstructor) {
        this.consumer = consumer;
        this.gaianQueryConstructor = gaianQueryConstructor;
        this.viewsConstructor = viewsConstructor;
    }

    @Override
    public void run() {

        shouldRun.set(true);

        try {
            while (shouldRun.get()) {
                try {
                    List<TableContextEvent> messages = receive(consumer, 2000);//TODO timeout should be configurable

                    for (TableContextEvent event : messages) {
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

    public List<TableContextEvent> receive(Consumer consumer, long timeoutMilliSeconds) {

        List<TableContextEvent> messages = new ArrayList<>();
        ConsumerRecords<String, String> records = consumer.poll(timeoutMilliSeconds);
        TableContextEvent tableContextEvent;

        if (records != null) {
            for (ConsumerRecord<String, String> record : records) {

                log.debug("Received Message topic ={}, partition ={}, offset = {}, key = {}, value = {}",
                        record.topic(), record.partition(), record.offset(), record.key(), record.value());

                try {
                    tableContextEvent = MAPPER.readValue(record.value(), TableContextEvent.class);
                } catch (Exception e) {
                    log.info("Virtualizer can only handle valid TableContextEvent, ignoring message from kafka");
                    tableContextEvent = null;
                }

                if (isValid(tableContextEvent)) {
                    log.info("Event to process {}", tableContextEvent);
                    messages.add(tableContextEvent);
                }else{
                    log.info("Virtualizer can only handle valid TableContextEvent, ignoring message from kafka");
                }
            }
        }

        return messages;

    }

    private boolean isValid(TableContextEvent tableContextEvent) {
        //TODO once event beans are updated in IV OMAS, should use validation from bean definition instead of this method
        return tableContextEvent != null && tableContextEvent.getTableSource() != null && tableContextEvent.getTableColumns() != null;
    }


}