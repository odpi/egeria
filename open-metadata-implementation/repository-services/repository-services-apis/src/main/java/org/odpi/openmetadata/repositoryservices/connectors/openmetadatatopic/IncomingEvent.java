/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventProcessor;
import org.odpi.openmetadata.repositoryservices.events.future.OMRSFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An event that was received from a connector
 */
public class IncomingEvent {
    
    private final long creationTime = System.currentTimeMillis();
    private static final Logger logger = LoggerFactory.getLogger(IncomingEvent.class);
    private volatile IncomingEventState currentState = IncomingEventState.CREATED;
    
    private final String json;
    private final List<OMRSFuture> asyncProcessingResults = Collections.synchronizedList(new ArrayList<>()); 
    
    /**
     * Constructor
     * 
     * @param json the json for the event
     */
    public IncomingEvent(String json) {
        this.json = json;
    }
    
    public String getJson() {
        return json;
    }
    
    /**
     * Adds a {@link Future} for the processing of this event by some
     * {@link OMRSInstanceEventProcessor}
     * @param future the {@link Future} to add
     */
    public void addAsyncProcessingResult(OMRSFuture future) {
        synchronized (asyncProcessingResults) {
            asyncProcessingResults.add(future);
        }
    }
    
    /**
     * Updates the state of the event
     * 
     * @param state the new state
     */
    public void setState(IncomingEventState state) {
        synchronized (this) {
            currentState = state;
        }
    }
    
    public long getCreationTime() {
        return creationTime;
    }
    
    /**
     * Checks whether the given amount of time has elapsed
     * since the event was created
     * 
     * @param elapsedTimeMs the elapsed time to check, in milliseconds
     */
    public boolean hasTimeElapsedSinceCreation(long elapsedTimeMs) {
        long now = System.currentTimeMillis();
        long elapsed = now - creationTime;
        return elapsed >= elapsedTimeMs;
    }
    /**
     * Checks whether all processing for this event has completed.
     * 
     * @return whether all processing for this event has completed
     */
    public boolean isFullyProcessed() {
        
        synchronized (this) {
            //We need to check the state because while the state is CREATED,
            //additional asynchronous processing results can still be added by topic
            //listeners.  In addition, for events with no asynchronous processing, the 
            //state is how we know whether the event has been fully processed.  While
            //the state is still CREATED, the event is still being passed
            //to and consumed by topic listeners.
            
            if (currentState == IncomingEventState.CREATED) {
               return false; 
            }
        }
        
        //If any asynchronous processing results have been added, we need
        //to check them.  The event has not been fully processed until
        //all asynchronous processing has completed.
        synchronized(asyncProcessingResults) {
            for(OMRSFuture future: asyncProcessingResults) {
                if (! future.isDone()) {
                    return false;
                }
            }
            return true;
        }
    }
}
