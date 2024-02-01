/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.frameworks.connectors.properties.NestedSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ComplexSchemaType;

import java.io.Serial;

class ConnectedNestedSchemaType extends NestedSchemaType
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Typical constructor creates an NestedSchemaType object primed with information to retrieve an asset's schema
     * information.
     *
     * @param bean super class properties
     * @param serviceName calling service
     * @param serverName  name of the server.
     * @param platformRootURL url root of the server to use.
     * @param userId user id to use on server calls.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     * @param restClient client to call REST API
     */
    ConnectedNestedSchemaType(ComplexSchemaType      bean,
                              String                 serviceName,
                              String                 serverName,
                              String                 platformRootURL,
                              String                 userId,
                              int                    maxCacheSize,
                              OCFRESTClient          restClient)
    {
        super(bean);

        super.schemaAttributes = new ConnectedSchemaAttributes(serviceName,
                                                               serverName,
                                                               userId,
                                                               platformRootURL,
                                                               this.getGUID(),
                                                               this.getAttributeCount(),
                                                               maxCacheSize,
                                                               restClient);

    }
}
