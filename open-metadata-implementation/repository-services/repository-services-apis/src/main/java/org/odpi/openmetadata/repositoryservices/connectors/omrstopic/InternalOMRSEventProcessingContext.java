/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.omrstopic;

import java.util.concurrent.Future;

import org.odpi.openmetadata.repositoryservices.events.future.CompletedFuture;
import org.odpi.openmetadata.repositoryservices.events.future.CompoundFuture;
import org.odpi.openmetadata.repositoryservices.events.future.OMRSFuture;
import org.odpi.openmetadata.repositoryservices.events.future.OMRSFutureImpl;

/**
 * Internal Asynchronous message processing context.  This class allows egeria to be notified when a message is being
 * processed asynchronously so that the consumer can correctly record when the message has been
 * completely processed.  This class is not considered part of the Egeria public
 * API and may change without notice.  
 */
public class InternalOMRSEventProcessingContext
{
    private static final ThreadLocal<InternalOMRSEventProcessingContext> INSTANCE = new ThreadLocal<>();

    //unique identifier for the current message
    private String currentMessageId;
    
    private final CompoundFuture processingResult = new CompoundFuture();
    
    /**
     * Registers a {@link Future} associated with asynchronous message processing.
     * 
     * @param future registered object
     */
    public void addAsyncProcessingResult(Future<?> future) {
        processingResult.addFuture(new OMRSFutureImpl(future));
    }


    /**
     * Registers an {@link OMRSFuture} associated with asynchronous message processing.
     *
     * @param future registered object
     */
    public void addAsyncProcessingResult(OMRSFuture future) {
        processingResult.addFuture(future);
    }


    /**
     * Gets the overall asynchronous processing result from all consumers.
     * 
     * This method is guaranteed to return a non-null result.  If there is no
     * asynchronous processing happening, an instance of {@link CompletedFuture} will
     * be returned, indicating that asynchronous processing has finished
     * 
     * @return the overall processing result
     */
    public OMRSFuture getOverallAsyncProcessingResult()
    {
        if (processingResult.hasChildren())
        {
            return processingResult;
        }
        return CompletedFuture.INSTANCE;
    }


    /**
     * Gets the thread-local {@link InternalOMRSEventProcessingContext} instance
     * for the current thread.
     * 
     * @return the instance of {@link InternalOMRSEventProcessingContext} for the current thread
     */
    public static InternalOMRSEventProcessingContext getInstance()
    {
        InternalOMRSEventProcessingContext instance = INSTANCE.get();

        if (instance == null)
        {
            instance = new InternalOMRSEventProcessingContext();
            INSTANCE.set(instance);
        }
        return instance;
    }
    
    /**
     * Clears the {@link InternalOMRSEventProcessingContext} for the
     * current thread.
     */
     public static void clear()
     {
        INSTANCE.set(null);
     }
     
    /**
     * Gets the unique identifier for the current message that
     * is being processed.
     * 
     * @return the current message id
     */
    public String getCurrentMessageId() {
        return currentMessageId;
    }
    
    /**
     * Sets the unique identifier for the current message that
     * is being processed.
     * 
     * @param messageId The messageId to set
     */
    public void setCurrentMessageId(String messageId) { 
        this.currentMessageId = messageId;
    }
}
