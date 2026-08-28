/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.duplicatefvt;

import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;

import java.util.UUID;

/**
 * Builds a standalone {@link ConnectorContextBase} - the same object a connector would be handed by the
 * platform - so that the duplicate-fvt tests can drive its connector context clients (the generic
 * {@code OpenMetadataStore} plus the typed clients) directly over REST against the metadata access store
 * that {@link OMAGPlatformExtension} started, without needing a real connector in the loop.
 * <br>
 * This is deliberately the same route Mendel itself takes to open metadata, so a test that reads the
 * repository sees exactly what the connector would see - including the deduplication, which is applied on
 * the way out of the repository handler rather than by any client.
 */
class ConnectorContextFactory
{
    static final int MAX_PAGE_SIZE = DuplicateFvtTestSupport.MAX_PAGE_SIZE;


    /**
     * Create a new connector context pointed at the duplicate-fvt metadata access store.
     *
     * @return connector context
     * @throws Exception problem creating the underlying client
     */
    static ConnectorContextBase newContext() throws Exception
    {
        OpenMetadataClient openMetadataClient = newOpenMetadataClient();

        return new ConnectorContextBase(OMAGPlatformExtension.METADATA_STORE_NAME,
                                         "duplicate-fvt",
                                         null,
                                         null,
                                         UUID.randomUUID().toString(),
                                         "duplicate-fvt",
                                         OMAGPlatformExtension.USER_ID,
                                         null,
                                         false,
                                         openMetadataClient,
                                         null,
                                         MAX_PAGE_SIZE,
                                         DeleteMethod.SOFT_DELETE);
    }


    /**
     * Create the client that the connector context is built on.  Some of the retrieval calls the tests need -
     * the exact-match, list-returning name lookups - are on the client rather than the connector context's
     * OpenMetadataStore, so the tests use it directly for those.
     *
     * @return open metadata client
     * @throws Exception problem creating the client
     */
    static OpenMetadataClient newOpenMetadataClient() throws Exception
    {
        return new EgeriaOpenMetadataStoreClient(OMAGPlatformExtension.METADATA_STORE_NAME,
                                                  OMAGPlatformExtension.getPlatformURLRoot(),
                                                  (String) null,
                                                  null,
                                                  null,
                                                  MAX_PAGE_SIZE,
                                                  null);
    }
}
