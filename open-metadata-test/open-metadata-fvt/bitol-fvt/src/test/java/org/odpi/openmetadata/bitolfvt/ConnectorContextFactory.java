/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.bitolfvt;

import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;

import java.util.UUID;

/**
 * Builds a standalone {@link ConnectorContextBase} - the same object the platform hands to a connector - so
 * that the tests can read the repository back over REST, and run the Bitol generators against it, without a
 * connector broker in the loop.  The tests use it to <em>check</em> what the cataloguers produced; the
 * cataloguing itself is done by the connectors running in the integration daemon.
 */
class ConnectorContextFactory
{
    /**
     * Create a new connector context pointed at this suite's metadata access store.
     *
     * @return connector context
     * @throws Exception problem creating the underlying client
     */
    static ConnectorContextBase newContext() throws Exception
    {
        OpenMetadataClient openMetadataClient = new EgeriaOpenMetadataStoreClient(OMAGPlatformExtension.METADATA_STORE_NAME,
                                                                                  OMAGPlatformExtension.getPlatformURLRoot(),
                                                                                  (String) null,
                                                                                  null,
                                                                                  null,
                                                                                  BitolFvtTestSupport.MAX_PAGE_SIZE,
                                                                                  null);

        return new ConnectorContextBase(OMAGPlatformExtension.METADATA_STORE_NAME,
                                         "bitol-fvt",
                                         null,
                                         null,
                                         UUID.randomUUID().toString(),
                                         "bitol-fvt",
                                         OMAGPlatformExtension.USER_ID,
                                         null,
                                         false,
                                         openMetadataClient,
                                         null,
                                         BitolFvtTestSupport.MAX_PAGE_SIZE,
                                         DeleteMethod.SOFT_DELETE);
    }
}
