/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.TimeoutException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BooleanSupplier;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Verify the send path of KafkaOpenMetadataEventProducer against Kafka's MockProducer: callers never block,
 * the buffer is bounded, events are handed to Kafka asynchronously and in order, and a retryable failure does
 * not lose the event.
 */
public class EventProducerTest
{
    private static final String TOPIC = "test.topic";

    /**
     * Producer whose Kafka client is the supplied mock.
     */
    private static class TestProducer extends KafkaOpenMetadataEventProducer
    {
        private final MockProducer<String, String> mockProducer;

        TestProducer(BlockingQueue<String> queue, MockProducer<String, String> mockProducer)
        {
            super(TOPIC, "server-id", new Properties(), queue, null);
            this.mockProducer = mockProducer;
        }

        @Override
        Producer<String, String> createProducer()
        {
            return mockProducer;
        }
    }


    private static MockProducer<String, String> newMockProducer(boolean autoComplete)
    {
        return new MockProducer<>(autoComplete, null, new StringSerializer(), new StringSerializer());
    }


    private static void waitFor(BooleanSupplier condition) throws InterruptedException
    {
        long deadline = System.currentTimeMillis() + 10000;
        while (!condition.getAsBoolean())
        {
            assertTrue(System.currentTimeMillis() < deadline, "Timed out waiting for condition");
            Thread.sleep(20);
        }
    }


    /**
     * A full buffer discards new events instead of growing or blocking.
     */
    @Test(timeOut = 5000)
    public void testFullBufferDiscards()
    {
        BlockingQueue<String> queue    = new LinkedBlockingQueue<>(3);
        TestProducer          producer = new TestProducer(queue, newMockProducer(true));

        for (int i = 0; i < 10; i++)
        {
            producer.sendEvent("event-" + i);
        }

        assertEquals(queue.size(), 3);
        assertEquals(producer.getDroppedEventCount(), 7L);
        assertEquals(queue.peek(), "event-0");
    }


    /**
     * Queued events are all handed to Kafka, in order, and counted when acknowledged.
     */
    @Test(timeOut = 20000)
    public void testEventsSentInOrder() throws Exception
    {
        final int                    eventCount   = 5000;
        BlockingQueue<String>        queue        = new LinkedBlockingQueue<>(eventCount);
        MockProducer<String, String> mockProducer = newMockProducer(true);
        TestProducer                 producer     = new TestProducer(queue, mockProducer);

        for (int i = 0; i < eventCount; i++)
        {
            producer.sendEvent("event-" + i);
        }

        Thread thread = new Thread(producer);
        thread.start();

        waitFor(() -> producer.getMessageSendCount() == eventCount);

        producer.safeCloseProducer();
        thread.join();

        List<ProducerRecord<String, String>> history = mockProducer.history();
        assertEquals(history.size(), eventCount);
        for (int i = 0; i < eventCount; i++)
        {
            assertEquals(history.get(i).value(), "event-" + i);
            assertEquals(history.get(i).topic(), TOPIC);
        }
        assertTrue(mockProducer.closed());
    }


    /**
     * A retryable error from send() keeps the event and retries it once Kafka recovers.
     */
    @Test(timeOut = 20000)
    public void testRetryableSendErrorKeepsEvent() throws Exception
    {
        BlockingQueue<String>        queue        = new LinkedBlockingQueue<>(10);
        MockProducer<String, String> mockProducer = newMockProducer(true);
        TestProducer                 producer     = new TestProducer(queue, mockProducer);

        mockProducer.sendException = new TimeoutException("metadata not available");

        producer.sendEvent("first");
        producer.sendEvent("second");

        Thread thread = new Thread(producer);
        thread.start();

        Thread.sleep(700);
        assertEquals(mockProducer.history().size(), 0);

        mockProducer.sendException = null;

        waitFor(() -> producer.getMessageSendCount() == 2);

        producer.safeCloseProducer();
        thread.join();

        assertEquals(mockProducer.history().get(0).value(), "first");
        assertEquals(mockProducer.history().get(1).value(), "second");
        assertEquals(producer.getMessageFailedSendCount(), 0L);
    }


    /**
     * A failure reported through the delivery callback is counted.
     */
    @Test(timeOut = 20000)
    public void testDeliveryFailureCounted() throws Exception
    {
        BlockingQueue<String>        queue        = new LinkedBlockingQueue<>(10);
        MockProducer<String, String> mockProducer = newMockProducer(false);
        TestProducer                 producer     = new TestProducer(queue, mockProducer);

        producer.sendEvent("good");
        producer.sendEvent("bad");

        Thread thread = new Thread(producer);
        thread.start();

        waitFor(() -> mockProducer.history().size() == 2);

        assertTrue(mockProducer.completeNext());
        assertTrue(mockProducer.errorNext(new KafkaException("broker rejected record")));

        waitFor(() -> producer.getMessageFailedSendCount() == 1);
        assertEquals(producer.getMessageSendCount(), 1L);

        producer.safeCloseProducer();
        thread.join();
    }
}
