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
     * @param future The future that should be monitored by Egeria to detect
     *   when the asynchronous processing is complete
     */
    public static void addAsyncProcessingResult(Future<?> future)
    {
        InternalOMRSEventProcessingContext.getInstance().addAsyncProcessingResult(future);
    }
    
   /**
    * Registers a {@link Future} so that Egeria can keep track of asynchronous message
    * processing being done by consumers.  Egeria will not mark a message as processed
    * until all consumers have fully processed the message or the processing timeout
    * has expired
    * 
    * @param future The future that should be monitored by Egeria to detect
    *   when the asynchronous processing is complete
    */
    public static void addAsyncProcessingResult(OMRSFuture future)
    {
        InternalOMRSEventProcessingContext.getInstance().addAsyncProcessingResult(future);
    }
    
    /**
     * Gets unique identifier for the current message.  The identifier is specific to
     * the underlying messaging system.  If a message is being reprocessed (eg due
     * to a server restart before the message was fully processed), the identifier
     * will be the same.  The identifier should be different if content happens
     * to be the same as the content of another message but they are actually different
     * messages.
     * 
     * @return the current message id
     */
    public static String getCurrentMessageId()
    {
        return InternalOMRSEventProcessingContext.getInstance().getCurrentMessageId();
    }
}
