/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore;

import org.testng.annotations.Test;

/**
 * OpenMetadataArchiveStoreProviderBaseTest just tests the null constructor
 */
public class OpenMetadataArchiveStoreProviderBaseTest
{
   @Test public void testConstructor()
   {
       new MockOpenMetadataArchiveStoreProvider();
   }
}
