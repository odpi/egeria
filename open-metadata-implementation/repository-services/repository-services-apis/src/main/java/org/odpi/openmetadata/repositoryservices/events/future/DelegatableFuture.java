/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events.future;

/**
 * Future that starts out as unfinished can can be delegated
 * to a different Future once processing takes place.  This is
 * useful if we need to return a future for an event that has 
 * not been scheduled yet. 
 *
 */
public class DelegatableFuture implements OMRSFuture
{
    private volatile OMRSFuture delegate;

    /**
     * Sets the future to delegate to.  This can only
     * be called once.
     * 
     * @param delegate future
     */
    public void setDelegate(OMRSFuture delegate)
    {
        // synchronize setting the delegate to close timing windows that
        // would allow waiting for the delegate to be set indefinitely. See
        // safelyWaitForDelegateToBeSet()
        if (this.delegate != null)
        {
            throw new RuntimeException("Delegate is already set");
        }

        this.delegate = delegate;
    }

    /**
     * Return whether complete
     */
    @Override
    public boolean isDone()
    {
        if (delegate != null)
        {
            return delegate.isDone();
        }
        return false;
    }
}
