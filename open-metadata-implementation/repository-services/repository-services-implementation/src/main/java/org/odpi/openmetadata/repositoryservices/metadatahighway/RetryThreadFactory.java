/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.metadatahighway;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Factory that create the threads used for initialization retries
 */
class RetryThreadFactory implements ThreadFactory
{

    /**
     * The singleton instance of {@link RetryThreadFactory}
     */
    public static final ThreadFactory INSTANCE = new RetryThreadFactory();

    private static final AtomicInteger threadCount = new AtomicInteger();

    /**
     * Singleton constructor
     */
    private RetryThreadFactory()
    {

    }

    @Override
    public Thread newThread(Runnable r)
    {

        Thread thr = new Thread(r);
        thr.setName("OMRS Cohort Initialization Retry Thread " + threadCount.incrementAndGet());
        thr.setDaemon(true);
        return thr;
    }
}
