/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.init;

/**
 * A method of performing initialization
 * 
 */
public interface InitializationMethod 
{
    
    /**
     * Attempts to initialize the component.  In the event of an initialization
     * failure, this method may end up being called multiple times
     * 
     * @return
     */
    InitializationResult attemptInitialization();
    
    /**
     * Returns true if the initialization failed in
     * a way that should be retried.  
     * 
     * Note that a retry will only actually be performed if the maximum
     * number of retries has not been exceeded.
     * 
     * @param result
     * @return
     */
    boolean isRetryNeeded(InitializationResult result);
    
    /**
     * Human readable name of the object being initialized
     * @return
     */
    String getObjectName();
}