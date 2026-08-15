/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.bvt;

import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;

import java.util.UUID;

/**
 * Builds a standalone {@link ConnectorContextBase} - the same object a connector would be handed by the
 * platform - so that the BVT tests can drive its connector context clients (AssetClient, CollectionClient,
 * ProjectClient, and so on) directly over REST against the server that {@link OMAGPlatformExtension}
 * started, without needing a real connector or connector broker in the loop.
 */
class ConnectorContextFactory
{
    static final int MAX_PAGE_SIZE = 100;


    /**
     * Create a new connector context backed by a fresh {@link EgeriaOpenMetadataStoreClient} pointed at
     * the BVT server.
     *
     * @return connector context
     * @throws Exception problem creating the underlying client
     */
    static ConnectorContextBase newContext() throws Exception
    {
        OpenMetadataClient openMetadataClient = new EgeriaOpenMetadataStoreClient(OMAGPlatformExtension.SERVER_NAME,
                                                                                   OMAGPlatformExtension.getPlatformURLRoot(),
                                                                                   (String) null,
                                                                                   null,
                                                                                   null,
                                                                                   MAX_PAGE_SIZE,
                                                                                   null);

        return new ConnectorContextBase(OMAGPlatformExtension.SERVER_NAME,
                                         "open-metadata-bvt",
                                         null,
                                         null,
                                         UUID.randomUUID().toString(),
                                         "open-metadata-bvt",
                                         OMAGPlatformExtension.USER_ID,
                                         null,
                                         false,
                                         openMetadataClient,
                                         null,
                                         MAX_PAGE_SIZE,
                                         DeleteMethod.PURGE);
    }
}
