/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.frameworks.connectors.properties.DeployedAPIOperation;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.APIOperation;

import java.io.Serial;

class ConnectedDeployedAPIOperation extends DeployedAPIOperation
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Typical constructor creates an DeployedAPIOperation object primed with information to retrieve an asset's API operations.
     *
     * @param bean super class properties
     * @param serviceName calling service
     * @param serverName  name of the server.
     * @param platformURLRoot url root of the server to use.
     * @param userId user id to use on server calls.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     * @param restClient client to call REST API
     */
    ConnectedDeployedAPIOperation(APIOperation  bean,
                                  int           headerAttributeCount,
                                  int           requestAttributeCount,
                                  int           responseAttributeCount,
                                  String        serviceName,
                                  String        serverName,
                                  String        platformURLRoot,
                                  String        userId,
                                  int           maxCacheSize,
                                  OCFRESTClient restClient)
    {
        super(bean);

        if (headerAttributeCount > 0)
        {
            super.headerAttributes = new ConnectedSchemaAttributes(serviceName,
                                                                   serverName,
                                                                   userId,
                                                                   platformURLRoot,
                                                                   this.getGUID(),
                                                                   maxCacheSize,
                                                                   restClient);

        }

        if (requestAttributeCount > 0)
        {
            super.headerAttributes = new ConnectedSchemaAttributes(serviceName,
                                                                   serverName,
                                                                   userId,
                                                                   platformURLRoot,
                                                                   this.getGUID(),
                                                                   maxCacheSize,
                                                                   restClient);

        }

        if (responseAttributeCount > 0)
        {
            super.headerAttributes = new ConnectedSchemaAttributes(serviceName,
                                                                   serverName,
                                                                   userId,
                                                                   platformURLRoot,
                                                                   this.getGUID(),
                                                                   maxCacheSize,
                                                                   restClient);

        }
    }
}
