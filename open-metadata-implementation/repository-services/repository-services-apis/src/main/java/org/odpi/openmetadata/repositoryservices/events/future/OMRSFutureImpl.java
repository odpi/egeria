/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events.future;

import java.util.concurrent.Future;

/**
 * Implementation of {@link OMRSFuture}
 */
public class OMRSFutureImpl implements OMRSFuture
{
    private final Future<?> wrappedFuture;


    /**
     * Constructor
     *
     * @param future future to nest
     */
    public OMRSFutureImpl(Future<?> future)
    {
        wrappedFuture = future;
    }
    
    /**
     * Checks whether processing for the future has completed.
     * @return whether the processing for the future has completed
     */
    @Override
    public boolean isDone()
    {
        return wrappedFuture.isDone();
    }
}
