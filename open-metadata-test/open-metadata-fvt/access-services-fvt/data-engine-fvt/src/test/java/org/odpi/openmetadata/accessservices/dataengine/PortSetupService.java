/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * Generates test data of type PortImplementation and PortAlias, and triggers requests via client for aforementioned type
 */
public class PortSetupService {

    /**
     * Creates a PortImplementation or updates one.
     *
     * @param userId the user which creates the data engine
     * @param dataEngineClient the data engine client that is used to create the external data engine
     * @param portImplementation port implementation to upsert. If null, a default will be used
     * @param processQualifiedName process qualified name. Required
     *
     * @return portImplementation
     */
    public PortImplementation createOrUpdatePortImplementation(String userId, DataEngineClient dataEngineClient,
                                                    PortImplementation portImplementation, String processQualifiedName)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {
        if(processQualifiedName == null || processQualifiedName.isEmpty()){
            throw new IllegalArgumentException("processQualifiedName is null");
        }
        if(portImplementation == null) {
            portImplementation = getDefaultPortImplementation();
        }
        dataEngineClient.createOrUpdatePortImplementation(userId, portImplementation, processQualifiedName);
        return portImplementation;
    }

    private PortImplementation getDefaultPortImplementation() {
        PortImplementation portImplementation = new PortImplementation();
        portImplementation.setQualifiedName("port-implementation-qualified-name");
        portImplementation.setDisplayName("port-implementation-display-name");
        portImplementation.setPortType(PortType.INPUT_PORT);
        return portImplementation;
    }

    /**
     * Delete a PortImplementation using the dataEngineClient provided
     *
     * @param userId user
     * @param dataEngineClient data engine client
     * @param qualifiedName qualified name
     * @param guid guid
     */
    public void deletePortImplementation(String userId, DataEngineClient dataEngineClient, String qualifiedName, String guid)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {
        if(qualifiedName == null || guid == null){
            throw new IllegalArgumentException("Unable to delete PortImplementation. QualifiedName and Guid are both required. Missing at least one");
        }
        dataEngineClient.deletePortImplementation(userId, qualifiedName, guid);
    }

    /**
     * Creates a PortAlias or updates one.
     *
     * @param userId the user which creates the data engine
     * @param dataEngineClient the data engine client that is used to create the external data engine
     * @param portAlias port alias to upsert. If null, a default will be used
     * @param processQualifiedName process qualified name. Required
     *
     * @return portImplementation
     */
    public PortAlias createOrUpdatePortAlias(String userId, DataEngineClient dataEngineClient,
                                                               PortAlias portAlias, String processQualifiedName)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {
        if(processQualifiedName == null || processQualifiedName.isEmpty()){
            throw new IllegalArgumentException("processQualifiedName is null");
        }
        if(portAlias == null) {
            portAlias = getDefaultPortAlias();
        }
        dataEngineClient.createOrUpdatePortAlias(userId, portAlias, processQualifiedName);
        return portAlias;
    }

    private PortAlias getDefaultPortAlias() {
        PortAlias portAlias = new PortAlias();
        portAlias.setQualifiedName("port-alias-qualified-name");
        portAlias.setDisplayName("port-alias-display-name");
        portAlias.setPortType(PortType.INPUT_PORT);
        return portAlias;
    }

    /**
     * Delete a PortAlias using the dataEngineClient provided
     *
     * @param userId user
     * @param dataEngineClient data engine client
     * @param qualifiedName qualified name
     * @param guid guid
     */
    public void deletePortAlias(String userId, DataEngineClient dataEngineClient, String qualifiedName, String guid)
            throws UserNotAuthorizedException, ConnectorCheckedException, PropertyServerException, InvalidParameterException {
        if(qualifiedName == null || guid == null){
            throw new IllegalArgumentException("Unable to delete PortAlias. QualifiedName and Guid are both required. Missing at least one");
        }
        dataEngineClient.deletePortAlias(userId, qualifiedName, guid);
    }

}
