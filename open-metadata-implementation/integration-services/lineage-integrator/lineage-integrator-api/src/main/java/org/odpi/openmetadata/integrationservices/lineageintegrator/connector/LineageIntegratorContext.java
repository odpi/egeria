/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.lineageintegrator.connector;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineRESTClient;
import org.odpi.openmetadata.accessservices.dataengine.model.*;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * LineageIntegratorContext provides a wrapper around the Data Engine OMAS client.
 * It provides the simplified interface to open metadata needed by the LineageIntegratorConnector.
 */
public class LineageIntegratorContext
{
    private DataEngineRESTClient dataEngineClient;
    private String               userId;

    /**
     * Create a new context for a connector.
     *
     * @param dataEngineClient common client to map requests to
     * @param userId integration daemon's userId
     */
    public LineageIntegratorContext(DataEngineRESTClient dataEngineClient,
                                    String               userId)
    {
        this.dataEngineClient = dataEngineClient;
        this.userId           = userId;
    }


    /**
     * Create or update the processes, with all the ports, schema types and corresponding relationships including
     * process hierarchy relationships.
     *
     * @param processes list of processes
     *
     * @return unique identifier of the process in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public List<String> createOrUpdateProcesses(List<Process> processes) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return dataEngineClient.createOrUpdateProcesses(userId, processes);
    }


    /**
     * Create or update the schema type entity, with the corresponding schema attributes and relationships
     *
     * @param schemaType the schema type bean
     *
     * @return unique identifier of the schema type in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String createOrUpdateSchemaType(SchemaType schemaType) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        return dataEngineClient.createOrUpdateSchemaType(userId, schemaType);
    }


    /**
     * Create or update the port implementation entity, with the corresponding schema type and port schema relationship.
     *
     * @param portImplementation the port implementation bean
     *
     * @return unique identifier of the port implementation in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String createOrUpdatePortImplementation(PortImplementation portImplementation) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return dataEngineClient.createOrUpdatePortImplementation(userId, portImplementation);
    }


    /**
     * Create or update the port alias entity with a PortDelegation relationship
     *
     * @param portAlias the port alias bean
     *
     * @return unique identifier of the port alias in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String createOrUpdatePortAlias(PortAlias portAlias) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return dataEngineClient.createOrUpdatePortAlias(userId, portAlias);
    }


    /**
     * Add lineage mapping relationships between schema types
     *
     * @param lineageMappings list of lineage mappings
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void addLineageMappings(List<LineageMapping> lineageMappings) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        dataEngineClient.addLineageMappings(userId, lineageMappings);
    }


    /**
     * Add ports and process ports relationship to an existing port
     *
     * @param portGUIDs   the list of port GUIDs
     * @param processGUID the process GUID
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void addPortsToProcess(List<String> portGUIDs, String processGUID) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        dataEngineClient.addPortsToProcess(userId, portGUIDs, processGUID);
    }
}
