/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.omrstopic;

import java.util.concurrent.Future;

import org.odpi.openmetadata.repositoryservices.events.future.OMRSFuture;

/**
 * OMRS message processing context.  This class allows Egeria to be notified when a message is being
 * processed asynchronously so that the consumer can correctly record when the message has been
 * completely processed.
 * 
 * This class is considered part of the Egeria public API and is used
 * directly by consumer implementations.
 * 
 */
public class OMRSEventProcessingContext {
    
    /**
     * Registers a {@link Future} so that Egeria can keep track of asynchronous message
     * processing being done by consumers.  Egeria will not mark a message as processed
     * until all consumers have fully processed the message or the processing timeout
     * has expired
     * 
     * @param future
     */
    public static void addAsyncProcessingResult(Future<?> future) {
        InternalOMRSEventProcessingContext.getInstance().addAsyncProcessingResult(future);
    }
    
   /**
    * Registers a {@link Future} so that Egeria can keep track of asynchronous message
    * processing being done by consumers.  Egeria will not mark a message as processed
    * until all consumers have fully processed the message or the processing timeout
    * has expired
    * 
    * @param future
    */
    public static void addAsyncProcessingResult(OMRSFuture future) {
        InternalOMRSEventProcessingContext.getInstance().addAsyncProcessingResult(future);
    }
}
