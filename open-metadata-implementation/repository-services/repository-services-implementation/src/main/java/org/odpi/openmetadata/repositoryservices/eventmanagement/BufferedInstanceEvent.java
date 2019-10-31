/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.eventmanagement;

import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.future.DelegatableFuture;

/**
 * An instance event that is buffered because 
 * the {@link OMRSRepositoryEventManager} is not
 * active yet.
 * 
 * @see OMRSRepositoryEventManager
 */
public class BufferedInstanceEvent
{
    private final OMRSInstanceEvent event;
    
    private final String messageId;
    private final DelegatableFuture associatedFuture = new DelegatableFuture();
    
    /**
     * Constructor
     * 
     * @param event event content
     * @param messageId identifier
     */
    public BufferedInstanceEvent(OMRSInstanceEvent event, String messageId)
    {
        this.event = event;
        this.messageId = messageId;
    }
    
    
    /**
     * Gets the associated {@link OMRSInstanceEvent}
     * 
     * @return returns event content
     */
    public OMRSInstanceEvent getEvent() {
        return event;
    }


    /**
     * Gets the {@link DelegatableFuture} associated with the event
     * @return future object
     */
    public DelegatableFuture getFuture() {
        return associatedFuture;
    }


    /**
     * Gets the message id for the event.
     *
     * @return messageId
     */
    public String getMessageId() {
        return messageId;
    }
    
}