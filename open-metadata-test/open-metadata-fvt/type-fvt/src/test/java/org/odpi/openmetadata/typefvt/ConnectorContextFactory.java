/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.typefvt;

import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;

import java.util.UUID;

/**
 * Builds a standalone {@link ConnectorContextBase} - the same object a connector would be handed by the
 * platform - so that the type-fvt tests can drive its connector context clients (the generic
 * {@code OpenMetadataStore} plus the typed clients such as {@code CollectionClient}) directly over REST
 * against the server that {@link OMAGPlatformExtension} started, without needing a real connector or
 * connector broker in the loop.
 */
class ConnectorContextFactory
{
    static final int MAX_PAGE_SIZE = 100;


    /**
     * Create a new connector context backed by a fresh {@link EgeriaOpenMetadataStoreClient} pointed at
     * the type-fvt server, whose ordinary "delete" calls perform a soft delete (the element remains
     * retrievable via a status-filtered query, only vanishing from the default "active only" view) -
     * this is what most of the test suite needs, since {@link StatusFVT} specifically checks
     * that soft-deleted elements can still be found via {@code limitResultsByStatus}.
     *
     * @return connector context
     * @throws Exception problem creating the underlying client
     */
    static ConnectorContextBase newContext() throws Exception
    {
        return newContext(DeleteMethod.SOFT_DELETE);
    }


    /**
     * Create a new connector context backed by a fresh {@link EgeriaOpenMetadataStoreClient} pointed at
     * the type-fvt server, using the supplied delete method for any "delete" call made through it.
     *
     * @param defaultDeleteMethod delete method applied by simple (boolean-cascade) delete calls made
     *                            through clients obtained from the returned context
     * @return connector context
     * @throws Exception problem creating the underlying client
     */
    static ConnectorContextBase newContext(DeleteMethod defaultDeleteMethod) throws Exception
    {
        OpenMetadataClient openMetadataClient = new EgeriaOpenMetadataStoreClient(OMAGPlatformExtension.SERVER_NAME,
                                                                                   OMAGPlatformExtension.getPlatformURLRoot(),
                                                                                   (String) null,
                                                                                   null,
                                                                                   null,
                                                                                   MAX_PAGE_SIZE,
                                                                                   null);

        return new ConnectorContextBase(OMAGPlatformExtension.SERVER_NAME,
                                         "type-fvt",
                                         null,
                                         null,
                                         UUID.randomUUID().toString(),
                                         "type-fvt",
                                         OMAGPlatformExtension.USER_ID,
                                         null,
                                         false,
                                         openMetadataClient,
                                         null,
                                         MAX_PAGE_SIZE,
                                         defaultDeleteMethod);
    }
}
