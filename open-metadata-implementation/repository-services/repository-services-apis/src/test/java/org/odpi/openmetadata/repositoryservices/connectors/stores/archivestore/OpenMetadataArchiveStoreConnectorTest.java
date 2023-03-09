/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore;

import org.testng.annotations.Test;

/**
 * OpenMetadataArchiveStoreConnectorTest just tests the null constructor and the fact that the connector
 * is auditable.
 */
public class OpenMetadataArchiveStoreConnectorTest
{
   @Test public void testConstructor()
   {
       OpenMetadataArchiveStoreConnector connector = new MockOpenMetadataArchiveStoreConnector();

       connector.setAuditLog(null);
   }
}
