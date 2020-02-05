/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetconsumer.connectors;

import org.odpi.openmetadata.accessservices.assetconsumer.api.AssetConsumerEventInterface;
import org.odpi.openmetadata.accessservices.assetconsumer.api.AssetConsumerEventListener;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;

/**
 * AssetConsumerOutTopicClientConnector is the java implementation of the
 * the client side connector that receives events from the Asset Consumer OMAS's out topic
 */
public class AssetConsumerOutTopicClientConnector extends ConnectorBase implements AssetConsumerEventInterface
{
    /**
     * Register a listener object that will be passed each of the events published by
     * the Asset Consumer OMAS.
     *
     * @param userId calling user
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(String                     userId,
                                 AssetConsumerEventListener listener) throws InvalidParameterException,
                                                                             ConnectionCheckedException,
                                                                             ConnectorCheckedException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {

    }
}
