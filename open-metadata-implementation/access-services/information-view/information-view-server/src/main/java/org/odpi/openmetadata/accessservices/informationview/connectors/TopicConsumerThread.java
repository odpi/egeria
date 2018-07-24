/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.connectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import kafka.utils.ShutdownableThread;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.InvalidEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public abstract class TopicConsumerThread<T> extends ShutdownableThread {

    private final AtomicBoolean shouldRun = new AtomicBoolean(false);
    private final Logger log = LoggerFactory.getLogger(TopicConsumerThread.class);
    private Consumer consumer;

    public TopicConsumerThread(String name, Consumer consumer) {
        super(name, false);
        this.consumer = consumer;
    }

    @Override
    public void doWork() {

        shouldRun.set(true);

        try {
            while (shouldRun.get()) {
                try {
                    List<T> messages = receive(consumer, Long.valueOf("14000"));//TODO timeout should be configurable

                    for (T event : messages) {
                        distributeEvents(event);
                    }
                } catch (Exception e) {
                    if (shouldRun.get()) {
                        log.error("Exception consuming event", e);
                    } else {
                        break;
                    }
                }
            }
        } finally {
            if (consumer != null) {
                log.info("closing consumer");

                consumer.close();
            }

        }
    }


    /**
     * @param consumer            A Kafka client that consumes records from a Kafka cluster.
     * @param timeoutMilliSeconds The time, in milliseconds, spent waiting in poll if data is not available in the buffer
     * @return
     */
    public List<T> receive(Consumer consumer, long timeoutMilliSeconds) throws InvalidEventException {

        List<T> messages = new ArrayList<>();
        ConsumerRecords<?, ?> records = consumer.poll(timeoutMilliSeconds);

        if (records != null) {
            for (ConsumerRecord<?, ?> record : records) {

                log.debug("Received Message topic ={}, partition ={}, offset = {}, key = {}, value = {}",
                        record.topic(), record.partition(), record.offset(), record.key(), record.value());

                ObjectMapper mapper = new ObjectMapper();
                T message;
                try {
                    message = mapper.readValue((String) record.value(), (Class<T>) getType());
                } catch (Exception e) {
                    InformationViewErrorCode errorCode = InformationViewErrorCode.INVALID_EVENT_FORMAT;
                    String errorMessage = errorCode.getErrorMessageId()
                            + errorCode.getFormattedErrorMessage("receiveEvent");

                    throw new InvalidEventException(this.getClass().getName(),
                            "receiveEvent",
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction(),
                            e);


                }

                if (message == null) {
                    continue;
                }

                messages.add(message);
            }
        }

        return messages;

    }

    public abstract void distributeEvents(T event) throws Exception;

    public abstract Class getType();

}
