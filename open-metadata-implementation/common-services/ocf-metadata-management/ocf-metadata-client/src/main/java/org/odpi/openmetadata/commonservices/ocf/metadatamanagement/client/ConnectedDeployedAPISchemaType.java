/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.frameworks.connectors.properties.DeployedAPISchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.APISchemaType;

class ConnectedDeployedAPISchemaType extends DeployedAPISchemaType
{
    private static final long    serialVersionUID = 1L;

    /**
     * Typical constructor creates an DeployedAPISchemaType object primed with information to retrieve an asset's API operations.
     *
     * @param bean super class properties
     * @param serviceName calling service
     * @param serverName  name of the server.
     * @param omasServerURL url root of the server to use.
     * @param userId user id to use on server calls.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     * @param restClient client to call REST API
     */
    ConnectedDeployedAPISchemaType(APISchemaType bean,
                                   String        serviceName,
                                   String        serverName,
                                   String        omasServerURL,
                                   String        userId,
                                   int           maxCacheSize,
                                   OCFRESTClient restClient)
    {
        super(bean);

        if (this.getOperationCount() > 0)
        {
            super.apiOperations = new ConnectedAPIOperations(serviceName,
                                                             serverName,
                                                             userId,
                                                             omasServerURL,
                                                             this.getGUID(),
                                                             maxCacheSize,
                                                             this.getOperationCount(),
                                                             restClient);

        }
    }
}
