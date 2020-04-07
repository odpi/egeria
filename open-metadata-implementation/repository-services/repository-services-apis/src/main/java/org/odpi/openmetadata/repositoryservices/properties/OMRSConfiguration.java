/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.properties;

/**
 * Allows applications to programmatically configure some aspects
 * of Egeria using their values from their own configuration
 * mechanism.
 */
public class OMRSConfiguration {
    
    private static final OMRSConfiguration INSTANCE = new OMRSConfiguration();
    
    private int numInitRetryThreads = 1;

    /**
     * Gets the number of threads in the thread pool used to
     * retry failed initializations.  The thread pool is
     * shared between all components.  The default is one thread.
     */
    public int getNumberOfInitRetryThreads() {
    
        return numInitRetryThreads;
    }
    /**
     * Sets the number of threads in the thread pool used to
     * retry failed initializations.  The thread pool is
     * shared between all components.  The default is one thread.
     * 
     * @param numInitThreads size of initialization retry thread pool
     */
    public void setNumberOfInitRetryThreads(int numInitThreads) {
    
        this.numInitRetryThreads = numInitThreads;
    }
    
    /**
     * Gets the singleton {@link OMRSConfiguration} instance
     * @return
     */
    public static OMRSConfiguration getInstance() {
    
        return INSTANCE;
    }
}