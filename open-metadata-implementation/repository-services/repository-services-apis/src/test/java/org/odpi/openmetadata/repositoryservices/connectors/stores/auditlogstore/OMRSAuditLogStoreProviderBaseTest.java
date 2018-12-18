/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore;

import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.MockOpenMetadataArchiveStoreProvider;
import org.testng.annotations.Test;

/**
 * OpenMetadataArchiveStoreProviderBaseTest just tests the null constructor
 */
public class OMRSAuditLogStoreProviderBaseTest
{
   @Test public void testConstructor()
   {
       new MockOMRSAuditLogStoreProvider();
   }
}
