/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.NotLeaderOrFollowerException;
import org.apache.kafka.common.errors.TimeoutException;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutionException;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Verify that KafkaOpenMetadataEventProducer walks the cause chain of a send failure correctly.  The previous
 * implementation re-wrapped the same cause on each iteration, so a chain with no retryable exception looped
 * for ever - pinning a CPU and allocating an exception per iteration until the platform ran out of heap.
 */
public class RetryableExceptionTest
{
    /**
     * A send failure whose cause is not retryable returns false - and returns at all.
     */
    @Test(timeOut = 5000)
    public void testNonRetryableCauseReturns()
    {
        Exception error = new ExecutionException(new KafkaException("not retryable"));

        assertFalse(KafkaOpenMetadataEventProducer.isExceptionRetryable(error));
    }


    /**
     * A deeper chain with no retryable exception anywhere also returns false.
     */
    @Test(timeOut = 5000)
    public void testNonRetryableDeepChainReturns()
    {
        Exception error = new ExecutionException(
                new KafkaException("outer", new IllegalStateException("middle", new RuntimeException("inner"))));

        assertFalse(KafkaOpenMetadataEventProducer.isExceptionRetryable(error));
    }


    /**
     * A retryable exception directly under the send failure is found.
     */
    @Test(timeOut = 5000)
    public void testRetryableDirectCause()
    {
        Exception error = new ExecutionException(new TimeoutException("broker not responding"));

        assertTrue(KafkaOpenMetadataEventProducer.isExceptionRetryable(error));
    }


    /**
     * A retryable exception several levels down the chain is found.
     */
    @Test(timeOut = 5000)
    public void testRetryableNestedCause()
    {
        Exception error = new ExecutionException(
                new KafkaException("outer", new RuntimeException("middle", new NotLeaderOrFollowerException("leader moved"))));

        assertTrue(KafkaOpenMetadataEventProducer.isExceptionRetryable(error));
    }


    /**
     * A retryable exception passed in directly is recognized.
     */
    @Test(timeOut = 5000)
    public void testRetryableItself()
    {
        assertTrue(KafkaOpenMetadataEventProducer.isExceptionRetryable(new TimeoutException("timed out")));
    }


    /**
     * An exception with no cause, and a null, return false.
     */
    @Test(timeOut = 5000)
    public void testNoCause()
    {
        assertFalse(KafkaOpenMetadataEventProducer.isExceptionRetryable(new Exception("no cause")));
        assertFalse(KafkaOpenMetadataEventProducer.isExceptionRetryable(null));
    }
}
