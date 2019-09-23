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
public class BufferedInstanceEvent {
    
    private final OMRSInstanceEvent event;
    private final DelegatableFuture associatedFuture = new DelegatableFuture();
    
    public BufferedInstanceEvent(OMRSInstanceEvent event) {
        this.event = event;
    }
    public OMRSInstanceEvent getEvent() {
        return event;
    }
    
    public DelegatableFuture getFuture() {
        return associatedFuture;
    }
    
}