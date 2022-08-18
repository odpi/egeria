/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.events.future;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link OMRSFuture} which has a list of child futures.  It
 * is not complete until all of its child futures have completed.
 */
public class CompoundFuture implements OMRSFuture {
    
    private final List<OMRSFuture> children = new ArrayList<>();
   
    /**
     * Adds a future whose result should be included
     * 
     * @param future the future to add
     */
    public void addFuture(OMRSFuture future) {
        children.add(future);
    }
    
    public boolean hasChildren() {
        return ! children.isEmpty();
    }

    @Override
    public boolean isDone() {
        //the future is done when all the children are done
        for(OMRSFuture future : children) {
            if (! future.isDone()) {
                return false;
            }
        }
        return true;
    }
    
  }