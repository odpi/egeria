/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.client;

import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.EventType;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFlow;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessHierarchy;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.Engine;
import org.odpi.openmetadata.accessservices.dataengine.model.Topic;
import org.odpi.openmetadata.accessservices.dataengine.rest.FindRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.List;
import java.util.Map;

/**
 * DataEngineClient provides the client-side interface for a data engine tool to create processes with ports,
 * schemas and relationships.
 */
public interface DataEngineClient {
    /**
     * Create or update the process, with all the ports, schema types and corresponding relationships including
     * the process hierarchy relationship.
     *
     * @param userId  the name of the calling user
     * @param process the process
     *
     * @return unique identifier of the process in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    String createOrUpdateProcess(String userId, Process process) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException,
                                                                        ConnectorCheckedException;

    /**
     * Delete a process
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name of the process to be deleted
     * @param guid          the unique identifier of the process to be deleted
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    void deleteProcess(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException,
                                                                                ConnectorCheckedException;


    /**
     * Create or update the engine entity
     *
     * @param userId the name of the calling user
     * @param engine the engine bean
     *
     * @return unique identifier of the server in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    String createExternalDataEngine(String userId, Engine engine) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException,
                                                                                                             ConnectorCheckedException;

    /**
     * Delete the external data engine
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name of the external data engine
     * @param guid          the unique identifier of the external data engine
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    void deleteExternalDataEngine(String userId, String qualifiedName, String guid) throws InvalidParameterException,
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
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    String createOrUpdateSchemaType(String userId, SchemaType schemaType) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException,
                                                                                 ConnectorCheckedException;

    /**
     * Delete the schema type
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name of the schema type
     * @param guid          the unique identifier of the schema type
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    void deleteSchemaType(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException,
                                                                                   ConnectorCheckedException;

    /**
     * Create or update the port implementation entity,with the corresponding schema type and port schema relationship. It attaches the port
     * implementation to the provided process
     *
     * @param userId               the name of the calling user
     * @param portImplementation   the port implementation bean
     * @param processQualifiedName the process qualified name
     *
     * @return unique identifier of the port implementation in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  internal problem with the connector
     */
    String createOrUpdatePortImplementation(String userId, PortImplementation portImplementation, String processQualifiedName) throws
                                                                                                                               InvalidParameterException,
                                                                                                                               UserNotAuthorizedException,
                                                                                                                               PropertyServerException,
                                                                                                                               ConnectorCheckedException;

    /**
     * Delete the port implementation
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name of port implementation
     * @param guid          the unique identifier of the port implementation
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    void deletePortImplementation(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException,
                                                                                           ConnectorCheckedException;

    /**
     * Create or update the port alias entity with a PortDelegation relationship. It attaches the port alias to the provided process
     *
     * @param userId               the name of the calling user
     * @param portAlias            the port alias bean
     * @param processQualifiedName the process qualified name
     *
     * @return unique identifier of the port alias in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  internal problem with the connector
     */
    String createOrUpdatePortAlias(String userId, PortAlias portAlias, String processQualifiedName) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException,
                                                                                                           ConnectorCheckedException;

    /**
     * Delete the port alias
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name of the port alias
     * @param guid          the unique identifier of the port alias
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    void deletePortAlias(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException,
                                                                                  ConnectorCheckedException;

    /**
     * Add a ProcessHierarchy relationship to a process
     *
     * @param userId           the name of the calling user
     * @param processHierarchy the process hierarchy bean
     *
     * @return the unique identifier (guid) of the child of the process hierarchy that was updated
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  internal problem with the connector
     */
    String addProcessHierarchy(String userId, ProcessHierarchy processHierarchy) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException,
                                                                                        ConnectorCheckedException;

    /**
     * Add data flow relationships between entities
     *
     * @param userId          the name of the calling user
     * @param dataFlows list of data flows
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  internal problem with the connector
     */
    void addDataFlows(String userId, List<DataFlow> dataFlows) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException,
                                                                      ConnectorCheckedException;

    /**
     * Sets external source system name using the data engine client
     *
     * @param externalSourceName Source system name
     */
    void setExternalSourceName(String externalSourceName);

    /**
     * Returns the name of the source system using data engine client
     *
     * @return Source system name
     */
    String getExternalSourceName();

    /**
     * Create or update the database entity
     *
     * @param userId   the name of the calling user
     * @param database the database bean
     *
     * @return unique identifier of database in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  internal problem with the connector
     */
    String upsertDatabase(String userId, Database database) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException,
                                                                   ConnectorCheckedException;

    /**
     * Create or update the database schema entity
     *
     * @param userId                the name of the calling user
     * @param databaseSchema        the database schema bean
     * @param databaseQualifiedName the qualified name of the database, in case it is known
     *
     * @return unique identifier of database schema in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  internal problem with the connector
     */
    String upsertDatabaseSchema(String userId, DatabaseSchema databaseSchema, String databaseQualifiedName) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException,
                                                                                                                   ConnectorCheckedException;

    /**
     * Create or update the relational table entity
     *
     * @param userId                      the name of the calling user
     * @param relationalTable             the relational table bean
     * @param databaseSchemaQualifiedName the qualified name of the database schema to which it will be related
     *
     * @return unique identifier of the relational table in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  internal problem with the connector
     */
    String upsertRelationalTable(String userId, RelationalTable relationalTable, String databaseSchemaQualifiedName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, ConnectorCheckedException;

    /**
     * Create or update the data file entity
     *
     * @param userId   the name of the calling user
     * @param dataFile the data file  bean
     *
     * @return unique identifier of the relational table in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  internal problem with the connector
     */
    String upsertDataFile(String userId, DataFile dataFile) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException,
                                                                   ConnectorCheckedException;

    /**
     * Delete the database
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name of the database
     * @param guid          the unique identifier of the database
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    void deleteDatabase(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException,
                                                                                 ConnectorCheckedException;

    /**
     * Delete the database schema
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name of the database schema
     * @param guid          the unique identifier of the database schema
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    void deleteDatabaseSchema(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException,
                                                                                       ConnectorCheckedException;

    /**
     * Delete the relational table
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name of the relational table
     * @param guid          the unique identifier of the relational table
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    void deleteRelationalTable(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException,
                                                                                        ConnectorCheckedException;

    /**
     * Delete the data file
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name of the data file
     * @param guid          the unique identifier of the data file
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    void deleteDataFile(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException,
                                                                                 ConnectorCheckedException;

    /**
     * Delete the folder
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name of the data file
     * @param guid          the unique identifier of the folder
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    void deleteFolder(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException,
                                                                               ConnectorCheckedException;

    /**
     * Delete the connection
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name of the connection
     * @param guid          the unique identifier of the connection
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    void deleteConnection(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException,
                                                                                   ConnectorCheckedException;

    /**
     * Delete the endpoint
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name of the endpoint
     * @param guid          the unique identifier of the endpoint
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    void deleteEndpoint(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException,
                                                                                 ConnectorCheckedException;

    /**
     * Find an entity
     *
     * @param userId          the name of the calling user
     * @param findRequestBody request body
     *
     * @return list of found entities
     *
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws ConnectorCheckedException     problem with the underlying connector (if used)
     * @throws FunctionNotSupportedException this request is not supported in the target repositories
     */
    GUIDListResponse find(String userId, FindRequestBody findRequestBody) throws ConnectorCheckedException,
                                                                                 InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException,
                                                                                 FunctionNotSupportedException;

    /**
     * Create or update the topic entity
     *
     * @param userId the name of the calling user
     * @param topic  the topic bean
     *
     * @return unique identifier of topic in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  internal problem with the connector
     */
    String upsertTopic(String userId, Topic topic) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException,
                                                          ConnectorCheckedException;

    /**
     * Create or update the event type entity
     *
     * @param userId             the name of the calling user
     * @param eventType          the event type bean
     * @param topicQualifiedName the qualified name of the topic
     *
     * @return unique identifier of event type in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  internal problem with the connector
     */
    String upsertEventType(String userId, EventType eventType, String topicQualifiedName) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException,
                                                                                                 ConnectorCheckedException;

    /**
     * Delete the topic
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name of the topic
     * @param guid          the unique identifier of the topic
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    void deleteTopic(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException,
                                                                              ConnectorCheckedException;

    /**
     * Delete the event type
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name of the event type
     * @param guid          the unique identifier of the event type
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  problem with the underlying connector (if used)
     */
    void deleteEventType(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException,
                                                                                  ConnectorCheckedException;

    /**
     * Create or update the engine's processing state classification with the provided properties
     *  @param userId      the name of the calling user
     * @param properties  properties of the processing state
     */
    void upsertProcessingState(String userId, Map<String, Long> properties) throws PropertyServerException,
            InvalidParameterException, UserNotAuthorizedException, ConnectorCheckedException;

    /**
     * Get the engine's processing state classification's properties
     *
     * @param userId      the name of the calling user
     */
    public Map<String, Long> getProcessingState(String userId) throws PropertyServerException;
}
