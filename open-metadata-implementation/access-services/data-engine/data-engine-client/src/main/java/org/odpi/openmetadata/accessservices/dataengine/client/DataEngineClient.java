/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.client;

import org.odpi.openmetadata.accessservices.dataengine.model.*;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * DataEngineClient provides the client-side interface for a data engine tool to create processes with ports,
 * schemas and relationships.
 */
public interface DataEngineClient {

    /**
     * Create or update the processes, with all the ports, schema types and corresponding relationships including
     * process hierarchy relationships.
     *
     * @param userId    the name of the calling user
     * @param processes list of processes
     *
     * @return unique identifier of the process in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     * @throws ConnectorCheckedException problem with the underlying connector (if used)
     */
    List<String> createOrUpdateProcesses(String userId, List<Process> processes) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException,
                                                                                        ConnectorCheckedException;

    /**
     * Create or update the software server capability entity
     *
     * @param userId                   the name of the calling user
     * @param softwareServerCapability the software server capability bean
     *
     * @return unique identifier of the server in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     * @throws ConnectorCheckedException problem with the underlying connector (if used)
     */
    String createExternalDataEngine(String userId, SoftwareServerCapability softwareServerCapability) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException,
                                                                                                             ConnectorCheckedException;
    /**
     * Create or update the schema type entity, with the corresponding schema attributes and relationships
     *
     * @param userId     the name of the calling user
     * @param schemaType the schema type bean
     *
     * @return unique identifier of the schema type in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     * @throws ConnectorCheckedException problem with the underlying connector (if used)
     */
    String createOrUpdateSchemaType(String userId, SchemaType schemaType) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException,
                                                                                 ConnectorCheckedException;
    /**
     * Create or update the port implementation entity,with the corresponding schema type and port schema relationship
     *
     * @param userId             the name of the calling user
     * @param portImplementation the port implementation bean
     *
     * @return unique identifier of the port implementation in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     * @throws ConnectorCheckedException internal problem with the connector
     */
    String createOrUpdatePortImplementation(String userId, PortImplementation portImplementation) throws
                                                                                                  InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException,
                                                                                                  ConnectorCheckedException;

    /**
     * Create or update the port alias entity with a PortDelegation relationship
     *
     * @param userId    the name of the calling user
     * @param portAlias the port alias bean
     *
     * @return unique identifier of the port alias in the repository
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     * @throws ConnectorCheckedException internal problem with the connector
     */
    String createOrUpdatePortAlias(String userId, PortAlias portAlias) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException,
                                                                              ConnectorCheckedException;

    /**
     * Add a ProcessHierarchy relationship to a process
     *
     * @param userId           the name of the calling user
     * @param processHierarchy the process hierarchy bean
     *
     * @return the unique identifier (guid) of the child of the process hierarchy that was updated
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     * @throws ConnectorCheckedException internal problem with the connector
     */
    String addProcessHierarchy(String userId, ProcessHierarchy processHierarchy) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException,
                                                                                        ConnectorCheckedException;

    /**
     * Add lineage mapping relationships between schema types
     *
     * @param userId          the name of the calling user
     * @param lineageMappings list of lineage mappings
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     * @throws ConnectorCheckedException internal problem with the connector
     */
    void addLineageMappings(String userId, List<LineageMapping> lineageMappings) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException,
                                                                                        ConnectorCheckedException;

    /**
     * Add ports and process ports relationship to an existing port
     *
     * @param userId      the name of the calling user
     * @param portGUIDs   the list of port GUIDs
     * @param processGUID the process GUYID
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     * @throws ConnectorCheckedException internal problem with the connector
     */
    void addPortsToProcess(String userId, List<String> portGUIDs, String processGUID) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException,
                                                                                             ConnectorCheckedException;

    /**
     * Sets external source system name using the data engine client
     * @param externalSourceName Source system name
     */
    void setExternalSourceName(String externalSourceName);

    /**
     * Returns the name of the source system using data engine client
     * @return Source system name
     */
    String getExternalSourceName();
}
