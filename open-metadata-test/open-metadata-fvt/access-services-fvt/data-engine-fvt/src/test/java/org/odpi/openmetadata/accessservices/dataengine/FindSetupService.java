/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.rest.FindRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.model.Identifiers;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.Objects;

/**
 * Triggers find requests via client
 */
public class FindSetupService {

    public FindSetupService() { }

    /**
     * Finds an entity via DataEngine OMAS
     *
     * @param userId the user which creates the data engine
     * @param dataEngineOMASClient the data engine client that is used to create the external data engine
     * @param qualifiedName qualified name by which to find entity
     * @param type type by which to find entity (optional)
     * @param externalSourceName external source name by which to find entity (optional)
     *
     * @return list of guids that matched
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDListResponse find(String userId, DataEngineClient dataEngineOMASClient, String qualifiedName, String type,
                                 String externalSourceName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, ConnectorCheckedException,
            FunctionNotSupportedException {

        if(Objects.isNull(qualifiedName)){
            throw new IllegalArgumentException("Parameter required: qualifiedName");
        }
        FindRequestBody findRequestBody = getDefaultFindRequestBody(qualifiedName, type, externalSourceName);
        return dataEngineOMASClient.find(userId, findRequestBody);
    }

    private FindRequestBody getDefaultFindRequestBody(String qualifiedName, String type, String externalSourceName){
        Identifiers identifiers = new Identifiers();
        identifiers.setQualifiedName(qualifiedName);

        FindRequestBody findRequestBody = new FindRequestBody();
        findRequestBody.setExternalSourceName(externalSourceName);
        findRequestBody.setType(type);
        findRequestBody.setIdentifiers(identifiers);
        return findRequestBody;
    }

}
