/* SPDX-License-Identifier: Apache-2.0 */
package org.apache.atlas.virtualiser.kafka;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kafka.utils.ShutdownableThread;
import org.apache.atlas.omas.informationview.events.ColumnContextEvent;
import org.apache.atlas.virtualiser.ffdc.VirtualiserCheckedException;
import org.apache.atlas.virtualiser.ffdc.VirtualiserErrorCode;
import org.apache.atlas.virtualiser.gaian.GaianQueryConstructor;
import org.apache.atlas.virtualiser.views.ViewsConstructor;
import org.apache.kafka.clients.consumer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * KafkaVirtualiserConsumer is used to listen to Information View OMAS Out topic
 * and it will parse the json file sent out by Information View OMAS and ship the json
 * to ViewsConstructor and GaianQueryConstructor.
 *
 */

public class KafkaVirtualiserConsumer extends ShutdownableThread{

    private final AtomicBoolean shouldRun = new AtomicBoolean(false);
    private final Logger log = LoggerFactory.getLogger(KafkaVirtualiserConsumer.class);
    private Consumer consumer;
    GaianQueryConstructor gaianQueryConstructor;
    ViewsConstructor viewsConstructor;

    public KafkaVirtualiserConsumer(String name, Consumer consumer,GaianQueryConstructor gaianQueryConstructor, ViewsConstructor viewsConstructor){
        super(name, false);
        this.consumer = consumer;
        this.gaianQueryConstructor = gaianQueryConstructor;
        this.viewsConstructor = viewsConstructor;
    }

    @Override
    public void doWork() {

        final String methodName="doWork";
        shouldRun.set(true);

        try {
            while (shouldRun.get()) {
                try {
                    List<ColumnContextEvent> messages = receive(consumer, 20000);//TODO timeout should be configurable

                    for (ColumnContextEvent event : messages) {
                        gaianQueryConstructor.notifyGaian(event);
                        //create business view and technical view and notify Information View
                        viewsConstructor.notifyIVOMAS(event);
                    }
                } catch (JsonParseException e) {
                /*
                 * Wrap exception in the DatabaseConnectCheckedException with a suitable message
                 * when events is not able to parse the json.
                */
                VirtualiserErrorCode errorCode = VirtualiserErrorCode.JACKSON_PARSE_FAIL;

                String        errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(methodName);
                log.error(errorMessage);

            } catch (JsonMappingException e) {
                /*
                 * Wrap exception in the DatabaseConnectCheckedException with a suitable message
                 * when events is not able to map the json with the object.
                */
                VirtualiserErrorCode errorCode = VirtualiserErrorCode.JACKSON_MAPPING_FAIL;

                String        errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(methodName);
                log.error(errorMessage);

            } catch (IOException e) {
                /*
                 * Wrap exception in the DatabaseConnectCheckedException with a suitable message
                 * when there is interrputed I/O operations.
                */
                VirtualiserErrorCode errorCode = VirtualiserErrorCode.INTERRUPTED_IO_EXCEPTION;

                String        errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(methodName);
                log.error(errorMessage);

            } catch (VirtualiserCheckedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            if (consumer != null) {
                log.info("closing consumer");
                consumer.close();
            }

        }
    }

    public List<ColumnContextEvent> receive(Consumer consumer, long timeoutMilliSeconds) throws IOException {

        List<ColumnContextEvent> messages = new ArrayList<>();
        ConsumerRecords<Long, String> records = consumer.poll(timeoutMilliSeconds);
        ColumnContextEvent columnContextEvent=null;

        if (records != null) {
            for (ConsumerRecord<Long, String> record : records) {

                log.debug("Received Message topic ={}, partition ={}, offset = {}, key = {}, value = {}",
                        record.topic(), record.partition(), record.offset(), record.key(), record.value());

                ObjectMapper mapper = new ObjectMapper();
                System.out.println(record.value());
                columnContextEvent=mapper.readValue(record.value(), ColumnContextEvent.class);

                messages.add(columnContextEvent);

                if (columnContextEvent == null) {
                    continue;
                }

                messages.add(columnContextEvent);
            }
        }

        return messages;

    }



}