/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * Connection and Endpoint are generated when creating data files. This class only triggers requests via client to delete
 */
public class ConnectionAndEndpointSetupService {

    /**
     * Delete a Connection using the dataEngineClient provided
     *
     * @param userId user
     * @param dataEngineClient data engine client
     * @param qualifiedName qualified name
     * @param guid guid
     */
    public void deleteConnection(String userId, DataEngineClient dataEngineClient, String qualifiedName, String guid)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {
        if(qualifiedName == null || guid == null){
            throw new IllegalArgumentException("Unable to delete Connection. QualifiedName and Guid are both required. Missing at least one");
        }
        dataEngineClient.deleteConnection(userId, qualifiedName, guid);
    }

    /**
     * Delete an Endpoint using the dataEngineClient provided
     *
     * @param userId user
     * @param dataEngineClient data engine client
     * @param qualifiedName qualified name
     * @param guid guid
     */
    public void deleteEndpoint(String userId, DataEngineClient dataEngineClient, String qualifiedName, String guid)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {
        if(qualifiedName == null || guid == null){
            throw new IllegalArgumentException("Unable to delete Endpoint. QualifiedName and Guid are both required. Missing at least one");
        }
        dataEngineClient.deleteEndpoint(userId, qualifiedName, guid);
    }

}
