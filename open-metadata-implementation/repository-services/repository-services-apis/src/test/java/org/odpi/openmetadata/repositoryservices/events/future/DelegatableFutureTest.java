/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events.future;

import org.odpi.openmetadata.repositoryservices.events.future.CompletedFuture;
import org.odpi.openmetadata.repositoryservices.events.future.DelegatableFuture;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for {@link DelegatableFuture}
 */
public class DelegatableFutureTest {
    
    @Test
    public void testIsDoneReturnsTrueOnceDelegateIsSet() throws Throwable {
        
        DelegatableFuture future = new DelegatableFuture();
 
        Assert.assertFalse(future.isDone());
        future.setDelegate(CompletedFuture.INSTANCE);
       
        Assert.assertTrue(future.isDone());
        
    }
}
