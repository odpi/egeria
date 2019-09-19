/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events.future;

/**
 * Future that represents a task that has already completed.
 */
public class CompletedFuture implements OMRSFuture {
    
    public static final CompletedFuture INSTANCE = new CompletedFuture();
    
    /**
     * Singleton constructor
     *
     */
    private CompletedFuture() {
      
    }
  
    @Override
    public boolean isDone() {
        return true;
    }
}
