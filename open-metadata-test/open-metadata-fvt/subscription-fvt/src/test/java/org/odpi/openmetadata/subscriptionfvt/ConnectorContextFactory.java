/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.subscriptionfvt;

import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;

import java.util.UUID;

/**
 * Builds a standalone {@link ConnectorContextBase} - the same object the platform hands to a connector - so
 * that the subscription-fvt tests can read the repository back through the generic {@code OpenMetadataStore} over
 * REST, without needing a connector or a connector broker in the loop.
 * <br>
 * The tests use this to <em>check</em> what the connectors and governance services produced.  They do not use
 * it to produce it: everything under test is started through the Automated Curation API and carried out by
 * the engine host and the integration daemon, which is the whole point of the suite.
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
        return newContext(DeleteMethod.SOFT_DELETE);
    }


    /**
     * Create a new connector context pointed at this suite's metadata access store, using the supplied delete
     * method for any "delete" call made through it.
     *
     * @param defaultDeleteMethod delete method applied by simple delete calls made through clients obtained
     *                            from the returned context
     * @return connector context
     * @throws Exception problem creating the underlying client
     */
    static ConnectorContextBase newContext(DeleteMethod defaultDeleteMethod) throws Exception
    {
        OpenMetadataClient openMetadataClient = new EgeriaOpenMetadataStoreClient(OMAGPlatformExtension.METADATA_STORE_NAME,
                                                                                  OMAGPlatformExtension.getPlatformURLRoot(),
                                                                                  (String) null,
                                                                                  null,
                                                                                  null,
                                                                                  SubscriptionFvtTestSupport.MAX_PAGE_SIZE,
                                                                                  null);

        return new ConnectorContextBase(OMAGPlatformExtension.METADATA_STORE_NAME,
                                         "subscription-fvt",
                                         null,
                                         null,
                                         UUID.randomUUID().toString(),
                                         "subscription-fvt",
                                         OMAGPlatformExtension.USER_ID,
                                         null,
                                         false,
                                         openMetadataClient,
                                         null,
                                         SubscriptionFvtTestSupport.MAX_PAGE_SIZE,
                                         defaultDeleteMethod);
    }
}
