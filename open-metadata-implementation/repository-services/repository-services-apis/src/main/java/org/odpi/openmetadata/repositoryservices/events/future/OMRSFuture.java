/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events.future;

/**
 * Future wrapper to prevent consumers from 
 * directly accessing Futures created by other 
 * consumers.
 */
public interface OMRSFuture
{
    /**
     * Checks whether processing for the future has completed.
     *
     * @return boolean
     */
    boolean isDone();

}
