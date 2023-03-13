/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore;

import org.testng.annotations.Test;

/**
 * OMRSAuditLogStoreConnectorTest just tests the null constructor.
 */
public class OMRSAuditLogStoreConnectorTest
{
   @Test public void testConstructor()
   {
       new MockOMRSAuditLogStoreConnectorBase();
   }
}
